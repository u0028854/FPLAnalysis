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
                console.log(uriValue);
                console.log(res.error);
                process.exit(1);
            }

            let jsonArray = res.toJSON().body?.elements;
            for (let i = 0; i < jsonArray.length; i++){
                let tempPlayer = {};
                tempPlayer.id = jsonArray[i].id;
                tempPlayer._id = gameWeek + '_' + jsonArray[i].id;
                tempPlayer.gw = gameWeek;
                tempPlayer.minutes =  jsonArray[i].stats.minutes;
                tempPlayer.goals_scored =  jsonArray[i].stats.goals_scored;
                tempPlayer.assists =  jsonArray[i].stats.assists;
                tempPlayer.clean_sheets =  jsonArray[i].stats.clean_sheets;
                tempPlayer.goals_conceded =  jsonArray[i].stats.goals_conceded;
                tempPlayer.own_goals =  jsonArray[i].stats.own_goals;
                tempPlayer.penalties_saved =  jsonArray[i].stats.penalties_saved;
                tempPlayer.penalties_missed =  jsonArray[i].stats.penalties_missed;
                tempPlayer.yellow_cards =  jsonArray[i].stats.yellow_cards;
                tempPlayer.red_cards =  jsonArray[i].stats.red_cards;
                tempPlayer.saves =  jsonArray[i].stats.saves;
                tempPlayer.bonus =  jsonArray[i].stats.bonus;
                tempPlayer.bps =  jsonArray[i].stats.bps;
                tempPlayer.total_points =  jsonArray[i].stats.total_points;

                retVal.push(tempPlayer);
            }

            resolve(retVal);
        });
    });
}

async function processGWEvent(gameWeek, dbName, dbCollection){
    let uriValue = 'https://fantasy.premierleague.com/api/event/' + gameWeek + '/live'
    let objectsToInsert = await processGWData(uriValue, gameWeek);
    console.log('objectsToInsert.length ' + objectsToInsert.length);
    await mongoDBMethods(objectsToInsert, dbName, dbCollection);
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
            dbName = argMap.get('dbCollection');

            if(!dbName || !dbCollection){
                console.error('dbName requires dbCollection and vice vesra');
                process.exit(1);
            }
        }
    }

    for(let i = start; i <= end; i++){
        console.log('processGWEvent begin');
        await processGWEvent(i, dbName, dbCollection);
        console.log('processGWEvent done');
    }

    process.exit(0);
}

main();