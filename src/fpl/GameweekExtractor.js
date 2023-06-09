/**
 * @description         Extracts gameweek deadlines from FPL
 * @param   writeToFile String form of boolean that determines if GW deadlines are written to file
 * @returns             Array of deadline dates with array index = (gameweek - 1)
 * @comment             To call from command line: npx run-func .\GameweekExtractor.js getGWSchedule "{writeToFile}"
 */

'use strict';

const unirest = require("unirest");
const fs = require("fs");

const DEFAULT_OUTPUTFILE = 'GWScheduleExtract.csv';

function getGWSchedule(writeToFile){
    return new Promise((resolve) => {
        const req = unirest.get("https://fantasy.premierleague.com/api/bootstrap-static/");
        let gwScheduleArray = [];
        req.end(function(res) {
            if (res.error) throw new Error(res.error);

            let jsonArray = res.toJSON().body.events;
            
            for (let i = 0; i < jsonArray.length; i++){
                let gwDate = Date.parse(jsonArray[i].deadline_time);
                gwScheduleArray[jsonArray[i].id - 1] = gwDate;
                if(writeToFile === 'true'){
                    fs.appendFileSync(DEFAULT_OUTPUTFILE, jsonArray[i].id + "," + gwDate + "\n", (err) => {
                        if (err) console.log(err);
                    });
                }
            }

            return resolve(gwScheduleArray);
        });
    });
}

function main(){
    getGWSchedule('true');
}

//main();

module.exports = getGWSchedule;