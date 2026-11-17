import * as fplUtils from '../fplUtils.js';

export default class FPLPlayerObject {
    constructor(name, nowCost) {
        this.playerName = fplUtils.removeSpecialChars(name);
        this.fplPts = 0;
        this.fplBonus = 0;
        this.bpsPts = 0;
        this.bpsDuringStarts = 0
        this.cleanSheets = 0;
        this.fplAssists = 0;
        this.nowCost = nowCost;
        this.ownGoals = 0;
        this.defensive_contribution = 0;
        this.defConDuringStarts = 0
        this.starts = 0;
    }

    add(fplPts, fplBonus, bpsPts, cleanSheets, fplAssists, ownGoals, defensiveContributions, matchStarts){
        this.addPts(fplPts);
        this.addBonus(fplBonus);
        this.addBPS(bpsPts);
        this.addCS(cleanSheets);
        this.addAssist(fplAssists);
        this.addOwnGoal(ownGoals);
        this.addDefCon(defensiveContributions);
        this.addStarts(matchStarts);

        if(matchStarts > 0){
            this.addStartBPS(bpsPts);
            this.addStartsDefCon(defensiveContributions);
        }
    }

    addPts(fplPts){
        this.fplPts += fplPts;
    }

    addBonus(fplBonus){
        this.fplBonus += fplBonus;
    }

    addBPS(bpsPts){
        this.bpsPts += bpsPts;
    }

    addStartBPS(bpsPts){
        this.bpsDuringStarts += bpsPts;
    }

    addCS(cleanSheets){
        this.cleanSheets += cleanSheets;
    }

    addAssist(fplAssists){
        this.fplAssists += fplAssists;
    }

    addOwnGoal(ownGoals){
        this.ownGoals += ownGoals;
    }

    addDefCon(defensiveContributions){
            this.defensive_contribution += defensiveContributions;
    }

    addStartsDefCon(defensiveContributions){
            this.defConDuringStarts += defensiveContributions;
    }

    addStarts(matchStarts){
            this.starts += matchStarts;
    }

    blankPlayer(){
        if(this.fplPts == 0 &&
        this.fplBonus == 0 &&
        this.bpsPts == 0 &&
        this.bpsDuringStarts == 0 &&
        this.cleanSheets == 0 &&
        this.fplAssists == 0 &&
        this.ownGoals == 0 &&
        this.defensive_contribution == 0 &&
        this.defConDuringStarts &&
        this.starts == 0){
            return true;
        }
        else{
            return false;
        }
    }

    outputJSON(){
        return '{"playerName": "' + this.playerName
        + '", "fplPts": ' + this.fplPts
        + ', "fplBonus": ' + this.fplBonus
        + ', "bpsPts": ' + this.bpsPts
        + ', "bpsDuringStarts": ' + this.bpsDuringStarts
        + ', "cleanSheets": ' + this.cleanSheets
        + ', "fplAssists": ' + this.fplAssists
        + ', "nowCost": ' + this.nowCost
        + ', "ownGoals": ' + this.ownGoals
        + ', "defContributions": ' + this.defensive_contribution
        + ', "defConDuringStarts": ' + this.defConDuringStarts
        + ', "matchStarts": ' + this.starts
        + '}';
    }
}