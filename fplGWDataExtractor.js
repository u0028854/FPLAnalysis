'use strict';

const mongoDBMethods = require('./mongoDBconnector.js');
const unirest = require("unirest");
const fs = require("fs");
const { argv } = require('process');

async function temp(uriValue, fileName, gameWeek){
    return new Promise(resolve => {
        let retVal = [];
        unirest.get(uriValue).end(function(res) {
            if (res.error){
                console.log(uriValue);
                console.log(res.error);
            }

            let jsonArray = res.toJSON().body?.elements;
            for (let i = 0; i < jsonArray.length; i++){
                let tempPlayer = {};
                tempPlayer.id = jsonArray[i].id;
                tempPlayer._id = gameWeek + '_' + jsonArray[i].id;
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

                if(tempPlayer.total_points > 10){
                    retVal.push(tempPlayer);
                }

                // fs.appendFileSync(fileName, JSON.stringify(tempPlayer) + "\n", (err) => {
                //     if (err){
                //         console.log(uriValue);
                //         console.log(err);
                //     }
                // });
                console.log('i is ' + i + ' retVal.length ' + retVal.length);
            }

            resolve(retVal);
            // resolve('resolved');
        });
    });
}

async function processGWEvent(gameWeek){
    // return new Promise(resolve => {
        let uriValue = 'https://fantasy.premierleague.com/api/event/' + gameWeek + '/live'
        let fileName = 'GW' + gameWeek + '.csv';
        let objectsToInsert = await temp(uriValue, fileName, gameWeek);
        console.log('objectsToInsert.length ' + objectsToInsert.length);
        await mongoDBMethods(objectsToInsert);
    // });
}

async function main(){
    let start = 1;
    let end = 38;

    if(argv && argv.length === 4){
        start = parseInt(argv[2]);
        end = parseInt(argv[3]);

        if(start > end){
            console.error('Start gameweek is greater than end gameweek');
            return;
        }
        else if(start < 1 || start > 38){
            console.error('Start gameweek is less than 1 or greater than 38');
            return;
        }
        else if(end < 1 || end > 38){
            console.error('End gameweek is less than 1 or greater than 38');
            return;
        }
    }

    for(let i = start; i <= end; i++){
        console.log('processGWEvent begin');
        await processGWEvent(i);
        console.log('processGWEvent done');
    }

    process.exit(0);
}

main();