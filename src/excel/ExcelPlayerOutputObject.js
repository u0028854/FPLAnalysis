import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';

export default class ExcelPlayerOutputObject {
    constructor(ffsPlayerData) {
        if(!ffsPlayerData){
            throw new Error('Missing required parameter for ExcelPlayerOutputObject');
        }

        this.playerName = ffsPlayerData.playerName;
        this.playerPosition = ffsPlayerData.playerPosition;
        this.playerTeam = ffsPlayerData.playerTeam;

        const ffsPlayerDataValues = ffsPlayerData.playerValues;

        if(!ffsPlayerDataValues){
            throw new Error('Missing playerValues for ' + ffsPlayerData.playerName);
        }

        const playerAppearances = Number(ffsPlayerDataValues.get('Appearances'));
        const playerShots = Number(ffsPlayerDataValues.get('Shots'));

        this.excelPlayerValues = new Map();
    
        let fsXPts = 0.00;
        let fsNPXPts = 0.00;
        let fsXA = Number(ffsPlayerDataValues.get('xA Expected Assists')).toFixed(2);
        let fsXG = Number(ffsPlayerDataValues.get('xG Expected Goals')).toFixed(2);
        let fsNPXG = Number(ffsPlayerDataValues.get('xG Non-Penalty')).toFixed(2);

        if(!isNaN(fsXA) && !isNaN(fsXG) && !isNaN(fsNPXG)){
            switch(this.playerPosition){
                case 'FWD':
                    fsXPts = ((fsXA * 3) + (fsXG * 4)).toFixed(2);
                    fsNPXPts = ((fsXA * 3) + (fsNPXG * 4)).toFixed(2);
                    break;
                case 'MID':
                    fsXPts = ((fsXA * 3) + (fsXG * 5)).toFixed(2);
                    fsNPXPts = ((fsXA * 3) + (fsNPXG * 5)).toFixed(2);
                    break;
                case 'DEF':
                    fsXPts = ((fsXA * 3) + (fsXG * 6)).toFixed(2);
                    fsNPXPts = ((fsXA * 3) + (fsNPXG * 6)).toFixed(2);
                    break;
            }
        }
    
        this.excelPlayerValues.set('Apps', playerAppearances);
        this.excelPlayerValues.set('As', Number(ffsPlayerDataValues.get('Assists - Opta')));
        this.excelPlayerValues.set('Base BPS', Number(ffsPlayerDataValues.get('Baseline BPS')));
        this.excelPlayerValues.set('BC Created', Number(ffsPlayerDataValues.get('Big Chances Created')));
        this.excelPlayerValues.set('BC Miss', Number(ffsPlayerDataValues.get('Big Chances Missed')));
        this.excelPlayerValues.set('BC', Number(ffsPlayerDataValues.get('Big Chances Total')));
        this.excelPlayerValues.set('Def Con Pts', Number(ffsPlayerDataValues.get('Defensive Contribution Points')));
        this.excelPlayerValues.set('fsNPXG', fsNPXG);
        this.excelPlayerValues.set('fsNPXPts', fsNPXPts);
        this.excelPlayerValues.set('fsXA', fsXA);
        this.excelPlayerValues.set('fsXG', fsXG);
        this.excelPlayerValues.set('fsXPts', fsXPts);
        this.excelPlayerValues.set('GIB', Number(ffsPlayerDataValues.get('Goals From Inside Box')));
        this.excelPlayerValues.set('Goal Inv',  this.createFormula(this.excelPlayerValues.get('FPL Goal Involvement'),100, 2, true, 'divide'));
        this.excelPlayerValues.set('Gs', Number(ffsPlayerDataValues.get('Goals')));
        this.excelPlayerValues.set('Headers', Number(ffsPlayerDataValues.get('Headed Goal Attempts')));
        this.excelPlayerValues.set('ICT Index', Number(ffsPlayerDataValues.get('ICT Index')));
        this.excelPlayerValues.set('KP', Number(ffsPlayerDataValues.get('Chances Created')));
        this.excelPlayerValues.set('Mins', Number(ffsPlayerDataValues.get('Time Played - FPL')));
        this.excelPlayerValues.set('Name', this.playerName);
        this.excelPlayerValues.set('PenTchs', Number(ffsPlayerDataValues.get('Touches - Penalty Area')));
        this.excelPlayerValues.set('PK Gs', Number(ffsPlayerDataValues.get('Goals From Penalties')));
        this.excelPlayerValues.set('PK Miss', Number(ffsPlayerDataValues.get('Penalties Missed')));
        this.excelPlayerValues.set('Shots', Number(playerShots));
        this.excelPlayerValues.set('SIB', Number(ffsPlayerDataValues.get('Shots - Inside Box')));
        this.excelPlayerValues.set('SOT', Number(ffsPlayerDataValues.get('Shots On Target')));
        this.excelPlayerValues.set('Team', this.playerTeam);
    
        const fplPlayerObject = ffsPlayerData.fplPlayerObject;

        if(!fplPlayerObject){
            throw new Error('Missing fplPlayerObject for ' + ffsPlayerData.playerName);
        }

        this.excelPlayerValues.set('BPS', fplPlayerObject.bpsPts);
        this.excelPlayerValues.set('BPS in Starts', fplPlayerObject.bpsDuringStarts);
        this.excelPlayerValues.set('CS', fplPlayerObject.cleanSheets);
        this.excelPlayerValues.set('Def Con', fplPlayerObject.defensive_contribution);
        this.excelPlayerValues.set('DC in Starts', fplPlayerObject.defConDuringStarts);
        this.excelPlayerValues.set('Total As', fplPlayerObject.fplAssists);
        this.excelPlayerValues.set('Bonus', fplPlayerObject.fplBonus);
        this.excelPlayerValues.set('Pts', fplPlayerObject.fplPts);
        this.excelPlayerValues.set('Price', Number.parseInt(fplPlayerObject.nowCost) / 10.0);
        this.excelPlayerValues.set('Starts', fplPlayerObject.starts);

        // const underStatPlayerObject = ffsPlayerData.uStatPlayerDataObject;

        // this.excelPlayerValues.set('uXG/90', Math.round(underStatPlayerObject.playerXG / timePlayed * 90.0 * 100) / 100);
        // this.excelPlayerValues.set('uNPXG/90', Math.round(underStatPlayerObject.playerNPXG / timePlayed * 90.0 * 100) / 100);
        // this.excelPlayerValues.set('uXA/90', Math.round(underStatPlayerObject.playerXA / timePlayed * 90.0 * 100) / 100);

        if(isNaN(Number(this.excelPlayerValues.get('PK Gs'))) || isNaN(Number(this.excelPlayerValues.get('PK Miss')))){
            this.excelPlayerValues.set('NP Shots', this.createFormula(playerShots, 0, 0, false, 'subtract'));
        }
        else{
            this.excelPlayerValues.set('NP Shots', this.createFormula(playerShots, Number(this.excelPlayerValues.get('PK Gs')) + Number(this.excelPlayerValues.get('PK Miss')), 0, false, 'subtract'));
        }

        if(isNaN(Number(this.excelPlayerValues.get('Gs'))) || isNaN(Number(this.excelPlayerValues.get('PK Gs')))){
            this.excelPlayerValues.set('Shot Conv %', this.createFormula(0, 0, 2, true, 'divide'));
        }
        else{
            this.excelPlayerValues.set('Shot Conv %', this.createFormula(Number(this.excelPlayerValues.get('Gs')) - Number(this.excelPlayerValues.get('PK Gs')), this.excelPlayerValues.get('NP Shots'), 2, true, 'divide'));
        }

        this.excelPlayerValues.set('As %', this.createFormula(this.excelPlayerValues.get('As'), this.excelPlayerValues.get('KP'), 2, true, 'divide'));
        this.excelPlayerValues.set('As / App', this.createFormula(this.excelPlayerValues.get('As'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('Base BPS / App', this.createFormula(this.excelPlayerValues.get('Base BPS'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('BC / App', this.createFormula(this.excelPlayerValues.get('BC'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('BC Conv %', this.createFormula(Number(this.excelPlayerValues.get('BC')) - Number(this.excelPlayerValues.get('BC Miss')), this.excelPlayerValues.get('BC'), 2, true, 'divide'));
        this.excelPlayerValues.set('BC Scored', this.createFormula(this.excelPlayerValues.get('BC'), this.excelPlayerValues.get('BC Miss'), 0, false, 'subtract'));
        this.excelPlayerValues.set('BCC / App', this.createFormula(this.excelPlayerValues.get('BC Created'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('Bonus / App', this.createFormula(this.excelPlayerValues.get('Bonus'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('BPS / Start', this.createFormula(this.excelPlayerValues.get('BPS in Starts'), this.excelPlayerValues.get('Starts'), 1, false, 'divide'));
        this.excelPlayerValues.set('DC / Start', this.createFormula(this.excelPlayerValues.get('DC in Starts'), this.excelPlayerValues.get('Starts'), 1, false, 'divide'));
        this.excelPlayerValues.set('DC Pts / Start', this.createFormula(this.excelPlayerValues.get('Def Con Pts'), this.excelPlayerValues.get('Starts'), 1, false, 'divide'));
        this.excelPlayerValues.set('Total As / App', this.createFormula(this.excelPlayerValues.get('Total As'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('fsNPXG / App', this.createFormula(this.excelPlayerValues.get('fsNPXG'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('fsNPXPts / App', this.createFormula(fsNPXPts, playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('fsXA / App', this.createFormula(this.excelPlayerValues.get('fsXA'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('fsXG / App', this.createFormula(this.excelPlayerValues.get('fsXG'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('fsXPts / App', this.createFormula(fsXPts, playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('fsNPXPts / App', this.createFormula(fsNPXPts, playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('Gs / App', this.createFormula(this.excelPlayerValues.get('Gs'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('GIB %', this.createFormula(this.excelPlayerValues.get('GIB'), this.excelPlayerValues.get('Gs'), 2, true, 'divide'));
        this.excelPlayerValues.set('Header %', this.createFormula(this.excelPlayerValues.get('Headers'), playerShots, 2, true, 'divide'));
        this.excelPlayerValues.set('ICT Index / App', this.createFormula(this.excelPlayerValues.get('ICT Index'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('KP / App', this.createFormula(this.excelPlayerValues.get('KP'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('Mins / App', this.createFormula(this.excelPlayerValues.get('Mins'), playerAppearances, 0, false, 'divide'));
        this.excelPlayerValues.set('NP Shots / App', this.createFormula(this.excelPlayerValues.get('NP Shots'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('PnTchs / App', this.createFormula(this.excelPlayerValues.get('PenTchs'), playerAppearances, 1, false, 'divide'));
        this.excelPlayerValues.set('Pts / App', this.createFormula(this.excelPlayerValues.get('Pts'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('Shot Acc %', this.createFormula(this.excelPlayerValues.get('SOT'), playerShots, 2, true, 'divide'));
        this.excelPlayerValues.set('Shots / App', this.createFormula(playerShots, playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('SIB / App', this.createFormula(this.excelPlayerValues.get('SIB'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('SOT / App', this.createFormula(this.excelPlayerValues.get('SOT'), playerAppearances, 2, false, 'divide'));
        this.excelPlayerValues.set('SIB %', this.createFormula(this.excelPlayerValues.get('SIB'), playerShots, 2, true, 'divide'));
    }

    createFormula(left, right, precision = 2, isPercentage, operation = 'divide'){
        let retVal = 0.00;

        if(left !== null && left !== undefined && right !== null && right !== undefined){
            let leftNumber = Number(left);
            let rightNumber = Number(right);

            if(!isNaN(leftNumber) && !isNaN(rightNumber)){
                if(operation === 'divide' && rightNumber !== 0){
                    retVal = leftNumber / rightNumber;
                }
                else if(operation === 'subtract'){
                    retVal = leftNumber - rightNumber;
                }
            }
        }

        return retVal;
    }

    outputPlayerExcel(){
        let excelObjectOutputMap = fplUtils.excelOrdering;
        let outputArray = new Array(excelObjectOutputMap.size);

        excelObjectOutputMap.forEach((excelCellObject, key) => {
            let value = this.excelPlayerValues.get(key);

            if(value === null || value === undefined){
                console.error('Missing value for ' + key + ' for player ' + this.playerName);
            }
            else{
                let position = excelCellObject.position;
                excelCellObject.cellValue = value;
                excelCellObject.cellKey = key;
                outputArray[position] = excelCellObject;
            }
        });

        return outputArray;
    }

    toString(){
        let retVal = '';

        for(const [key, value] of this.excelPlayerValues){
            if (!!retVal){
                retVal += ',';
            }
            retVal += key + ' : ' + value;
        }

        return retVal;
    }
}