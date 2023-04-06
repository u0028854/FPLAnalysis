/**
 */

'use strict';

const mongoDBMethods = require('../mongoDBconnector.js');
const fplBasePlayerTeamDataExtract = require('./fplBasePlayerTeamDataExtractor.js');
const fplFixtureDataExtractor = require('./fplFixtureDataExtractor.js');
const fplGWDataExtractor = require('./fplGWDataExtractor.js');
const fplTeamGWDataExtractor = require('./fplTeamGWDataExtractor.js');
const unirest = require("unirest");
const { argv } = require('process');

async function main(){
    let teamBaseData;
    let playerBaseData;
    let fixtureBaseData;
    let gwDataArray;
    let teamGWDataArray;
    let teamDataMap = new Map();

    teamBaseData = await fplBasePlayerTeamDataExtract('teams');

    for(let i = 0; i < teamBaseData.length; i++){
        teamDataMap.set(parseInt(teamBaseData[i]['_id'].replace('teamData_','')), teamBaseData[i].name);
    }

    playerBaseData = await fplBasePlayerTeamDataExtract('players', teamDataMap);

    fixtureBaseData = await fplFixtureDataExtractor();

    gwDataArray = await fplGWDataExtractor(29, 29);

    teamGWDataArray = await fplTeamGWDataExtractor(29, 29, 132310);

    console.log(teamGWDataArray[0]);
}

main();