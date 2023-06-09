const fs = require('fs');
const { argv } = require('process');
const understatJSONExtractor = require('./understatJSONExtractor.js');
const fplUtils = require('../fplUtils.js');

const PLAYERDATA_VAR_NAME = 'datesData';
const SHOTDATA_VAR_NAME = 'shotsData';
const MATCHINFOR_VAR_NAME = 'match_info';

async function main(){
    if(argv.length === 3){
        let understatInputFile = argv[2];
        let playerArray = await understatJSONExtractor(PLAYERDATA_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), true);
        let shotsArray = await understatJSONExtractor(SHOTDATA_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), true);
        let matchInfo = await understatJSONExtractor(MATCHINFOR_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), false);
        
        let combinedPlayers = {...playerArray.h, ...playerArray.a};

        for (const x in combinedPlayers){
            console.log(fplUtils.buildNameMap(combinedPlayers[x].player, 2, 4));
        }
    }
    else{
        console.error('Missing input file parameter');
    }
}

main();