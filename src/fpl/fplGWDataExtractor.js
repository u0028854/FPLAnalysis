/**
 * @description Downloads data for all players for a given gameweek range.
 * @argument    start           start of gameweek range. requires end if entered.
 * @argument    end             end of gameweek range. requires start if entered.
 * @argument    dbName          optional name for database. Requires dbCollection if entered
 * @argument    dbCollection    optional name for db collection. Requires dbName if entered.
 * Player Object structure and json field names in FPL api:
 *      id              :   {id}
 *      _id             :   {gameweek}_{id}
 *      gw              :   {gameweek}
 *      minutes         :   {minutes}
 *      goals_scored    :   {goeals_scored}
 *      assists         :   {assists}
 *      clean_sheets    :   {clean_sheets}
 *      goals_conceded  :   {goals_conceded}
 *      own_goals       :   {own_goals}
 *      penalties_saved :   {penalties_saved}
 *      penalties_missed:   {penalties_missed}
 *      yellow_cards    :   {yellow_cards}
 *      red_cards       :   {red_cards}
 *      saves           :   {saves}
 *      bonus           :   {bonus}
 *      bps             :   {bps}
 *      total_points    :   {total_points}
 */

'use strict';

const { argv } = require('process');
const mongoDBMethods = require('../mongoDBconnector.js');
const fplUtils = require('../fplUtils.js');
const FPLBaseObject = require('./FPLBaseDataObject.js');
const unirest = require("unirest");

async function processGWData(uriValue, gameWeek){
    return new Promise(resolve => {
        let retVal = [];
        unirest.get(uriValue).end(function(res) {
            if (res.error){
                console.error(uriValue);
                console.error(res.error);
                process.exit(1);
            }

            let jsonArray = res.toJSON().body?.elements;
            for (let i = 0; i < jsonArray.length; i++){
                let validFixtureFound = false;
                let tempPlayer = jsonArray[i];
                let tempPlayerObject = {};
                tempPlayerObject.fplId = tempPlayer.id;
                tempPlayerObject._id = gameWeek + '_' + tempPlayer.id;
                tempPlayerObject.fplGameWeek = gameWeek;
                tempPlayerObject.goals_conceded =  tempPlayer.stats.goals_conceded;
                tempPlayerObject.saves =  tempPlayer.stats.saves;
                tempPlayerObject.bps =  tempPlayer.stats.bps;
                tempPlayerObject.total_points =  tempPlayer.stats.total_points;

                tempPlayerObject.gameweekFixtures = [];
                
                let tempFixtures = tempPlayer.explain;
                
                for(let j = 0; j < tempFixtures.length; j++){
                    let tempPlayerFixture = {};
                    tempPlayerFixture._id = gameWeek + '_' + tempFixtures[j].fixture;
                    tempPlayerFixture.id = tempFixtures[j].fixture;
                    tempPlayerFixture.fplGameWeek = gameWeek;
                    tempPlayerFixture.stats = [];
                    for(let k = 0; k < tempFixtures[j].stats.length; k++){
                        if(tempFixtures[j].stats[k].identifier === 'minutes' && tempFixtures[j].stats[k].value !== 0){
                            validFixtureFound = true;
                        }

                        tempPlayerFixture.stats.push({'type': tempFixtures[j].stats[k].identifier, 'points': tempFixtures[j].stats[k].points, 'value': tempFixtures[j].stats[k].value});
                    }

                    tempPlayerObject.gameweekFixtures.push(tempPlayerFixture);
                }
                
                if(validFixtureFound){
                    retVal.push(tempPlayerObject);
                }
            }

            resolve(retVal);
        });
    });
}

async function extractGWData(start, end){
    let retVal = [];

    if(start || end){
        if(!start || !end){
            console.error('Invalid start and/or end parameters');
            process.exit(1);
        }

        if(isNaN(start) || isNaN(end)){
            console.error('Invalid start and/or end parameters');
            process.exit(1);
        }
        else if(start > end){
            console.error('Start gameweek is greater than end gameweek');
            process.exit(1);
        }
        else if(start < 1 || start > 38){
            console.error('Start gameweek is less than 1 or greater than 38');
            process.exit(1);
        }
        else if(end < 1 || end > 38){
            console.error('End gameweek is less than 1 or greater than 38');
            process.exit(1);
        }
    }
    else{
        start = 1;
        end = 38;
    }

    for(let gameWeek = start; gameWeek <= end; gameWeek++){
        let uriValue = 'https://fantasy.premierleague.com/api/event/' + gameWeek + '/live'
        retVal.push(await processGWData(uriValue, gameWeek));
    }

    return retVal;
}

async function main(){
    let start = Number.parseFloat(await fplUtils.getProp('gwStart'));
    let end = Number.parseFloat(await fplUtils.getProp('gwEnd'));
    let dbName = await fplUtils.getProp('fplGWDatabase');
    let dbCollection = await fplUtils.getProp('fplGWDatabase');
    let fixtureDatabaseName = await fplUtils.getProp('fplBaseDatabase');
    let fixtureDatabaseCollection = await fplUtils.getProp('fplBaseDatabase');
    let newPlayerAndTeamData = false;
    let getNewFixtureData = false;

    let argMap = fplUtils.buildArgsMap(argv);

    if(argMap.has('newPlayerAndTeamData')){
        newPlayerAndTeamData = argMap.get('newPlayerAndTeamData') === 'true' ? true : false;
    }

    if(argMap.has('getNewFixtureData')){
        getNewFixtureData = argMap.get('getNewFixtureData') === 'true' ? true : false;
    }

    if(isNaN(start) || isNaN(end)){
        console.error('Invalid start and/or end parameters');
        process.exit(1);
    }
    else if(start > end){
        console.error('Start gameweek is greater than end gameweek');
        process.exit(1);
    }
    else if(start < 1 || start > 38){
        console.error('Start gameweek is less than 1 or greater than 38');
        process.exit(1);
    }
    else if(end < 1 || end > 38){
        console.error('End gameweek is less than 1 or greater than 38');
        process.exit(1);
    }

    if(!dbName || !dbCollection){
        console.error('dbName requires dbCollection and vice vesra');
        process.exit(1);
    }

    let baseData = await new FPLBaseObject(newPlayerAndTeamData, getNewFixtureData, fixtureDatabaseName, fixtureDatabaseCollection);
    let retVal = [];

    for(let gameWeek = start; gameWeek <= end; gameWeek++){
        let uriValue = 'https://fantasy.premierleague.com/api/event/' + gameWeek + '/live'
        let gwData = await processGWData(uriValue, gameWeek);

        for(let j = 0; j < gwData.length; j++){
            let tempGWDataObject = gwData[j];

            if(baseData._playerDataById.get(tempGWDataObject.fplId.toString())){
                let basePlayerData = baseData._playerDataById.get(tempGWDataObject.fplId.toString());
                tempGWDataObject.name = basePlayerData.name;
                tempGWDataObject.position = basePlayerData.position;
                tempGWDataObject.team = basePlayerData.team;
            

                for(let k = 0; k < tempGWDataObject.gameweekFixtures.length; k++){
                    let tempFixture = tempGWDataObject.gameweekFixtures[k];
                    let tempBaseFixture = baseData._fixtureData.get(tempFixture.id.toString());
                    tempFixture.awayTeamName = tempBaseFixture.awayTeamName;
                    tempFixture.homeTeamName = tempBaseFixture.homeTeamName;
                    tempFixture.dateTime = tempBaseFixture.dateTime;
                    tempGWDataObject.gameweekFixtures[k] = tempFixture;
                }
            }
            else{
                console.error('No base data for player ID ' + tempGWDataObject.fplId.toString());
            }
        }
        retVal = retVal.concat(gwData);
    }

    if(dbName && dbCollection){
        await mongoDBMethods.insertObjectData(retVal, dbName, dbCollection, 'fplGWDataExtractor.main');
    }
    else{
        console.log(retVal);
    }

    process.exit(0);
}

main();

//module.exports = extractGWData;