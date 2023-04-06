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
const unirest = require("unirest");
const { argv } = require('process');

async function processFixtureData(uriValue){
    return new Promise(resolve => {
        let retVal = [];
        unirest.get(uriValue).end(function(res) {
            if (res.error){
                console.log(uriValue);
                console.log(res.error);
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
    console.log('objectsToInsert.length ' + objectsToInsert.length);
    await mongoDBMethods(objectsToInsert, dbName, dbCollection);
}

async function main(){
    let dbName;
    let dbCollection;

    if(argv){
        let argMap = new Map();

        for(let x = 0; x < argv.length; x++){
            if(x > 1){
                let splitArg = argv[x].split('=');
                if(splitArg.length === 2){
                    argMap.set(splitArg[0].trim(), splitArg[1].trim());
                }
            }
        }

        if(argMap.has('dbName') || argMap.has('dbCollection')){
            dbName = argMap.get('dbName');
            dbName = argMap.get('dbCollection');

            if(!dbName || !dbCollection){
                console.error('dbName requires dbCollection and vice vesra');
                process.exit(1);
            }
        }
    }
    
    console.log('process fixture data begin');
    await extractFixtureData(dbName, dbCollection);
    console.log('process fixture data done');
    
    process.exit(0);
}

main();