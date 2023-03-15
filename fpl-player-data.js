'use strict';

const unirest = require('unirest');
const fs = require("fs");
const { argv } = require('process');
const textDecoder = require('./textDecode.js');

const FPL_PLAYER_FILEBASE = 'c:\\EPLJava\\stat_tables\\';
const FPL_PLAYER_FILESUFFIX = 'fpl.json';
const ERROR_FILESUFFIX = 'fplError.json';
const JSONFILESTART = '{"players":[';

function main(){
    let timeFrame = null;
    let outputFileData = JSONFILESTART;
    let errorData = new Array();
    let fileName = FPL_PLAYER_FILEBASE;

    if(argv.length === 3 && argv[2].includes('weeks')){
        try{
            timeFrame = parseInt(argv[2].replace('weeks',''));
            if(timeFrame < 1 || timeFrame > 38){
                timeFrame = null;
            }
            else{
                fileName += timeFrame + 'weeks_';
            }
        }
        catch(err){
            timeFrame = null;
            console.log(err);
        }
    }
    
    unirest.get('https://fantasy.premierleague.com/api/bootstrap-static/')
    .then((res) => {
        const fixtureDates = new Array();

        if(timeFrame != null){
            const fixtureList = res.body.events;
            
            const today = new Date();
            
            for(let x = 0; x < fixtureList.length; x++){
                let tempFixture = fixtureList[x];
                if(Math.floor(today.getTime()/1000) > tempFixture.deadline_time_epoch){
                    if(fixtureDates.length === timeFrame){
                        fixtureDates[0] = tempFixture.id;
                    }
                    else{
                        fixtureDates.push(tempFixture.id);
                    }
                    fixtureDates.sort(
                        (a, b) => {
                            if (a < b) return -1;
                            else if (a > b) return 1;
                            else return 0;
                        }
                    );
                }
            }
        }

        const fplJSON = JSON.parse(JSON.stringify(res.body)).elements;

        const activePlayers = new Map();

        for(let x = 0; x < fplJSON.length; x++){
            const tempPlayer = fplJSON[x];
            if(tempPlayer.minutes > 0){
                activePlayers.set(tempPlayer.first_name + ' ' + tempPlayer.second_name, [tempPlayer.id, tempPlayer.now_cost]);
            }
        }

        let itemsProcessed = 0;
        activePlayers.forEach((value,key)=>{
            let activePlayer = new FPL_Player(key, value[1]);
            try{
                let playerURL = 'https://fantasy.premierleague.com/api/element-summary/' + value[0] + '/';
                unirest.get(playerURL).then((result) => {
                    ++itemsProcessed;
                    if(JSON.stringify(result.body) && JSON.parse(JSON.stringify(result.body)).history){
                        let playerHistory = JSON.parse(JSON.stringify(result.body)).history;
                        for(let x = 0; x < playerHistory.length; x++){
                            let playerGameweek = playerHistory[x];
                            if(fixtureDates.length == 0 || fixtureDates.includes(playerGameweek.round)){
                                activePlayer.add(playerGameweek.total_points, playerGameweek.bonus, playerGameweek.bps, playerGameweek.clean_sheets, playerGameweek.assists, playerGameweek.own_goals);
                            }
                        }

                        if(outputFileData !== JSONFILESTART){
                            outputFileData += ',';
                        }
                        outputFileData += activePlayer.outputJSON();
                        
                        if(itemsProcessed === activePlayers.size) {
                            if(errorData.length > 0){
                                processErrors(errorData, outputFileData, fileName, fixtureDates);
                            }
                            else{
                                outputFile(fileName + FPL_PLAYER_FILESUFFIX, closePlayerJSON(outputFileData));
                            }
                        }
                    } else{
                        console.log('Error getting ' + playerURL);
                        errorData.push({url: playerURL, playerObject: activePlayer});

                        if(itemsProcessed === activePlayers.size) {
                            if(errorData.length > 0){
                                processErrors(errorData, outputFileData, fileName, fixtureDates);
                            }
                            else{
                                outputFile(fileName + FPL_PLAYER_FILESUFFIX, closePlayerJSON(outputFileData));
                            }
                        }
                    }
                });
            }
            catch(error){
                console.log(error);
            }
        });
    })
    .catch((error)=> {
        console.log(error);
    });
}



class FPL_Player {
    constructor(name, nowCost) {
      this.name = textDecoder.latinise(name);
      this.fplPts = 0;
      this.fplBonus = 0;
      this.bpsPts = 0;
      this.cleanSheets = 0;
      this.fplAssists = 0;
      this.nowCost = nowCost;
      this.ownGoals = 0;
    }

    add(fplPts, fplBonus, bpsPts, cleanSheets, fplAssists, ownGoals){
        this.addPts(fplPts);
        this.addBonus(fplBonus);
        this.addBPS(bpsPts);
        this.addCS(cleanSheets);
        this.addAssist(fplAssists);
        this.addOwnGoal(ownGoals);
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

    addCS(cleanSheets){
        this.cleanSheets += cleanSheets;
    }

    addAssist(fplAssists){
        this.fplAssists += fplAssists;
    }

    addOwnGoal(ownGoals){
        this.ownGoals += ownGoals;
    }

    blankPlayer(){
        if(this.fplPts == 0 &&
        this.fplBonus == 0 &&
        this.bpsPts == 0 &&
        this.cleanSheets == 0 &&
        this.fplAssists == 0 &&
        this.ownGoals == 0){
            return true;
        }
        else{
            return false;
        }
    }

    outputJSON(){
        return '{"name": "' + this.name
        + '", "fplPts": ' + this.fplPts
        + ', "fplBonus": ' + this.fplBonus
        + ', "bpsPts": ' + this.bpsPts
        + ', "cleanSheets": ' + this.cleanSheets
        + ', "fplAssists": ' + this.fplAssists
        + ', "nowCost": ' + this.nowCost
        + ', "ownGoals": ' + this.ownGoals
        + '}';
    }
}

function closePlayerJSON(jsonFileData) {
    return jsonFileData += ']}';
}

function outputFile(fileName, fileData){
    if(fileData && fileData.length > 0){
        fs.writeFileSync(fileName, fileData, (err) => {
            if (err) console.log(err);
        });
    }
}

function processErrors(errorData, outputFileData, fileName, fixtureDates){
    let errorFileData = '';
    let itemsProcessed = 0;
    errorData.forEach((errorObject)=> {
        let activePlayer = errorObject.playerObject;
        try{
            unirest.get(errorObject.url).then((result) => {
                ++itemsProcessed;
                if(activePlayer.blankPlayer() && JSON.stringify(result.body) && JSON.parse(JSON.stringify(result.body)).history){
                    let playerHistory = JSON.parse(JSON.stringify(result.body)).history;
                    for(let x = 0; x < playerHistory.length; x++){
                        let playerGameweek = playerHistory[x];
                        if(fixtureDates.length == 0 || fixtureDates.includes(playerGameweek.round)){
                            activePlayer.add(playerGameweek.total_points, playerGameweek.bonus, playerGameweek.bps, playerGameweek.clean_sheets, playerGameweek.assists, playerGameweek.own_goals);
                        }
                    }

                    if(outputFileData !== JSONFILESTART){
                        outputFileData += ',';
                    }
                    outputFileData += activePlayer.outputJSON();
                        
                    if(itemsProcessed === errorData.length) {
                        outputFile(fileName + FPL_PLAYER_FILESUFFIX, closePlayerJSON(outputFileData));
                        outputFile(fileName + ERROR_FILESUFFIX, errorFileData);
                    }
                } else{
                    console.log('Error getting ' + errorObject.url);
                    errorFileData += errorObject.url;

                    if(itemsProcessed === errorData.length) {
                        outputFile(fileName + FPL_PLAYER_FILESUFFIX, closePlayerJSON(outputFileData));
                        outputFile(fileName + ERROR_FILESUFFIX, errorFileData);
                    }
                }
            });
        }
        catch(error){
            console.log(error);
        }
    });
}

main();