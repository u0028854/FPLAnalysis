'use strict';

const mongoDBMethods = require('./mongoDBconnector.js');
const fplUtils = require('./fplUtils.js');

function buildFPLPlayerMap(playerList, playerByFPLIdMap){
    let retVal = new Map();

    if(playerList){
        playerList.forEach(player => {
            let keyValue = player.name + '_' + player.fplGameWeek;

            if(playerByFPLIdMap.has(player.fplId.toString())){
                keyValue = playerByFPLIdMap.get(player.fplId.toString()) + '_' + keyValue;
            }

            retVal.set(keyValue, player);
        });
    }

    return retVal;
}

async function main(){
    let start = Number.parseFloat(await fplUtils.getProp('gwStart'));
    let end = Number.parseFloat(await fplUtils.getProp('gwEnd'));
    let uStatDBName = await fplUtils.getProp('uStatGWDatabase');
    let uStatDBCollection = await fplUtils.getProp('uStatGWDatabase');
    let fplDBName = await fplUtils.getProp('fplGWDatabase');
    let fplDBCollection = await fplUtils.getProp('fplGWDatabase');
    let comboDBName = await fplUtils.getProp('comboGWDatabase');
    let comboDBCollection = await fplUtils.getProp('comboGWDatabase');
    let playerByUStatIdMap = fplUtils.buildNameMap(await fplUtils.getProp('playerIdMapFile'), 1, 0);
    let playerByFPLIdMap = fplUtils.buildNameMap(await fplUtils.getProp('playerIdMapFile'), 0, 1);

    if(playerByUStatIdMap.size === 0 || playerByFPLIdMap.size === 0){
        console.error('playerIdMap argument invalid');
        process.exit(1);
    }

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
        
    if(!uStatDBName || !uStatDBCollection){
        console.error('uStatDBName and uStatDBCollection required arguments');
        process.exit(1);
    }

    if(!fplDBName || !fplDBCollection){
        console.error('fplDBName and fplDBCollection required arguments');
        process.exit(1);
    }

    if(!comboDBName || !comboDBCollection){
        console.error('comboDBName and comboDBCollection required arguments');
        process.exit(1);
    }
        
    let uStatGWQuery = [];
    let fplGWQuery = [];

    for(let i = start; i <= end; i++){
        uStatGWQuery.push({ustatGameWeek : i});
        fplGWQuery.push({fplGameWeek : i});
    }

    let query = JSON.parse('{\"$or\" : ' + JSON.stringify(uStatGWQuery) + '}');

    let uStatPlayerData = await mongoDBMethods.queryObjectData(query, uStatDBName, uStatDBCollection, 'processCombinedGWPlayerData.main uStatPlayerData');

    query = JSON.parse('{\"$or\" : ' + JSON.stringify(fplGWQuery) + '}');
    let fplPlayerData = await mongoDBMethods.queryObjectData(query, fplDBName, fplDBCollection, 'processCombinedGWPlayerData.main fplPlayerData');

    let fplPlayerMap = buildFPLPlayerMap(fplPlayerData, playerByFPLIdMap);
    let combinedPlayers = [];
    
    for(let i = 0; i < uStatPlayerData.length; i++){
        let tempPlayer = uStatPlayerData[i];
        let tempPlayerKeyValue = tempPlayer.player + '_' + tempPlayer.ustatGameWeek;

        if(playerByUStatIdMap.has(tempPlayer.player_id)){
            tempPlayerKeyValue = tempPlayer.player_id + '_' + tempPlayerKeyValue;
        }

        let tempFPLPlayer = fplPlayerMap.get(tempPlayerKeyValue);

        if(tempFPLPlayer){
            let fplGWFixtures = tempFPLPlayer.gameweekFixtures;

            if(!fplGWFixtures){
                console.error('No gameweekFixtures array for ' + tempFPLPlayer.name + ' in GW' + tempFPLPlayer.fplGameWeek)
                break;
            }

            let fixtureFound = false;

            for(let i = 0; i < fplGWFixtures.length && !fixtureFound; i++){
                let tempFixture = fplGWFixtures[i];

                if(tempPlayer.homeTeam === tempFixture.homeTeamName && tempPlayer.awayTeam === tempFixture.awayTeamName){
                    fixtureFound = true;
                    
                    let statsArray = tempFixture.stats;
                    let totalPoints = 0;

                    for(let j = 0; j < statsArray.length; j++){
                        tempPlayer[statsArray[j].type.toString()] = statsArray[j].value;
                        tempPlayer[statsArray[j].type.toString() + '_points' ] = statsArray[j].points;
                        totalPoints = totalPoints + statsArray[j].points;
                    }

                    tempPlayer.totalPoints = totalPoints;
                }
            }

            if(fixtureFound){
                tempPlayer.position = tempFPLPlayer.position;
                tempPlayer.fplId = tempFPLPlayer.fplId;
                let xPts = 0.0;

                switch(tempPlayer.position){
                    case 'GKP':
                        xPts = tempPlayer.minutes_points;

                        if(xPts === 2){
                            xPts += (tempPlayer.cs_odds * 4);
                        }

                        xPts += (Number.parseFloat(tempPlayer.xG) * 6);
                        xPts += (Number.parseFloat(tempPlayer.xA) * 3);
                        break;
                    case 'DEF':
                        xPts = tempPlayer.minutes_points;

                        if(xPts === 2){
                            xPts += (tempPlayer.cs_odds * 4);
                        }

                        xPts += (Number.parseFloat(tempPlayer.xG) * 6);
                        xPts += (Number.parseFloat(tempPlayer.xA) * 3);
                        break;
                    case 'MID':
                        xPts = tempPlayer.minutes_points;

                        if(xPts === 2){
                            xPts += tempPlayer.cs_odds;
                        }

                        xPts += (Number.parseFloat(tempPlayer.xG) * 5);
                        xPts += (Number.parseFloat(tempPlayer.xA) * 3);
                        break;
                    case 'FWD':
                        xPts = tempPlayer.minutes_points;
                        xPts += (Number.parseFloat(tempPlayer.xG) * 4);
                        xPts += (Number.parseFloat(tempPlayer.xA) * 3);
                        break;
                    default:
                        console.error('Invalid position for ' + tempPlayer.player + ': ' + tempPlayer.position);
                        process.exit(1);
                }

                tempPlayer.xPts = xPts;

                combinedPlayers.push(tempPlayer);
            }
            else{
                console.log('Couldnt match UStat Fixture ' + tempPlayer.homeTeam + '-' + tempPlayer.awayTeam + ' in GW ' + tempPlayer.ustatGameWeek + ' for player ' + tempPlayer.player);
            }
        }
        else{
            console.log('Didnt find ' + tempPlayerKeyValue);
        }
    }

    await mongoDBMethods.insertObjectData(combinedPlayers, comboDBName, comboDBCollection, 'processCombinedGWPlayerData.main');
}

main();