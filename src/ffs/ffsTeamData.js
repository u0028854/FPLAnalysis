/**
 * @description     Extracts FFS player data from stored HTML pages on disk
 */
'use strict';

import { parse } from 'node-html-parser';
import fs from 'fs';
import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';
import FFSTeamDataObject from '/Users/cdbak/workspace/FPLAnalysis/src/ffs/FFSTeamDataObject.js';
import { argv } from 'process';

const TEAM_OUTPUT_FILE = await fplUtils.getProp('teamCSVOutputFile');
const SIXWEEK_TEAM_OUTPUT_FILE = await fplUtils.getProp('teamSixWeekCSVOutputFile');
const TEAM_INPUT_FILE = await fplUtils.getProp('teamCSVInputFile');
const SIXWEEK_TEAM_INPUT_FILE = await fplUtils.getProp('teamSixWeekCSVInputFile');

async function main(){
    let sixWeekTimeFrame;

    if(argv.length === 3 && argv[2] === '6weeks'){
        sixWeekTimeFrame = argv[2];
    }

    let inputFileName = sixWeekTimeFrame ? SIXWEEK_TEAM_INPUT_FILE : TEAM_INPUT_FILE;
    let outputFileName = sixWeekTimeFrame ? SIXWEEK_TEAM_OUTPUT_FILE : TEAM_OUTPUT_FILE;

    const statsTable = parse(fs.readFileSync(inputFileName, 'utf-8')).querySelectorAll(".stats.draggable.fixed1")[0];
    const statsTableBody = statsTable.children.filter(child => child.rawTagName === "tbody")[0];
    let csvOutput = fplUtils.defineExcelTeamColumns() + '\n';

    statsTableBody.children.forEach(row => {
        let rowChildren = row.children;
        let teamName = rowChildren[0].getAttribute('title').trim();
        let teamObject = new FFSTeamDataObject(teamName);
        
        for(let i = 1; i < rowChildren.length; i++){
            let keyValPair = rowChildren[i].getAttribute('title').trim();

            if(keyValPair){
                let [key, value] = keyValPair.split(':').map(s => s.trim());
                teamObject.addTeamValue(key, Number(value));
            }
        }

        csvOutput += teamObject.outputTeamData();
    });

    fs.writeFileSync(outputFileName, csvOutput, (err) => {
        if (err) console.log(err);
    });
}

main();