/**
 * @description     Extracts FFS player data from stored HTML pages on disk
 */
'use strict';

import { parse } from 'node-html-parser';
import fs from 'fs';
import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';
import FFSPlayerDataObject from '/Users/cdbak/workspace/FPLAnalysis/src/ffs/FFSPlayerDataObject.js';

const DEF_FILE_ONE = await fplUtils.getProp('ffsDefStatsFileOne');
const DEF_FILE_TWO = await fplUtils.getProp('ffsDefStatsFileTwo');
const DEF_FILE_THREE = await fplUtils.getProp('ffsDefStatsFileThree');
const MID_FILE_ONE = await fplUtils.getProp('ffsMidStatsFileOne');
const MID_FILE_TWO = await fplUtils.getProp('ffsMidStatsFileTwo');
const MID_FILE_THREE = await fplUtils.getProp('ffsMidStatsFileThree');
const FWD_FILE_ONE = await fplUtils.getProp('ffsFwdStatsFileOne');
const FWD_FILE_TWO = await fplUtils.getProp('ffsFwdStatsFileTwo');
const FWD_FILE_THREE = await fplUtils.getProp('ffsFwdStatsFileThree');

export async function getFFSPlayerData(){
    let playerMap = await processData(DEF_FILE_ONE, null, 'DEF');
    playerMap = await processData(DEF_FILE_TWO, playerMap, 'DEF');
    playerMap = await processData(DEF_FILE_THREE, playerMap, 'DEF');
    playerMap = await processData(MID_FILE_ONE, playerMap, 'MID');
    playerMap = await processData(MID_FILE_TWO, playerMap, 'MID');
    playerMap = await processData(MID_FILE_THREE, playerMap, 'MID');
    playerMap = await processData(FWD_FILE_ONE, playerMap, 'FWD');
    playerMap = await processData(FWD_FILE_TWO, playerMap, 'FWD');
    playerMap = await processData(FWD_FILE_THREE, playerMap, 'FWD');

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