/**
 * @description     Extracts fpl player data for gameweek period and creates new JSON structure to putput to external file
 * @argument        timeFrame   number of weeks in history to pull data from current date and gameweek
 */
'use strict';

import unirest from 'unirest';
import FPLPlayerObject from '/Users/cdbak/workspace/FPLAnalysis/src/fpl/FPLPlayerObject.js';
import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';

export async function getFPLPlayerData(gwRangeOption, fplFFSPlayerNameMap){
    let timeFrame = null;
    const playerDataMap = new Map();
    const fixtureDates = new Array();
    const activePlayers = new Map();

    if(gwRangeOption){
        try{
            timeFrame = parseInt(gwRangeOption.replace('weeks',''));
            if(timeFrame < 1 || timeFrame > 38){
                timeFrame = null;
            }
        }
        catch(err){
            timeFrame = null;
            console.log(err);
        }
    }
    
    const bootstrapResponse = await getBootstrapData();
    const fplJSON = JSON.parse(JSON.stringify(bootstrapResponse.body)).elements;
    
    if(timeFrame != null){
        const fixtureList = bootstrapResponse.body.events;
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

    for(let x = 0; x < fplJSON.length; x++){
        const tempPlayer = fplJSON[x];

        let playerName = fplUtils.removeSpecialChars(tempPlayer.first_name + ' ' + tempPlayer.second_name);

        if(fplFFSPlayerNameMap.has(playerName)){
            playerName = fplFFSPlayerNameMap.get(playerName);
        }

        if(tempPlayer.minutes > 0){
            activePlayers.set(playerName, [tempPlayer.id, tempPlayer.now_cost]);
        }
    }

    for(let [key, value] of activePlayers){
        let playerURL = 'https://fantasy.premierleague.com/api/element-summary/' + value[0] + '/';
        let activePlayer = await getIndividualFPLPlayerData(new FPLPlayerObject(key, value[1]), playerURL, fixtureDates);
        playerDataMap.set(activePlayer.playerName, activePlayer);
    }

    return playerDataMap;
}

function getIndividualFPLPlayerData(activePlayer, playerURL, fixtureDates){
    return new Promise((resolve) => {
        unirest.get(playerURL).end((response) => {
            if(response.error){
                console.error('Error getting ' + playerURL);
                resolve(activePlayer);
            }
            else{
                if(JSON.stringify(response.body) && JSON.parse(JSON.stringify(response.body)).history){
                    let playerHistory = JSON.parse(JSON.stringify(response.body)).history;
                    for(let x = 0; x < playerHistory.length; x++){
                        let playerGameweek = playerHistory[x];
                        if(fixtureDates.length == 0 || fixtureDates.includes(playerGameweek.round)){
                            activePlayer.add(
                                playerGameweek.total_points,
                                playerGameweek.bonus,
                                playerGameweek.bps,
                                playerGameweek.clean_sheets,
                                playerGameweek.assists,
                                playerGameweek.own_goals,
                                playerGameweek.defensive_contribution,
                                playerGameweek.starts
                            );
                        }
                    }
                }
                resolve(activePlayer);
            }
        });
    });
}

function getBootstrapData(){
    return new Promise((resolve) => {
        resolve(unirest.get('https://fantasy.premierleague.com/api/bootstrap-static/'));
    });
}