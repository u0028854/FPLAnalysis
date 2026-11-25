/**
 * @description     Extracts FFS player data from stored HTML pages on disk
 */
'use strict';

import { parse } from 'node-html-parser';
import fs from 'fs';
import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';
import FFSPlayerDataObject from '/Users/cdbak/workspace/FPLAnalysis/src/ffs/FFSPlayerDataObject.js';

export async function getFFSPlayerData(sixWeekTimeFrame){
    let DEF_FILE_ONE = await fplUtils.getProp('ffsDefStatsFileOne');
    let DEF_FILE_TWO = await fplUtils.getProp('ffsDefStatsFileTwo');
    let DEF_FILE_THREE = await fplUtils.getProp('ffsDefStatsFileThree');
    let MID_FILE_ONE = await fplUtils.getProp('ffsMidStatsFileOne');
    let MID_FILE_TWO = await fplUtils.getProp('ffsMidStatsFileTwo');
    let MID_FILE_THREE = await fplUtils.getProp('ffsMidStatsFileThree');
    let FWD_FILE_ONE = await fplUtils.getProp('ffsFwdStatsFileOne');
    let FWD_FILE_TWO = await fplUtils.getProp('ffsFwdStatsFileTwo');
    let FWD_FILE_THREE = await fplUtils.getProp('ffsFwdStatsFileThree');

    if(sixWeekTimeFrame){
        DEF_FILE_ONE = await fplUtils.getProp('ffs6WeekDefStatsFileOne');
        DEF_FILE_TWO = await fplUtils.getProp('ffs6WeekDefStatsFileTwo');
        DEF_FILE_THREE = await fplUtils.getProp('ffs6WeekDefStatsFileThree');
        MID_FILE_ONE = await fplUtils.getProp('ffs6WeekMidStatsFileOne');
        MID_FILE_TWO = await fplUtils.getProp('ffs6WeekMidStatsFileTwo');
        MID_FILE_THREE = await fplUtils.getProp('ffs6WeekMidStatsFileThree');
        FWD_FILE_ONE = await fplUtils.getProp('ffs6WeekFwdStatsFileOne');
        FWD_FILE_TWO = await fplUtils.getProp('ffs6WeekFwdStatsFileTwo');
        FWD_FILE_THREE = await fplUtils.getProp('ffs6WeekFwdStatsFileThree');
    }

    let defPlayerMap = await processData(DEF_FILE_ONE, null, 'DEF');
    defPlayerMap = await processData(DEF_FILE_TWO, defPlayerMap, 'DEF');
    defPlayerMap = await processData(DEF_FILE_THREE, defPlayerMap, 'DEF');

    let midPlayerMap =await processData(MID_FILE_ONE, null, 'MID');
    midPlayerMap = await processData(MID_FILE_TWO, midPlayerMap, 'MID');
    midPlayerMap = await processData(MID_FILE_THREE, midPlayerMap, 'MID');

    let fwdPlayerMap =await processData(FWD_FILE_ONE, null, 'FWD');
    fwdPlayerMap = await processData(FWD_FILE_TWO, fwdPlayerMap, 'FWD');
    fwdPlayerMap = await processData(FWD_FILE_THREE, fwdPlayerMap, 'FWD');

    let playerMap = new Map();

    playerMap.set('DEF', defPlayerMap);
    playerMap.set('MID', midPlayerMap);
    playerMap.set('FWD', fwdPlayerMap);

    return playerMap;
}

function processData(filePath, playerMap, position){
    return new Promise((resolve) => {
        if(!playerMap){
            playerMap = new Map();
        }
    
        const statsTable = parse(fs.readFileSync(filePath, 'utf-8')).querySelectorAll(".stats.draggable.fixed3")[0];
        const statsTableBody = statsTable.children.filter(child => child.rawTagName === "tbody")[0];

        statsTableBody.children.forEach(row => {
            let rowChildren = row.children;
            let playerName = fplUtils.removeSpecialChars(rowChildren[1].children[0].getAttribute('title').trim());
            let playerTeam = rowChildren[2].childNodes[2].rawText.trim();
            let playerPosition = position;

            let newPlayer;

            if(playerMap.has(playerName)){
                newPlayer = playerMap.get(playerName);
            }
            else{
                newPlayer = new FFSPlayerDataObject(playerName, playerTeam, playerPosition);
            }

            for(let i = 4; i < rowChildren.length; i++){
                let keyValPair = rowChildren[i].getAttribute('title').trim();

                if(keyValPair){
                    let [key, value] = keyValPair.split(':').map(s => s.trim());
                    newPlayer.addPlayerValue(key, value);
                }
            }

            playerMap.set(newPlayer.playerName, newPlayer);
        });

        resolve(playerMap);
    });
}