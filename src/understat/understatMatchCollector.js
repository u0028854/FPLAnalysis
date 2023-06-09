/**
 * @description     Processes the base EPL league file from Understat.com and extracts the match URLs. Can either add all or 
 * add only those not already in the input file.
 * @argument        understatInputFile Downloaded file of the base understat home page
 * @argument        downloadOption addMatches/redownloadMatches Determines if the list of URLs in the input file will be 
 * reprocessed or not
 * @argument        currentMatchFile name of file containing list of previously downloaded matches (matchlist.txt if not 
 * provided). The match URLs will be output to this file.
 */

const fs = require('fs');
const { argv } = require('process');
const understatJSONExtractor = require('./understatJSONExtractor.js');
const fplUtils = require('../fplUtils.js');

const MATCH_VAR_NAME = 'datesData';
const USTAT_MATCH_BASE_URL = 'https://understat.com/match/';
const ADD_MATCHES_TO_FILE = 'addMatches';
const REDOWNLOAD_ALL_MATCHES = 'redownloadMatches';

async function main(){
    let currentMatchFile = await fplUtils.getProp('matchListFile');
    let understatBaseEPLFile = await fplUtils.getProp('uStatBaseEPLFile');

    if(!understatBaseEPLFile){
        console.error('Invalid understatBaseEPLFile argument');
        process.exit(1);
    }

    if(!currentMatchFile){
        console.error('Invalid currentMatchFile argument');
        process.exit(1);
    }

    if(argv){
        let argMap = fplUtils.buildArgsMap(argv);

        let downloadOption;
        let currentMatchList = [];
        let matchOutput = [];

        if(argMap.has('downloadOption')){
            downloadOption = argMap.get('downloadOption');
        }
        else{
            console.error('Invalid downloadOption argument');
            process.exit(1);
        }
        
        if(currentMatchFile){
            try{
                currentMatchList = fs.readFileSync(currentMatchFile, "utf8").split('\n');
            }
            catch (error){
                if(error.code === 'ENOENT'){
                    console.log('Input file not found. No current match data imported');
                }
                else{
                    console.error(error);
                    process.exit(1);
                }
            }
        }
        
        let matchArray = await understatJSONExtractor(MATCH_VAR_NAME, fs.readFileSync(understatBaseEPLFile, "utf8"), true);

        matchArray.forEach(matchObject => {
            let matchURL = USTAT_MATCH_BASE_URL + matchObject.id;
            if((downloadOption === ADD_MATCHES_TO_FILE) && currentMatchList.includes(matchURL)){
                console.log(matchURL + ' already downloaded');
            }
            else {
                if(matchObject.isResult){
                    matchOutput.push(matchURL);
                }
            }
        });

        if(downloadOption === ADD_MATCHES_TO_FILE){
            currentMatchList = currentMatchList.concat(matchOutput);
        }
        else if(downloadOption === REDOWNLOAD_ALL_MATCHES){
            currentMatchList = matchOutput;
        }
        else{
            console.error('DownloadOption argument invalid');
            process.exit(1);
        }
        fs.writeFileSync(currentMatchFile, currentMatchList.join('\n'));        
    }
    else{
        console.error('Argument list invalid');
    }
}

main();