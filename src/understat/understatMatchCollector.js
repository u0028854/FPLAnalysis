const fs = require('fs');
const { argv } = require('process');
const understatJSONExtractor = require('./understatJSONExtractor.js');

const MATCH_VAR_NAME = 'datesData';
const USTAT_MATCH_BASE_URL = 'https://understat.com/match/';
const ADD_MATCHES_TO_FILE = 'addMatches';
const DONOT_ADD_MATCHES_TO_FILE = 'dontAddMatches';
const REDOWNLOAD_ALL_MATCHES = 'redownloadMatches';

async function main(){
    if(argv.length >= 4){
        let currentMatchFile = argv.length === 4 ? 'matchList.txt' : argv[4];
        let matchOutput = [];
        let currentMatchList;

        if(currentMatchFile){
            try{
                currentMatchList = fs.readFileSync(currentMatchFile, "utf8").split('\n');
            }
            catch (error){
                if(error.code === 'ENOENT'){
                    console.log('Input file not found. No current match data inmported');
                }
                else{
                    console.error(error);
                    process.exit(1);
                }
            }
        }
        let understatInputFile = argv[2];
        let downloadOption = argv[3];
        let matchArray = await understatJSONExtractor(MATCH_VAR_NAME, fs.readFileSync(understatInputFile, "utf8"), true);

        matchArray.forEach(matchObject => {
            let matchURL = USTAT_MATCH_BASE_URL + matchObject.id;
            if((downloadOption === ADD_MATCHES_TO_FILE || downloadOption === DONOT_ADD_MATCHES_TO_FILE) && currentMatchList.includes(matchURL)){
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
        fs.writeFileSync(currentMatchFile, currentMatchList.join('\n'));        
    }
    else{
        console.error('Argument list invalid');
    }


    /**
     * 4. Once that is done, build code to process match files and upsert into database
     * 5. Processing includes player data as well as clean sheet percentage
     */
}

main();