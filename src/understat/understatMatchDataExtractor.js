'use strict';

const fs = require('fs');
const path = require('path');
const { argv } = require('process');
const understatJSONExtractor = require('./understatJSONExtractor.js');
const gwSchedule = require('../fpl/GameweekExtractor.js');
const mongoDBMethods = require('../mongoDBconnector.js');
const fplUtils = require('../fplUtils.js');

const PLAYERDATA_VAR_NAME = 'rostersData';
const SHOTDATA_VAR_NAME = 'shotsData';
const MATCHINFOR_VAR_NAME = 'match_info';

async function calculateCSOdds(shotArray){
    let csChance = 1;

    for(const x in shotArray){
        csChance = csChance * (1 - parseFloat(shotArray[x]));
    }

    return csChance;
}

async function buildPlayerObject(playerData, teamName, homeTeam, awayTeam, gameLocation, csOdds, gameWeek, playerNameMap){
    let retVal = {};

    retVal.player = fplUtils.removeSpecialChars(playerData.player);

    // if(playerNameMap && playerNameMap.has(retVal.player)){
    //     retVal.player = playerNameMap.get(retVal.player);
    // }

    retVal.ustatId = playerData.id;
    retVal._id = 'us_' + playerData.id;
    retVal.xG = playerData.xG;
    retVal.player_id = playerData.player_id;
    retVal.team_id = playerData.team_id;
    retVal.team = teamName;
    retVal.homeTeam = homeTeam;
    retVal.awayTeam = awayTeam;
    retVal.gameLocation = gameLocation;
    retVal.xA = playerData.xA;
    retVal.cs_odds = csOdds;
    retVal.ustatGameWeek = gameWeek;

    return retVal;
}

async function findGWByDate(gwScheduleArray, gameDate, indexStart, indexEnd){
    let computedIndex = indexStart + Math.floor((indexEnd - indexStart) / 2);
    let midpointDate = gwScheduleArray[computedIndex];
    
    if (gameDate === midpointDate || (indexStart === indexEnd)){
        if(computedIndex === 0){
            computedIndex = 1;
        }
        else if(computedIndex === 37 && gameDate > midpointDate){
            computedIndex = 38;
        }

        return computedIndex;
    }
    else if(gameDate < midpointDate){
        return await findGWByDate(gwScheduleArray, gameDate, indexStart, computedIndex);
    }
    else{
        return await findGWByDate(gwScheduleArray, gameDate, computedIndex + 1, indexEnd);
    }
}

async function processGwFile(uStatGameFile, playerNameMap){
    let gwScheduleArray = await gwSchedule('false');
    let playerArray = await understatJSONExtractor(PLAYERDATA_VAR_NAME, fs.readFileSync(uStatGameFile, "utf8"), true);
    let shotsArray = await understatJSONExtractor(SHOTDATA_VAR_NAME, fs.readFileSync(uStatGameFile, "utf8"), true);
    let matchInfo = await understatJSONExtractor(MATCHINFOR_VAR_NAME, fs.readFileSync(uStatGameFile, "utf8"), false);
    let retVal = [];
    let homeShotData = [];
    let awayShotData = [];
    let matchGW = await findGWByDate(gwScheduleArray, Date.parse(matchInfo.date), 0, gwScheduleArray.length - 1);

    for (const x in shotsArray.a){
        awayShotData.push(shotsArray.a[x].xG);
    }

    for (const x in shotsArray.h){
        homeShotData.push(shotsArray.h[x].xG);
    }

    let awayCSOdds = await calculateCSOdds(homeShotData);
    let homeCSOdds = await calculateCSOdds(awayShotData);
    
    for (const x in playerArray.h){
        retVal.push(await buildPlayerObject(playerArray.h[x], matchInfo.team_h, matchInfo.team_h, matchInfo.team_a, 'home', homeCSOdds, matchGW, playerNameMap));
    }

    for (const x in playerArray.a){
        retVal.push(await buildPlayerObject(playerArray.a[x], matchInfo.team_a, matchInfo.team_h, matchInfo.team_a, 'away', awayCSOdds, matchGW, playerNameMap));
    }

    return retVal;
}

async function main(){
    let understatInputDirectory = await fplUtils.getProp('uStatInputDirectory');
    let dbName = await fplUtils.getProp('uStatGWDatabase');
    let dbCollection = await fplUtils.getProp('uStatGWDatabase');
    let playerNameMap = fplUtils.buildNameMap(await fplUtils.getProp('playerNameMapFile'), 4, 2);
    let combinedPlayers = [];

    if(!dbName || !dbCollection){
        console.error('dbName requires dbCollection and vice vesra');
        process.exit(1);
    }

    if(playerNameMap.size === 0){
        console.error('playerNameMapFile argument invalid');
        process.exit(1);
    }

    if(argv){
        let argMap = fplUtils.buildArgsMap(argv);
        let understatInputFile;

        if(!understatInputDirectory && argMap.has('understatInputFile')){
            understatInputDirectory = argMap.get('understatInputFile');
        }
        else if(!understatInputDirectory && !understatInputFile){
            console.error('Invalid uStatInputFile and uStatInputDirectory arguments');
            process.exit(1);
        }

        if(understatInputDirectory){
            let directoryData = fs.readdirSync(understatInputDirectory);

            for(let i = 0; i < directoryData.length; i++){
                console.log('Processing file ' + directoryData[i]);
                combinedPlayers = combinedPlayers.concat(await processGwFile(path.resolve(understatInputDirectory, directoryData[i]), playerNameMap));
            }
        }
        else{
            combinedPlayers = await processGwFile(understatInputFile, playerNameMap);    
        }

        if(dbName && dbCollection){
            await mongoDBMethods.insertObjectData(combinedPlayers, dbName, dbCollection, 'understatMatchDataExtractor.main');
        }
        else{
            console.log(combinedPlayers);
        }
    }
}

main();