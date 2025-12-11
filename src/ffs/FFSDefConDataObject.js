const fplUtils = require('../fplUtils.js');

module.exports = class FFSDefConDataObject {
    constructor(defensiveContributionsStringArray, fileName) {
        if(!Array.isArray(defensiveContributionsStringArray) || defensiveContributionsStringArray.length !== 26){
            throw new Error('FFSDefConDataObject array is not an array with 26 elements');
        }

        let playerNamePos = defensiveContributionsStringArray[2].split('<br>');
        this.playerName = fplUtils.translateHTMLCode(playerNamePos[0]);
        this.playerPosition = playerNamePos[1];
        this.playerTeam = defensiveContributionsStringArray[6];
        this.minutesPlayed = defensiveContributionsStringArray[9];
        this.cleanSheets = defensiveContributionsStringArray[10];
        this.goalsConceded = defensiveContributionsStringArray[11];
        this.defConTotal = defensiveContributionsStringArray[12];
        this.clearances = defensiveContributionsStringArray[14];
        this.blocks = defensiveContributionsStringArray[15];
        this.interceptions = defensiveContributionsStringArray[16];
        this.recoveries = defensiveContributionsStringArray[17];
        this.totalTackles = defensiveContributionsStringArray[18];
        this.opponentTeam = null;
        this.fileName = fileName;
    }

    setOpponentTeam(oppTeam){
        this.opponentTeam = oppTeam;
    }

    outputPlayerData(){
        let delimiter = ',';
        return this.playerName + delimiter + this.playerTeam + delimiter + this.opponentTeam + delimiter + this.minutesPlayed + delimiter + this.cleanSheets + delimiter + this.goalsConceded + delimiter + this.defConTotal + delimiter + this.clearances + delimiter + this.blocks + delimiter + this.interceptions + delimiter + this.recoveries + delimiter + this.totalTackles;
    }
}