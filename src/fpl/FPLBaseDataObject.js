const mongoDBMethods = require('../mongoDBconnector.js');
const fplUtils  = require('../fplUtils.js');
const fplBaseDataExtractor = require('./fplBasePlayerTeamDataExtractor.js');
const fplFixtureDataExtractor = require('./fplFixtureDataExtractor');


const DB_NAME = 'fplBaseDatabase';
const DB_COLLECTION = 'fplBaseDatabase';
const TEAM_NAME_MAP_FILE ='teamNameMapFile';
const PLAYER_NAME_MAP_FILE ='playerNameMapFile';

module.exports = class FPLBaseDataObject {
    constructor(getNewPlayerAndTeamData, getNewFixtureData, dbName, dbCollection) {
        return (async () => {
            if (FPLBaseDataObject._instance) {
                return FPLBaseDataObject._instance;
            }

           if(!dbName){
                dbName =  await fplUtils.getProp(DB_NAME);
            }

            if(!dbCollection){
                dbCollection =  await fplUtils.getProp(DB_COLLECTION);
            }

            if(getNewPlayerAndTeamData){
                await fplBaseDataExtractor('both', null, dbName, dbCollection, await fplUtils.getProp(TEAM_NAME_MAP_FILE), await fplUtils.getProp(PLAYER_NAME_MAP_FILE));
            }

            if(getNewFixtureData){
                await fplFixtureDataExtractor(dbName, dbCollection);
            }
    
            this._teamDataById = new Map();
            this._teamDataByName = new Map();
            this._fixtureData = new Map();
            this._playerDataByName = new Map();
            this._playerDataById = new Map();

            this.processResultSet(await mongoDBMethods.queryObjectData({ objectType : { $ne : "fixtureData" } }, dbName, dbCollection, 'FPLBaseDataObject.constructor'));
            this.processResultSet(await mongoDBMethods.queryObjectData({ objectType : "fixtureData"  }, dbName, dbCollection, 'FPLBaseDataObject.constructor'));

            FPLBaseDataObject._instance = this;
            return FPLBaseDataObject._instance;
          })();
    }

    processResultSet(baseDataResultSet){
        if(baseDataResultSet){
            baseDataResultSet.forEach(element => {
                if(element._id){
                    if(element._id.includes('fixtureData')){
                        if(this._teamDataById && this._teamDataById.size == 20){
                            let tempFixture = {};

                            tempFixture.awayTeamId = element.awayTeamId;
                            tempFixture.homeTeamId = element.homeTeamId;
                            tempFixture.awayTeamName = this._teamDataById.get(element.awayTeamId.toString()).name;
                            tempFixture.homeTeamName = this._teamDataById.get(element.homeTeamId.toString()).name;
                            tempFixture.dateTime = element.dateTime;
                            tempFixture.gameWeek = element.gameWeek;
                            tempFixture.dateTime = element.dateTime;
                            tempFixture.id = element._id.replace('fixtureData_','');

                            this._fixtureData.set(tempFixture.id, tempFixture);
                        }
                        else{
                            throw new Error('Team Data not yet initialized, cannot process fixture Data.');
                        }
                    }
                    else if(element._id.includes('teamData')){
                        let tempTeam = {};

                        tempTeam.name = element.name;
                        tempTeam.id = element._id.replace('teamData_','');

                        this._teamDataById.set(tempTeam.id, tempTeam);
                        this._teamDataByName.set(tempTeam.name, tempTeam);
                    }
                    else if(element._id.includes('playerData')){
                        let tempPlayer = {};

                        tempPlayer.name = this.name = fplUtils.removeSpecialChars(element.name);
                        tempPlayer.team = element.team;
                        tempPlayer.position = element.element_type;
                        tempPlayer.id = element._id.replace('playerData_','');

                        this._playerDataByName.set(tempPlayer.name, tempPlayer);
                        this._playerDataById.set(tempPlayer.id, tempPlayer);
                    }
                    else{
                        throw new Error('_id does not include playerData, teamData, or fixtureData');
                    }
                }
                else{
                    console.log(element);
                }
            });
        }
    }
}