const fplUtils = require('../fplUtils.js');

module.exports = class UnderstatPlayerDataObject {
    constructor(name, minutes, xG, NPxG, xA, position, team) {
        if(!name || !team || !position || (minutes === undefined || minutes === null) || (xG === undefined || xG === null) || (NPxG === undefined || NPxG === null) || (xA === undefined || xA === null)){
            throw new Error('Missing required parameters for UnderstatPlayerDataObject');
        }

        this.playerName = fplUtils.removeSpecialChars(name);
        this.playerMinutes = minutes;
        this.playerXG = xG;
        this.playerNPXG = NPxG;
        this.playerXA = xA;
        this.playerPosition = position;
        this.playerTeam = team;
    }

    outputPlayerData(){
        // let delimiter = ',';
        // return this.playerName + delimiter + this.playerTeam + delimiter + this.opponentTeam + delimiter + this.minutesPlayed + delimiter + this.cleanSheets + delimiter + this.goalsConceded + delimiter + this.defConTotal + delimiter + this.clearances + delimiter + this.blocks + delimiter + this.interceptions + delimiter + this.recoveries + delimiter + this.totalTackles;
    }

    toString(){
        return this.playerName + ' ' + this.playerTeam + ' : ' + this.playerValues;
    }

    hashCode(){
        return UnderstatPlayerDataObject.getHashCode(this.playerName, this.playerTeam, this.playerPosition);
    }

    static getHashCode(playerName, playerTeam, playerPosition){
        return playerName + '-' + playerTeam + '-' + playerPosition;
    }

    getMapValues(){
        const retVal = new Map();

        retVal.set('')
    }
}