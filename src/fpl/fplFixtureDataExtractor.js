/**
 * @description Pulls fixture data and inserts into database
 * @argument    dbName          optional name for database. Requires dbCollection if entered
 * @argument    dbCollection    optional name for db collection. Requires dbName if entered.
 * Fixture Object structure:
 *      _id         :   fixtureData_{id}
 *      objectType  :   fixtureData
 *      awayTeamId  :   {team_a}
 *      homeTeamId  :   {team_h}
 *      dateTime    :   {kickoff_time}
 *      gameWeek    :   {event}
 */

'use strict';

const mongoDBMethods = require('../mongoDBconnector.js');
const fplUtils = require('../fplUtils.js');
const unirest = require("unirest");

async function processFixtureData(uriValue){
    return new Promise(resolve => {
        let retVal = [];
        unirest.get(uriValue).end(function(res) {
            if (res.error){
                console.error(uriValue);
                console.error(res.error);
            }

            let jsonArray = res.toJSON().body;

            for (let i = 0; i < jsonArray.length; i++){
                let tempFixture = {};
                
                tempFixture.objectType = 'fixtureData';
                tempFixture._id = tempFixture.objectType + '_' + jsonArray[i].id;
                tempFixture.awayTeamId = jsonArray[i].team_a;
                tempFixture.homeTeamId = jsonArray[i].team_h;
                tempFixture.dateTime = jsonArray[i].kickoff_time;
                tempFixture.gameWeek = jsonArray[i].event;

                retVal.push(tempFixture);
            }

            resolve(retVal);
        });
    });
}

async function extractFixtureData(dbName, dbCollection){
    let uriValue = 'https://fantasy.premierleague.com/api/fixtures/';
    let objectsToInsert = await processFixtureData(uriValue);
    await mongoDBMethods.insertObjectData(objectsToInsert, dbName, dbCollection, 'fplFixtureDataExtractor.extractFixtureData');
}

async function exportFixtureData(dbName, dbCollection){
    let uriValue = 'https://fantasy.premierleague.com/api/fixtures/';
    let retVal = await processFixtureData(uriValue);

    if(dbName && dbCollection){
        await mongoDBMethods.insertObjectData(retVal, dbName, dbCollection, 'fplFixtureDataExtractor.exportFixtureData');
    }

    return retVal;
}

async function main(){
    let dbName = await fplUtils.getProp('fplBaseDatabase');
    let dbCollection = await fplUtils.getProp('fplBaseDatabase');

    if(!dbName || !dbCollection){
        console.error('dbName requires dbCollection and vice vesra');
        process.exit(1);
    }
    
    await extractFixtureData(dbName, dbCollection);
    
    process.exit(0);
}

//main();

module.exports = exportFixtureData;