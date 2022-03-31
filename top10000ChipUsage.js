'use strict';

var unirest = require("unirest");
var fs = require("fs");
//const { Console } = require("console");
const { argv } = require('process');

var fileName = "ChipUsage.csv";
var urlBase = 'https://fantasy.premierleague.com/api/leagues-classic/314/standings/?page_standings=';
let teamsProcessed = 0;

let chipTracker = setChipTracker();

function setChipTracker(){
    let retVal = new Map();

    retVal.set('BB',0);
    retVal.set('BBFH',0);
    retVal.set('BBFHFH',0);
    retVal.set('BBFHFHTXC',0);
    retVal.set('BBFHFHTXCWC',0);
    retVal.set('BBFHFHWC',0);
    retVal.set('BBFH',0);
    retVal.set('BBFHTXC',0);
    retVal.set('BBFHTXCWC',0);
    retVal.set('BBFHWC',0);
    retVal.set('BBTXC',0);
    retVal.set('BBTXCWC',0);
    retVal.set('BBWC',0);
    retVal.set('FH',0);
    retVal.set('FHFH',0);
    retVal.set('FHFHTXC',0);
    retVal.set('FHFHWC',0);
    retVal.set('FHFHTXCWC',0);
    retVal.set('FHTXC',0);
    retVal.set('FHTXCWC',0);
    retVal.set('FHWC',0);
    retVal.set('FH',0);
    retVal.set('FHTXC',0);
    retVal.set('FHTXCWC',0);
    retVal.set('FHWC',0);
    retVal.set('TXC',0);
    retVal.set('TXCWC',0);
    retVal.set('TXCFH',0);
    retVal.set('WC',0);

    try {
        const data = fs.readFileSync(fileName, 'utf8');
        
        if(data && data != ''){
            let tempObj = JSON.parse(data);
        
            for (const [key, value] of Object.entries(tempObj)){
                retVal.set(key, retVal.get(key) + value);
            }
        }
    } catch (err) {
        console.error(err)
    }
    
    return retVal;
}

async function getURLData(url){
    return unirest.get(url);
}

async function getTeamChips(teamData){
    let teamChips = teamData.toJSON().body.chips;
    let teamChipTracker = ['WC', 'FH', 'FH', 'BB', 'TXC'];
    
    for(let k = 0; k < teamChips.length; k++){
        switch (teamChips[k].name) {
            case 'wildcard':
                if(teamChips[k].event >= 20){
                    teamChipTracker[teamChipTracker.indexOf('WC')] = null;
                }
                break;
            case 'freehit':
                teamChipTracker[teamChipTracker.indexOf('FH')] = null;
                break;
            case 'bboost':
                teamChipTracker[teamChipTracker.indexOf('BB')] = null;
                break;
            case '3xc':
                teamChipTracker[teamChipTracker.indexOf('TXC')] = null;
                break;
        }
    }

    let temp = [];

    for(const val of teamChipTracker.values()){
        if(val){
            temp.push(val);
        }
    }

    temp.sort();

    let key = '';

    for(const val of temp.values()){
        key += val;
    }

    if(key !== ''){
        chipTracker.set(key, chipTracker.get(key) + 1);
    }

    return chipTracker;
}

async function execute(startPageNumber, endPageNumber){
    let retVal;
    for(let j = startPageNumber; j <= endPageNumber; j++){
        let res = await getURLData(urlBase + j);
        let jsonArray = res.toJSON().body.standings.results;

        for (let i = 0; i < jsonArray.length; i++){
            console.log('Processing team ' + jsonArray[i].entry);
            let teamData = await getURLData('https://fantasy.premierleague.com/api/entry/' + jsonArray[i].entry + '/history/');
            retVal = await getTeamChips(teamData);
        }
    }

    return retVal;
}

async function main(){
    let startPageNumber = 1;
    let endPageNumber = 2000;
    if(argv && argv.length === 4){
        startPageNumber = argv[2];
        endPageNumber = argv[3];

        if(startPageNumber > endPageNumber){
            startPageNumber = endPageNumber;
        }
    }

    let localChipTracker = await execute(startPageNumber, endPageNumber);

    try{
        fs.writeFileSync(fileName, JSON.stringify(Object.fromEntries(localChipTracker)));
    } catch (err){
        console.errror(err);
    }
}

main();