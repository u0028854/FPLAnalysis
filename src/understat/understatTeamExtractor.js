const fplUtils = require('../fplUtils.js');
const fs = require('fs');

async function main(){
    const response = await fetch("https://understat.com/getLeagueData/EPL/2025", {
        "headers": {
            "accept": "application/json, text/javascript, */*; q=0.01",
            "accept-language": "en-US,en;q=0.9",
            "cache-control": "no-cache",
            "pragma": "no-cache",
            "sec-ch-ua": "\"Google Chrome\";v=\"143\", \"Chromium\";v=\"143\", \"Not A(Brand\";v=\"24\"",
            "sec-ch-ua-mobile": "?0",
            "sec-ch-ua-platform": "\"Windows\"",
            "sec-fetch-dest": "empty",
            "sec-fetch-mode": "cors",
            "sec-fetch-site": "same-origin",
            "x-requested-with": "XMLHttpRequest"
        },
        "referrer": "https://understat.com/league/EPL",
        "body": null,
        "method": "GET",
        "mode": "cors",
        "credentials": "include"
    });

    const body = await response.text();
    let teamJSONs = JSON.parse(body);
    let teamOutput = '';

    let i = 0;

    for (const teamIterator in teamJSONs.teams) {
        let tempTeamObject = teamJSONs.teams[teamIterator];
        let teamName = tempTeamObject.title;
        let teamObjectMap = new Map();
        let teamXG = 0.0;
        let teamXGA = 0.0;
        
        tempTeamObject.history.forEach(match => {
            let tempMatchObject = {};

            tempMatchObject.xG = match.xG;
            tempMatchObject.xGA = match.xGA;
            tempMatchObject.date = new Date(match.date);

            teamObjectMap.set(tempMatchObject.date, tempMatchObject);

            teamXG += tempMatchObject.xG;
            teamXGA += tempMatchObject.xGA;
        });

        let teamMatchDate = Array.from(teamObjectMap.keys());
        teamMatchDate.sort((a, b) => a - b);
        teamMatchDate.reverse();

        let team6GameXG = 0.0;
        let team6GameXGA = 0.0;

        for(let i = 0; i < teamMatchDate.length && i < 6; i++){
            team6GameXG += teamObjectMap.get(teamMatchDate.at(i)).xG;
            team6GameXGA += teamObjectMap.get(teamMatchDate.at(i)).xGA;
        }

        let roundedXG = Math.round(teamXG * 100) / 100;
        let roundedXGA = Math.round(teamXGA * 100) / 100;
        let rounded6XG = Math.round(team6GameXG * 100) / 100;
        let rounded6XGA = Math.round(team6GameXGA * 100) / 100;

        let teamOutputString = teamName + ',' + roundedXG + ',' + roundedXGA + ',' + rounded6XG + ',' + rounded6XGA;
        teamOutput += (teamOutput ? '\n' + teamOutputString : teamOutputString);
    }

    let outputFile = await fplUtils.getProp('uStatTeamOutputFile');

    fs.writeFileSync(outputFile, teamOutput);
}

main();