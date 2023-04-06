const fs = require('fs');
const { argv } = require('process');
const understatJSONExtractor = require('./understatJSONExtractor.js');
const textDecoder = require('../textDecode.js');
const gwSchedule = require('../fpl/GameweekExtractor.js');

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

async function main(){
    if (argv.length === 3){
        let understatInputFile = argv[2];
        let gwScheduleArray = await gwSchedule('false');
        let playerArray = await understatJSONExtractor(PLAYERDATA_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), true);
        let shotsArray = await understatJSONExtractor(SHOTDATA_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), true);
        let matchInfo = await understatJSONExtractor(MATCHINFOR_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), false);
        let combinedPlayers = [];
        let homeShotData = [];
        let awayShotData = [];
        let matchGW = await findGWByDate(gwScheduleArray, Date.parse(matchInfo.date), 0, gwScheduleArray.length - 1);

        for (const x in shotsArray.a){
            awayShotData.push(shotsArray.a[x].xG);
        }

        for (const x in shotsArray.h){
            homeShotData.push(shotsArray.h[x].xG);
        }

        let awayCSOdds = await calculateCSOdds(awayShotData);
        let homeCSOdds = await calculateCSOdds(homeShotData);
        
        for (const x in playerArray.h){
            let tempObject = {};
            tempObject.player = textDecoder.latinise(playerArray.h[x].player);
            tempObject.id = playerArray.h[x].id;
            tempObject.xG = playerArray.h[x].xG;
            tempObject.player_id = playerArray.h[x].player_id;
            tempObject.team_id = playerArray.h[x].team_id;
            tempObject.team = matchInfo.team_h;
            tempObject.xA = playerArray.h[x].xA;
            tempObject.cs_odds = homeCSOdds;
            tempObject.game_week = matchGW;
            combinedPlayers.push(tempObject);
        }

        for (const x in playerArray.a){
            let tempObject = {};
            tempObject.player = textDecoder.latinise(playerArray.a[x].player);
            tempObject.id = playerArray.a[x].id;
            tempObject.xG = playerArray.a[x].xG;
            tempObject.player_id = playerArray.a[x].player_id;
            tempObject.team_id = playerArray.a[x].team_id;
            tempObject.team = matchInfo.team_a;
            tempObject.xA = playerArray.a[x].xA;
            tempObject.cs_odds = awayCSOdds;
            tempObject.game_week = matchGW;
            combinedPlayers.push(tempObject);
        }

        console.log(combinedPlayers);
    }
    /**
     * Do CS and GC model
     * 4. Once that is done, build code to upsert into database
     * 5. Processing includes player data as well as clean sheet percentage
     */
}

main();