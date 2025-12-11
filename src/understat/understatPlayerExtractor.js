const fplUtils = require('../fplUtils.js');
const fs = require('fs');
const { argv } = require('process');

let DATEOFFSET = 86400000;
let DATETIMESTRING = '+06%3A00%3A00';

async function main(){
    let fileName = await fplUtils.getProp('gwScheduleFile');
    let fileData = fs.readFileSync(fileName, 'utf-8').split('\n');
    let gwArray = [];
    let last6GWOption = false;

    fileData.forEach(gwData => {
        gwArray.push(parseInt(gwData.split(',')[1]));
    });

    let startGW = 0;
    let endGW = 37;

    if(argv.length === 3 && argv[2].includes('last6GW')){
        last6GWOption = true;
        let currentTime = new Date().getTime();

        for(let i = 5; i < gwArray.length && gwArray[i] < currentTime; i++){
            endGW = i + 1;
        }

        startGW = endGW - 6  < 0 ? 0 : endGW - 6;
    }
    
    let gwStartDate = new Date(gwArray[startGW] - DATEOFFSET);
    let gwStartYear = gwStartDate.getFullYear();
    let gwStartMonth = gwStartDate.getMonth() + 1;
    let gwStartDay = gwStartDate.getDate();

    let gwStartDateString = gwStartYear + '-' + (gwStartMonth < 10 ? '0' : '') + gwStartMonth + '-' + (gwStartDay < 10 ? '0' : '') + gwStartDay + DATETIMESTRING;

    console.log('GW Start String: ' + gwStartDateString);

    const response = await fetch("https://understat.com/main/getPlayersStats/", {
    "headers": {
        "accept": "application/json, text/javascript, */*; q=0.01",
        "accept-language": "en-US,en;q=0.9",
        "cache-control": "no-cache",
        "content-type": "application/x-www-form-urlencoded; charset=UTF-8",
        "pragma": "no-cache",
        "sec-ch-ua": "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\"",
        "sec-ch-ua-mobile": "?0",
        "sec-ch-ua-platform": "\"Windows\"",
        "sec-fetch-dest": "empty",
        "sec-fetch-mode": "cors",
        "sec-fetch-site": "same-origin",
        "x-requested-with": "XMLHttpRequest",
        "cookie": "_ga=GA1.2.430755408.1754982789; UID=b05101d57f8f07da; _ym_uid=1763929900160077524; _ym_d=1763929900; _ga_5844WCVBK6=GS2.2.s1765145473$o106$g1$t1765146090$j21$l0$h0; PHPSESSID=saqfer5e3l8egll4mi3g1np0v7; _ym_isad=2; _ym_visorc=w; PROMOTIONS=eyI3Ijp7Im5hbWUiOiJhZHNlbnNlIiwidmlld3MiOlsxNzY1MjM4ODQ4ODg2LDE3NjUyMzg4NTA5ODAsMTc2NTIzODg1NjA4MiwxNzY1MjM5NDg4NjU4LDE3NjUyMzk2NTk2MjcsMTc2NTIzOTg4NTE3Nl0sImNsaWNrcyI6W119LCJkYXRldGltZSI6MTc2NTE0NTQ3Mjc1Mn0=",
        "Referer": "https://understat.com/league/EPL/2025"
    },
    "body": "league=EPL&season=2025&date_start=" + gwStartDateString,
    "method": "POST"
    });

    const body = await response.text();
    let playerJSONs = JSON.parse(body).players;
    let playerOutput = '';
    
    playerJSONs.forEach(playerJSON =>{
        playerOutput += fplUtils.removeSpecialChars(playerJSON.player_name) + ','
        + playerJSON.time + ','
        + (Math.round(playerJSON.xG * 100.0) / 100.0) + ','
        + (Math.round(playerJSON.npxG * 100.0) / 100.0) + ','
        + (Math.round(playerJSON.xA * 100.0) / 100.0) + ','
        + playerJSON.position + ','
        + playerJSON.team_title + '\n';

    });

    let outputFile = last6GWOption ? await fplUtils.getProp('uStat6GWPlayerOutputFile') : await fplUtils.getProp('uStatPlayerOutputFile');

    fs.writeFileSync(outputFile, playerOutput);
}

main();