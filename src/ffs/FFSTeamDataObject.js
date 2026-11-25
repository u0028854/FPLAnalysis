import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';

export default class FFSTeamDataObject {
    constructor(teamName) {
        if(!teamName){
            throw new Error('Missing required parameter for FFSTeamDataObject');
        }

        this.teamName = teamName;
        this.teamValues = new Map();
    }

    addTeamValue(key, value){
        this.teamValues.set(key, value);
    }

    outputTeamData(){
        let retVal = this.teamName;
        let outputColumns = fplUtils.defineExcelTeamColumns().slice(1);

        outputColumns.forEach(columnHeader =>{
            let value = this.teamValues.get(columnHeader);

            if(value === null || value === undefined){
                console.error(' No value for ' + columnHeader + ' for ' + this.teamName);
                return null;
            }

            retVal += ',' + value;
        })
        
        retVal += '\n';

        return retVal;
    }

    toString(){
        return this.teamName + ' ' + this.teamValues;
    }
}