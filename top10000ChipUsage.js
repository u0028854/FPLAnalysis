'use strict';

var unirest = require("unirest");
var fs = require("fs");
const { Console } = require("console");
const { mainModule } = require("process");

var fileName = "ChipUsage.csv";
var urlBase = 'https://fantasy.premierleague.com/api/leagues-classic/314/standings/?page_standings=';
let teamsProcessed = 0;

let chipTracker = new Map();

    chipTracker.set('BB',0);
    chipTracker.set('BBFH',0);
    chipTracker.set('BBFHFH',0);
    chipTracker.set('BBFHFH3XC',0);
    chipTracker.set('BBFHFH3XCWC',0);
    chipTracker.set('BBFH',0);
    chipTracker.set('BBFH3XC',0);
    chipTracker.set('BBFH3XCWC',0);
    chipTracker.set('BBFHWC',0);
    chipTracker.set('BB3XC',0);
    chipTracker.set('BB3XCWC',0);
    chipTracker.set('BBWC',0);
    chipTracker.set('FH',0);
    chipTracker.set('FHFH',0);
    chipTracker.set('FHFH3XC',0);
    chipTracker.set('FHFH3XCWC',0);
    chipTracker.set('FH3XC',0);
    chipTracker.set('FH3XCWC',0);
    chipTracker.set('FHWC',0);
    chipTracker.set('FH',0);
    chipTracker.set('FH3XC',0);
    chipTracker.set('FH3XCWC',0);
    chipTracker.set('FHWC',0);
    chipTracker.set('3XC',0);
    chipTracker.set('3XCWC',0);

async function getURLData(url){
    return unirest.get(url);
}

async function getTeamChips(teamData){
    let teamChips = teamData.toJSON().body.chips;
    let teamChipTracker = ['WC', 'FH', 'FH', 'BB', '3XC'];
    
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
                teamChipTracker[teamChipTracker.indexOf('3XC')] = null;
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

async function execute(){
    let retVal;
    for(let j = 1; j <= 200; j++){
        let res = await getURLData(urlBase + j);
        let jsonArray = res.toJSON().body.standings.results;

        for (let i = 0; i < jsonArray.length; i++){
            console.log('Processing team ' + ++teamsProcessed);
            let teamData = await getURLData('https://fantasy.premierleague.com/api/entry/' + jsonArray[i].entry + '/history/');
            retVal = await getTeamChips(teamData);
        }
    }

    return retVal;
}

async function main(){
    let localChipTracker = await execute();

    console.log(localChipTracker);
}

main();

//getTeamChips().then((data) => outputTeamChips(data));