/**
 * @description             Download the team selection data for a given gameweek range for a list, file, or individual teamID
 * @argument   teamId       Individual team ID to process
 * @argument   teamIdFile   File name expected to have team IDs on each line
 * @argument   teamIdList   Comma delimited list of team IDs
 * @argument   start        start of gameweek range. requires end if entered.
 * @argument   end          end of gameweek range. requires start if entered.
 * @argument   dbName       optional name for database. Requires dbCollection if entered
 * @argument   dbCollection optional name for db collection. Requires dbName if entered.
 * 
 * Gameweek Object Structure:
 *      teamId          :   {teamId}
 *      gameWeek        :   {gameWeek}
 *      _id             :   {teamId}_{gameWeek}
 *      active_chip     :   {active_chip}
 *      automatic_subs  :   {automatic_subs}
 *      playerSelections:   [{playerObjects}]
 * 
 * Player Object Structure:
 *      elementId       :   {element}
 *      multipler       :   {multiplier}
 */

'use strict';

const mongoDBMethods = require('../mongoDBconnector.js');
const unirest = require("unirest");
const { argv } = require('process');
const fs = require('fs');

async function getTeamListFromFile(fileName){
    let teamList = [];

    if(fileName){
        try{
            let fileData = fs.readFileSync(fileName, 'utf-8').split('\n');

            for(let x = 0; x < fileData.length; x++){
                let fileIdInt = parseInt(fileData[x].trim());
                if(!isNaN(fileIdInt)){
                    teamList.push(fileIdInt);
                }
            }
        }
        catch (error){
            if(error.code === 'ENOENT'){
                console.log('Input file not found. No team data downloaded.');
            }
            else{
                console.error(error);
                process.exit(1);
            }
        }
    }

    return teamList;
}

async function processGWData(uriValue, gameWeek, teamId){
    return new Promise(resolve => {
        unirest.get(uriValue).end(function(res) {
            if (res.error){
                console.error(uriValue);
                console.error(res.error);
            }

            let jsonArray = res.toJSON().body;

            let gameWeekData = {};
            gameWeekData.teamId = teamId;
            gameWeekData.gameWeek = gameWeek;
            gameWeekData._id = teamId + '_' + gameWeek;
            gameWeekData.active_chip = jsonArray.active_chip;
            gameWeekData.automatic_subs = jsonArray.automatic_subs;

            if(jsonArray.picks){
                let playerSelections = [];
                for (let i = 0; i < jsonArray.picks.length; i++){
                    let playerObject = {};
                    playerObject.elementId = jsonArray.picks[i].element;
                    playerObject.multiplier = jsonArray.picks[i].multiplier;

                    if(playerObject.multiplier > 0){
                        playerSelections.push(playerObject);
                    }
                }

                gameWeekData.playerSelections = playerSelections;
            }

            resolve(gameWeekData);
        });
    });
}

async function processGWEvent(gameWeek, teamId, dbName, dbCollection){
    let objectToInsert;
    if(Array.isArray(teamId)){
        objectToInsert = [];
        for(let x = 0; x < teamId.length; x++){
            let uriValue = 'https://fantasy.premierleague.com/api/entry/' + teamId[x] + '/event/' + gameWeek + '/picks/';
            objectToInsert.push(await processGWData(uriValue, gameWeek, teamId[x]));
        }
    }
    else{
        let uriValue = 'https://fantasy.premierleague.com/api/entry/' + teamId + '/event/' + gameWeek + '/picks/';
        objectToInsert = await processGWData(uriValue, gameWeek, teamId);
    }

    await mongoDBMethods(objectToInsert, dbName, dbCollection);
}

async function exportTeamGWData(start, end, teamId){
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
        if(Array.isArray(teamId)){
            for(let x = 0; x < teamId.length; x++){
                let uriValue = 'https://fantasy.premierleague.com/api/entry/' + teamId[x] + '/event/' + gameWeek + '/picks/';
                retVal.push(await processGWData(uriValue, gameWeek, teamId[x]));
            }
        }
        else{
            let uriValue = 'https://fantasy.premierleague.com/api/entry/' + teamId + '/event/' + gameWeek + '/picks/';
            retVal.push(await processGWData(uriValue, gameWeek, teamId));
        }
    }

    return retVal;
}

async function main(){
    let start = 1;
    let end = 38;
    let teamId = 0;
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

        if(argMap.has('teamId')){
            teamId = parseInt(argMap.get('teamId'));
        }
        else if(argMap.has('teamIdFile')){
            teamId = await getTeamListFromFile(argMap.get('teamIdFile').trim());
        }
        else if(argMap.has('teamIdList')){
            let tempTeamIdArgs = argMap.get('teamIdList').split(',');
            teamId = [];

            for(let x = 0; x < tempTeamIdArgs.length; x++){
                let teamIdInt = parseInt(tempTeamIdArgs[x].trim());
                if(tempTeamIdArgs[x] && !isNaN(teamIdInt)){
                    teamId.push();
                }
            }
        }

        if((!Array.isArray(teamId) || teamId.length === 0) && (isNaN(teamId) || teamId < 1)){
            console.error('Invalid team Id');
            process.exit(1);
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
    else{
        console.error('Invalid argument string');
        retuprocess.exit(1);
    }

    for(let i = start; i <= end; i++){
        await processGWEvent(i, teamId, dbName, dbCollection);
    }

    process.exit(0);
}

// main();

module.exports = exportTeamGWData;