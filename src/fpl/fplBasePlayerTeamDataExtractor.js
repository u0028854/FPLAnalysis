/**
 * @description Pulls base player and team data and inserts into database
 * @argument    options         players/team/both   Determines if players, team or both data will be downloaded
 * @argument    dbName          optional name for database. Requires dbCollection if entered
 * @argument    dbCollection    optional name for db collection. Requires dbName if entered.
 * Player Object structure:
 *      _id         :   playerData_{player id}
 *      objectType  :   playerData
 *      name        :   {player name}
 *      team        :   {team name}
 *      now_cost    :   {cost of player currently}
 *      start_cost  :   {cost of player at start}
 *      element_type:   MID/DEF/FWD/GKP
 * 
 * Team Object structure:
 *      _id         :   teamData_{team id}
 *      objectType  :   teamData
 *      name        :   {team name}
 */

'use strict';

const { argv } = require('process');
const mongoDBMethods = require('../mongoDBconnector.js');
const fplUtils = require('../fplUtils.js');
const unirest = require("unirest");

async function processBaseData(uriValue, teamExtraction, playerExtraction, teamDataMap, teamNameMap, playerNameMap){
    return new Promise(resolve => {
        let retVal = [];
        unirest.get(uriValue).end(function(res) {
            if (res.error){
                console.error(uriValue);
                console.error(res.error);
            }

            let jsonArray = res.toJSON().body;

            if(jsonArray) {
                if(!teamDataMap){
                    teamDataMap = new Map();
                }
                
                let playerTypeMap = new Map();
                
                if(teamExtraction){
                    let teamData = jsonArray.teams;

                    for (let i = 0; i < teamData.length; i++){
                        let tempTeam = {};
                        
                        tempTeam.objectType = 'teamData';
                        tempTeam._id = tempTeam.objectType + '_' + teamData[i].id;
                        tempTeam.name = teamData[i].name;

                        if(teamNameMap.has(tempTeam.name)){
                            tempTeam.name = teamNameMap.get(tempTeam.name);
                        }
                        
                        retVal.push(tempTeam);
                        teamDataMap.set(teamData[i].id, tempTeam.name);
                    }
                }

                if(playerExtraction){
                    let playerData = jsonArray.elements;
                    let playerTypes = jsonArray.element_types;

                    for (let i = 0; i < playerTypes.length; i++){
                        let tempPlayerType = {};
                        
                        tempPlayerType.objectType = 'playerType';
                        tempPlayerType._id = tempPlayerType.objectType + '_' + playerTypes[i].id;
                        tempPlayerType.plural_name_short = playerTypes[i].plural_name_short;

                        playerTypeMap.set(playerTypes[i].id, tempPlayerType.plural_name_short);
                    }

                    for (let i = 0; i < playerData.length; i++){
                        let tempPlayerData = {};
                        
                        tempPlayerData.objectType = 'playerData';
                        tempPlayerData._id = tempPlayerData.objectType + '_' + playerData[i].id;
                        tempPlayerData.name = fplUtils.removeSpecialChars(playerData[i].first_name + ' ' + playerData[i].second_name);

                        if(playerNameMap && playerNameMap.has(tempPlayerData.name)){
                            tempPlayerData.name = playerNameMap.get(tempPlayerData.name);
                        }
                    
                        tempPlayerData.team = teamDataMap.get(playerData[i].team);
                        tempPlayerData.now_cost = playerData[i].now_cost;
                        tempPlayerData.start_cost = tempPlayerData.now_cost - playerData[i].cost_change_start;
                        tempPlayerData.element_type = playerTypeMap.get(playerData[i].element_type);

                        retVal.push(tempPlayerData);
                    }
                }
            }

            resolve(retVal);
        });
    });
}

async function extractBaseData(teamExtraction, playerExtraction, teamDataMap, teamNameMap, playerNameMap){
    let uriValue = 'https://fantasy.premierleague.com/api/bootstrap-static/';
    return await processBaseData(uriValue, teamExtraction, playerExtraction, teamDataMap, teamNameMap, playerNameMap);
}

async function extractBaseDataAndInsert(teamExtraction, playerExtraction, dbName, dbCollection, teamDataMap, teamNameMap, playerNameMap){
    let uriValue = 'https://fantasy.premierleague.com/api/bootstrap-static/';
    let objectsToInsert = await processBaseData(uriValue, teamExtraction, playerExtraction, teamDataMap, teamNameMap, playerNameMap);
    await mongoDBMethods.insertObjectData(objectsToInsert, dbName, dbCollection, 'fplBasePlayerTeamDataExtractor.extractBaseDataAndInsert');
}

async function exportPlayerTeamData(options, teamDataMap, dbName, dbCollection, teamNameMapFile, playerNameMapFile){
    let teamExtraction = false;
    let playerExtraction = false;
    let teamNameMap;
    let playerNameMap;

    if(options.toLowerCase() === 'players'){
        playerExtraction = true;
    }
    else if(options.toLowerCase() === 'teams'){
        teamExtraction = true;
    }
    else if(options.toLowerCase() === 'both'){
        playerExtraction = true;
        teamExtraction = true;
    }

    teamNameMap = fplUtils.buildNameMap(teamNameMapFile, 1, 0);
    playerNameMap = fplUtils.buildNameMap(playerNameMapFile, 4, 2);

    if(teamNameMap.size === 0){
        console.error('teamNameMapFile argument invalid');
        process.exit(1);
    }

    if(playerNameMap.size === 0){
        console.error('playerNameMapFile argument invalid');
        process.exit(1);
    }

    let retVal = await extractBaseData(teamExtraction, playerExtraction, teamDataMap, teamNameMap, playerNameMap);

    if(dbName && dbCollection){
        await mongoDBMethods.insertObjectData(retVal, dbName, dbCollection, 'fplBasePlayerTeamDataExtractor.exportPlayerTeamData');
    }

    return retVal;
}

async function main(){
    let teamExtraction = false;
    let playerExtraction = false;
    let teamNameMap = fplUtils.buildNameMap(await fplUtils.getProp('teamNameMapFile'), 1, 0);
    let playerNameMap = fplUtils.buildNameMap(await fplUtils.getProp('playerNameMapFile'), 4, 2);
    let dbName = await fplUtils.getProp('fplBaseDatabase');
    let dbCollection = await fplUtils.getProp('fplBaseDatabase');

    if(teamNameMap.size === 0){
        console.error('teamNameMapFile argument invalid');
        process.exit(1);
    }

    if(playerNameMap.size === 0){
        console.error('playerNameMapFile argument invalid');
        process.exit(1);
    }

    if(!dbName || !dbCollection){
        console.error('dbName requires dbCollection and vice vesra');
        process.exit(1);
    }

    if(argv){
        let argMap = fplUtils.buildArgsMap(argv);

        if(!argMap.has('options')){
            console.error('Missing options parameter');
            process.exit(1);
        }

        let options = argMap.get('options');

        if(options.toLowerCase() === 'players'){
            playerExtraction = true;
        }
        else if(options.toLowerCase() === 'teams'){
            teamExtraction = true;
        }
        else if(options.toLowerCase() === 'both'){
            playerExtraction = true;
            teamExtraction = true;
        }
        else{
            console.error('Options parameter invalid value. Please use \'players\', \'teams\', or \'both\'');
            process.exit(1);
        }
    }

    await extractBaseDataAndInsert(teamExtraction, playerExtraction, dbName, dbCollection, null, teamNameMap, playerNameMap);
    
    process.exit(0);
}

// main();

module.exports = exportPlayerTeamData;