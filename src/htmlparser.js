import fs from 'fs';
import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';
// import * as understatPlayerExtractor from '/Users/cdbak/workspace/FPLAnalysis/src/understat/understatPlayerExtractor.js';
import * as fplPlayerData from '/Users/cdbak/workspace/FPLAnalysis/src/fpl/fplPlayerData.js';
import * as ffsPlayerData from '/Users/cdbak/workspace/FPLAnalysis/src/ffs/ffsPlayerData.js';
import buildPlayerExcel from '/Users/cdbak/workspace/FPLAnalysis/src/excel.js';
import ExcelPlayerOutputObject from '/Users/cdbak/workspace/FPLAnalysis/src/excel/ExcelPlayerOutputObject.js';

/*
RUN npm i if module not found
*/

const PLAYER_NAME_MAP_FILE = await fplUtils.getProp('playerNameMapFile');

async function main(){
    const fplFFSPlayerNameMap = new Map();
    // const uStatFFSPlayerNameMap = new Map();
    const playerNameFile = fs.readFileSync(PLAYER_NAME_MAP_FILE, 'utf-8').split('\n');

    playerNameFile.forEach(line => {
        let [ffsName, fplName, uStatName] = line.split(',').map(s => s.trim());

        if(ffsName !== fplName){
            fplFFSPlayerNameMap.set(fplName, ffsName);
        }

        // if(ffsName !== uStatName){
        //     uStatFFSPlayerNameMap.set(uStatName, ffsName);
        // }
    });

    const playerMap = await ffsPlayerData.getFFSPlayerData();

    /*let uStatPlayerMap = await understatPlayerExtractor.getUStatPlayerData(false, uStatFFSPlayerNameMap);
    
    console.log(uStatPlayerMap.size);

    uStatPlayerMap.forEach((value, key) => {
        let playerName = value.playerName;
        let ffsPlayerObject = playerMap.get(playerName);

        if(ffsPlayerObject){
            ffsPlayerObject.uStatPlayerDataObject = value;
            playerMap.set(playerName, ffsPlayerObject);
        }
        else{
             console.log(playerName);
        }
    });*/

    const fplPlayerMap = await fplPlayerData.getFPLPlayerData(null, fplFFSPlayerNameMap);

    fplPlayerMap.forEach((value, key) => {
        let ffsPositionPlayerMap = playerMap.get(key);
        let fplPositionPlayerMap = value;

        fplPositionPlayerMap.forEach((value, key) => {
            let playerName = value.playerName;
            let ffsPlayerObject = ffsPositionPlayerMap.get(playerName);
       
            if(ffsPlayerObject){
                ffsPlayerObject.fplPlayerObject = value;
                ffsPositionPlayerMap.set(playerName, ffsPlayerObject);
            }
        });

        playerMap.set(key, ffsPositionPlayerMap);
    });

    buildPlayerExcel(playerMap);
}

main();