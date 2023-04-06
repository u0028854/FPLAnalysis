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

const mongoDBMethods = require('../mongoDBconnector.js');
const unirest = require("unirest");
const { argv } = require('process');

async function processBaseData(uriValue, teamExtraction, playerExtraction, teamDataMap){
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
                        tempPlayerData.name = playerData[i].first_name + ' ' + playerData[i].second_name;
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

async function extractBaseData(teamExtraction, playerExtraction, teamDataMap){
    let uriValue = 'https://fantasy.premierleague.com/api/bootstrap-static/';
    return await processBaseData(uriValue, teamExtraction, playerExtraction, teamDataMap);
}

async function extractBaseDataAndInsert(teamExtraction, playerExtraction, dbName, dbCollection){
    let uriValue = 'https://fantasy.premierleague.com/api/bootstrap-static/';
    let objectsToInsert = await processBaseData(uriValue, teamExtraction, playerExtraction);
    await mongoDBMethods(objectsToInsert, dbName, dbCollection, null);
}

async function exportPlayerTeamData(options, teamDataMap){
    let teamExtraction = false;
    let playerExtraction = false;

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

    return await extractBaseData(teamExtraction, playerExtraction, teamDataMap);
}

async function main(){
    let teamExtraction = false;
    let playerExtraction = false;
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

        if(!argMap.has('options')){
            console.error('Missing option parameter');
            process.exit(1);
        }

        let options = argMap.get('options');

        if(argMap.has('dbName') || argMap.has('dbCollection')){
            dbName = argMap.get('dbName');
            dbCollection = argMap.get('dbCollection');

            if(!dbName || !dbCollection){
                console.error('dbName requires dbCollection and vice vesra');
                process.exit(1);
            }
        }

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

    await extractBaseDataAndInsert(teamExtraction, playerExtraction, dbName, dbCollection);
    
    process.exit(0);
}

// main();

module.exports = exportPlayerTeamData;