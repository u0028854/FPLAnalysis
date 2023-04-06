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

const mongoDBMethods = require('../mongoDBconnector.js');
const unirest = require("unirest");
const { argv } = require('process');

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
                let tempPlayer = jsonArray[i];
                let tempPlayerObject = {};
                tempPlayerObject.id = tempPlayer.id;
                tempPlayerObject._id = gameWeek + '_' + tempPlayer.id;
                tempPlayerObject.gw = gameWeek;
                tempPlayerObject.goals_conceded =  tempPlayer.stats.goals_conceded;
                tempPlayerObject.saves =  tempPlayer.stats.saves;
                tempPlayerObject.bps =  tempPlayer.stats.bps;
                tempPlayerObject.total_points =  tempPlayer.stats.total_points;

                tempPlayerObject.gameweekFixtures = [];
                
                let tempFixtures = tempPlayer.explain;
                
                for(let j = 0; j < tempFixtures.length; j++){
                    let tempPlayerFixture = {};
                    tempPlayerFixture.id = gameWeek + '_' + tempFixtures[j].fixture;
                    tempPlayerFixture.stats = [];
                    for(let k = 0; k < tempFixtures[j].stats.length; k++){
                        tempPlayerFixture.stats.push({'type': tempFixtures[j].stats[k].identifier, 'points': tempFixtures[j].stats[k].points, 'value': tempFixtures[j].stats[k].value})
                    }

                    tempPlayerObject.gameweekFixtures.push(tempPlayerFixture);
                }
                
                retVal.push(tempPlayerObject);
            }

            resolve(retVal);
        });
    });
}

async function processGWEvent(gameWeek, dbName, dbCollection){
    let uriValue = 'https://fantasy.premierleague.com/api/event/' + gameWeek + '/live'
    let objectsToInsert = await processGWData(uriValue, gameWeek);
    await mongoDBMethods(objectsToInsert, dbName, dbCollection);
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
    let start = 1;
    let end = 38;
    let dbName;
    let dbCollection;

    if(argv){
        let argMap = new Map();

        for(let x = 0; x < argv.length; x++){
            if(x > 1){
                let splitArg = argv[x].split('=');
                if(splitArg.length === 2){
                    argMap.set(splitArg[0].trim(), splitArg[1].trim());
                }
            }
        }

        if(argMap.has('start') || argMap.has('end')){
            if(!argMap.has('start') || !argMap.has('end')){
                console.error('Invalid start and/or end parameters');
                process.exit(1);
            }

            start = parseInt(argMap.get('start').trim());
            end = parseInt(argMap.get('end').trim());

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

        if(argMap.has('dbName') || argMap.has('dbCollection')){
            dbName = argMap.get('dbName');
            dbCollection = argMap.get('dbCollection');

            if(!dbName || !dbCollection){
                console.error('dbName requires dbCollection and vice vesra');
                process.exit(1);
            }
        }
    }

    for(let i = start; i <= end; i++){
        await processGWEvent(i, dbName, dbCollection);
    }

    process.exit(0);
}

//main();

module.exports = extractGWData;