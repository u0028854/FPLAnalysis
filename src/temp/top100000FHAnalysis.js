'use strict';

var unirest = require("unirest");
var fs = require("fs");
const { argv } = require('process');

var fileName = "ChipUsage.csv";
var urlBase = 'https://fantasy.premierleague.com/api/leagues-classic/314/standings/?page_standings=';

async function getURLData(url){
    return unirest.get(url);
}

let fhTracker = {"GW29_Count": 0, "GW29_High": 0, "GW29_Sum": 0, "GW34_Count": 0, "GW34_High": 0, "GW34_Sum": 0};
let regularTracker = {"GW29_Count": 0, "GW29_High": 0, "GW29_Sum": 0, "GW34_Count": 0, "GW34_High": 0, "GW34_Sum": 0};

async function processFHScores(teamData, consoleOutput){
    try{
        let jsonBody = teamData.toJSON().body;
        let teamChips = jsonBody.chips;
        let GW29Points = jsonBody.current[28].points - jsonBody.current[28].event_transfers_cost;
        let GW34Points = jsonBody.current[33].points - jsonBody.current[33].event_transfers_cost;
        
        for(let k = 0; k < teamChips.length; k++){
            let tempChipsObject = teamChips[k]; 
            if(tempChipsObject.name === 'freehit'){
                if(tempChipsObject.event === 29){
                    fhTracker.GW29_Count += 1;
                    fhTracker.GW29_Sum += GW29Points;

                    if(fhTracker.GW29_High < GW29Points){
                        fhTracker.GW29_High = GW29Points;
                    }
                }
                else{
                    regularTracker.GW29_Count += 1;
                    regularTracker.GW29_Sum += GW29Points;

                    if(regularTracker.GW29_High < GW29Points){
                        regularTracker.GW29_High = GW29Points;
                    }
                }
                
                if(tempChipsObject.event === 34){
                    fhTracker.GW34_Count += 1;
                    fhTracker.GW34_Sum += GW34Points;

                    if(fhTracker.GW34_High < GW34Points){
                        fhTracker.GW34_High = GW34Points;
                    }
                }
                else{
                    regularTracker.GW34_Count += 1;
                    regularTracker.GW34_Sum += GW34Points;

                    if(regularTracker.GW34_High < GW34Points){
                        regularTracker.GW34_High = GW34Points;
                    }
                }
            }
        }

        fhTracker.GW29_Avg = fhTracker.GW29_Sum / fhTracker.GW29_Count;
        fhTracker.GW34_Avg = fhTracker.GW34_Sum / fhTracker.GW34_Count;

        regularTracker.GW29_Avg = regularTracker.GW29_Sum / regularTracker.GW29_Count;
        regularTracker.GW34_Avg = regularTracker.GW34_Sum / regularTracker.GW34_Count;

        if(consoleOutput){
            console.log('fhTracker: ' + JSON.stringify(fhTracker));
            console.log('regularTracker: ' + JSON.stringify(regularTracker));
        }
    }
    catch (error){
        console.log(JSON.stringify(teamData));
        console.log(JSON.stringify(error));

        process.exit(1);
    }

    return fhTracker;
}

async function execute(startPageNumber, endPageNumber){
    let retVal;
    let executionCount = 0;
    for(let j = startPageNumber; j <= endPageNumber; j++){
        let res = await getURLData(urlBase + j);
        let jsonArray = res.toJSON().body.standings.results;

        for (let i = 0; i < jsonArray.length; i++){
            executionCount++;
            if(executionCount % 50 === 0){
                console.log('Processing team ' + executionCount + ' : ' + jsonArray[i].entry);
            }
            let teamData = await getURLData('https://fantasy.premierleague.com/api/entry/' + jsonArray[i].entry + '/history/');
            retVal = await processFHScores(teamData, executionCount % 50 === 0);
        }
    }

    return retVal;
}

async function main(){
    let startPageNumber = 1000;
    let endPageNumber = 2000;
    if(argv && argv.length === 4){
        startPageNumber = argv[2];
        endPageNumber = argv[3];

        if(startPageNumber > endPageNumber){
            startPageNumber = endPageNumber;
        }
    }

    let localFHTracker = await execute(startPageNumber, endPageNumber);

    try{
        // fs.writeFileSync(fileName, JSON.stringify(Object.fromEntries(localFHTracker)));
        // console.log(JSON.stringify(localFHTracker));
    } catch (err){
        console.errror(err);
    }
}

main();