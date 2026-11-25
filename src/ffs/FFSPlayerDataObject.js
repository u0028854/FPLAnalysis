export default class FFSPlayerDataObject {
    constructor(name, team, position) {
        if(!name || !team || !position){
            throw new Error('Missing required parameters for FFSPlayerDataObject');
        }

        this.playerName = name;
        this.playerPosition = position;
        this.playerTeam = team;
        this.playerValues = new Map();
        this.uStatPlayerDataObject = null;
        this.fplPlayerObject = null;
    }

    addPlayerValue(key, value){
        this.playerValues.set(key, value);
    }

    outputPlayerData(){
        // let delimiter = ',';
        // return this.playerName + delimiter + this.playerTeam + delimiter + this.opponentTeam + delimiter + this.minutesPlayed + delimiter + this.cleanSheets + delimiter + this.goalsConceded + delimiter + this.defConTotal + delimiter + this.clearances + delimiter + this.blocks + delimiter + this.interceptions + delimiter + this.recoveries + delimiter + this.totalTackles;
    }

    toString(){
        return this.playerName + ' ' + this.playerTeam + ' ' + this.playerPosition + ' : ' + this.playerValues;
    }

    hashCode(){
        return FFSPlayerDataObject.getHashCode(this.playerName, this.playerTeam, this.playerPosition);
    }

    static getHashCode(playerName, playerTeam, playerPosition){
        return playerName + '-' + playerTeam + '-' + playerPosition;
    }
}