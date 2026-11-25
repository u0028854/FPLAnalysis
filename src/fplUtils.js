'use strict';
import * as textDecoder from './textDecode.js';
import * as fs from 'fs';
import { propertiesReader } from 'properties-reader';
const properties = propertiesReader({ sourceFile: '/Users/cdbak/workspace/FPLAnalysis/src/fplProps.properties' });

let propertiesMap = new Map();
let initCount = 0;

function buildArgsMap(args){
    let retVal = new Map();

    for(let x = 0; x < args.length; x++){
        if(x > 1){
            let splitArg = args[x].split('=');
            if(splitArg.length === 2){
                retVal.set(splitArg[0].trim(), splitArg[1].trim());
            }
        }
    }
    
    return retVal;
}

function buildNameMap(nameMapFile, valuesColumn, keyColumn){
    let retVal = new Map();

    try{
        let nameMapArray = fs.readFileSync(nameMapFile, 'utf-8').split('\n');
        
        for(let x = 0; x < nameMapArray.length; x++){
            let nameMapLine = nameMapArray[x].split(',');
            if(nameMapLine[valuesColumn] && nameMapLine[keyColumn]){
                retVal.set(nameMapLine[keyColumn].trim(), nameMapLine[valuesColumn].trim());
            }
        }
    }
    catch (error){
        if(error.code === 'ENOENT'){
            console.log('Player name map file not found.');
        }
        else{
            console.log(error);
        }
    }

    return retVal;
}

function removeSpecialChars(strVar){
    if(strVar.includes('&#039;')){
        strVar = strVar.replace('&#039;', '\'');
    }

    return textDecoder.latinise(strVar);
}

async function getAllProperties(){
    if(!propertiesMap || propertiesMap.size === 0){
        initializePropertiesMap();
    }
    
    return propertiesMap;
}

async function getProp(propertyName){
    if(!propertiesMap || propertiesMap.size === 0){
        initializePropertiesMap();
    }

    try{
        let retVal = propertiesMap.get(propertyName);
        return retVal.trim();
    }
    catch (error){
        throw new Error(propertyName + ' not found in properiesMap file');
    }
}

async function initializePropertiesMap(){
    console.log('Initializing properties: ' + ++initCount);
    if(!propertiesMap){
        propertiesMap = new Map();
    }

    properties.each((key, value) => {
        propertiesMap.set(key, value);
    });
}

function translateUTFCode(strVar){
   return(strVar.replace(/\\xc3\\x80/g, 'A')
                .replace(/\\xc3\\x81/g, 'A')
                .replace(/\\xc3\\x82/g, 'A')
                .replace(/\\xc3\\x83/g, 'A')
                .replace(/\\xc3\\x84/g, 'A')
                .replace(/\\xc3\\x85/g, 'A')
                .replace(/\\xc3\\x86/g, 'A')
                .replace(/\\xc3\\x87/g, 'C')
                .replace(/\\xc3\\x88/g, 'E')
                .replace(/\\xc3\\x89/g, 'E')
                .replace(/\\xc3\\x8a/g, 'E')
                .replace(/\\xc3\\x8b/g, 'E')
                .replace(/\\xc3\\x8c/g, 'I')
                .replace(/\\xc3\\x8d/g, 'I')
                .replace(/\\xc3\\x8e/g, 'I')
                .replace(/\\xc3\\x8f/g, 'I')
                .replace(/\\xc3\\x92/g, 'O')
                .replace(/\\xc3\\x93/g, 'O')
                .replace(/\\xc3\\x94/g, 'O')
                .replace(/\\xc3\\x95/g, 'O')
                .replace(/\\xc3\\x96/g, 'O')
                .replace(/\\xc3\\x98/g, 'O')
                .replace(/\\xc3\\x99/g, 'U')
                .replace(/\\xc3\\x9a/g, 'U')
                .replace(/\\xc3\\x9b/g, 'U')
                .replace(/\\xc3\\x9c/g, 'U')
                .replace(/\\xc3\\x9d/g, 'Y')
                .replace(/\\xc3\\xa0/g, 'a')
                .replace(/\\xc3\\xa1/g, 'a')
                .replace(/\\xc3\\xa2/g, 'a')
                .replace(/\\xc3\\xa3/g, 'a')
                .replace(/\\xc3\\xa4/g, 'a')
                .replace(/\\xc3\\xa5/g, 'a')
                .replace(/\\xc3\\xa6/g, 'a')
                .replace(/\\xc3\\xa7/g, 'c')
                .replace(/\\xc3\\xa8/g, 'e')
                .replace(/\\xc3\\xa9/g, 'e')
                .replace(/\\xc3\\xaa/g, 'e')
                .replace(/\\xc3\\xab/g, 'e')
                .replace(/\\xc3\\xac/g, 'i')
                .replace(/\\xc3\\xad/g, 'i')
                .replace(/\\xc3\\xae/g, 'i')
                .replace(/\\xc3\\xaf/g, 'i')
                .replace(/\\xc3\\xb1/g, 'n')
                .replace(/\\xc3\\xb2/g, 'o')
                .replace(/\\xc3\\xb3/g, 'o')
                .replace(/\\xc3\\xb4/g, 'o')
                .replace(/\\xc3\\xb5/g, 'o')
                .replace(/\\xc3\\xb6/g, 'o')
                .replace(/\\xc3\\xb8/g, 'o')
                .replace(/\\xc3\\xb9/g, 'u')
                .replace(/\\xc3\\xba/g, 'u')
                .replace(/\\xc3\\xbb/g, 'u')
                .replace(/\\xc3\\xbc/g, 'u')
                .replace(/\\xc3\\xbd/g, 'y')
                .replace(/\\xc3\\xbf/g, 'y')
                .replace(/\\xc4\\x80/g, 'A')
                .replace(/\\xc4\\x81/g, 'a')
                .replace(/\\xc4\\x82/g, 'A')
                .replace(/\\xc4\\x83/g, 'a')
                .replace(/\\xc4\\x84/g, 'A')
                .replace(/\\xc4\\x85/g, 'a')
                .replace(/\\xc4\\x86/g, 'C')
                .replace(/\\xc4\\x87/g, 'c')
                .replace(/\\xc4\\x88/g, 'C')
                .replace(/\\xc4\\x89/g, 'c')
                .replace(/\\xc4\\x8a/g, 'C')
                .replace(/\\xc4\\x8b/g, 'c')
                .replace(/\\xc4\\x8c/g, 'C')
                .replace(/\\xc4\\x8d/g, 'c')
                .replace(/\\xc4\\x8e/g, 'D')
                .replace(/\\xc4\\x8f/g, 'd')
                .replace(/\\xc4\\x90/g, 'D')
                .replace(/\\xc4\\x91/g, 'd')
                .replace(/\\xc4\\x92/g, 'E')
                .replace(/\\xc4\\x93/g, 'E')
                .replace(/\\xc4\\x94/g, 'E')
                .replace(/\\xc4\\x95/g, 'E')
                .replace(/\\xc4\\x96/g, 'E')
                .replace(/\\xc4\\x97/g, 'E')
                .replace(/\\xc4\\x98/g, 'E')
                .replace(/\\xc4\\x99/g, 'E')
                .replace(/\\xc4\\x9a/g, 'E')
                .replace(/\\xc4\\x9b/g, 'E')
                .replace(/\\xc4\\x9c/g, 'G')
                .replace(/\\xc4\\x9d/g, 'g')
                .replace(/\\xc4\\x9e/g, 'G')
                .replace(/\\xc4\\x9f/g, 'g')
                .replace(/\\xc4\\xa0/g, 'G')
                .replace(/\\xc4\\xa1/g, 'g')
                .replace(/\\xc4\\xa2/g, 'G')
                .replace(/\\xc4\\xa3/g, 'g')
                .replace(/\\xc4\\xa4/g, 'H')
                .replace(/\\xc4\\xa5/g, 'h')
                .replace(/\\xc4\\xa6/g, 'H')
                .replace(/\\xc4\\xa7/g, 'h')
                .replace(/\\xc4\\xa8/g, 'I')
                .replace(/\\xc4\\xa9/g, 'i')
                .replace(/\\xc4\\xaa/g, 'I')
                .replace(/\\xc4\\xab/g, 'i')
                .replace(/\\xc4\\xac/g, 'I')
                .replace(/\\xc4\\xad/g, 'i')
                .replace(/\\xc4\\xae/g, 'I')
                .replace(/\\xc4\\xaf/g, 'i')
                .replace(/\\xc4\\xb0/g, 'I')
                .replace(/\\xc4\\xb1/g, 'i')
                .replace(/\\xc4\\xb4/g, 'J')
                .replace(/\\xc4\\xb5/g, 'j')
                .replace(/\\xc4\\xb9/g, 'L')
                .replace(/\\xc4\\xba/g, 'l')
                .replace(/\\xc4\\xbb/g, 'L')
                .replace(/\\xc4\\xbc/g, 'l')
                .replace(/\\xc4\\xbd/g, 'L')
                .replace(/\\xc4\\xbe/g, 'l')
                .replace(/\\xc4\\xbf/g, 'L')
                .replace(/\\xc5\\x80/g, 'l')
                .replace(/\\xc5\\x81/g, 'L')
                .replace(/\\xc5\\x82/g, 'l')
                .replace(/\\xc5\\x83/g, 'N')
                .replace(/\\xc5\\x84/g, 'n')
                .replace(/\\xc5\\x85/g, 'N')
                .replace(/\\xc5\\x86/g, 'n')
                .replace(/\\xc5\\x87/g, 'N')
                .replace(/\\xc5\\x88/g, 'n')
                .replace(/\\xc5\\x8a/g, 'N')
                .replace(/\\xc5\\x8b/g, 'n')
                .replace(/\\xc5\\x8c/g, 'O')
                .replace(/\\xc5\\x8d/g, 'o')
                .replace(/\\xc5\\x8e/g, 'O')
                .replace(/\\xc5\\x8f/g, 'o')
                .replace(/\\xc5\\x90/g, 'O')
                .replace(/\\xc5\\x91/g, 'o')
                .replace(/\\xc5\\x9a/g, 'S')
                .replace(/\\xc5\\x9b/g, 's')
                .replace(/\\xc5\\x9c/g, 'S')
                .replace(/\\xc5\\x9d/g, 's')
                .replace(/\\xc5\\x9e/g, 'S')
                .replace(/\\xc5\\x9f/g, 's')
                .replace(/\\xc5\\xa0/g, 'S')
                .replace(/\\xc5\\xa1/g, 's')
                .replace(/\\xc5\\xa8/g, 'U')
                .replace(/\\xc5\\xa9/g, 'u')
                .replace(/\\xc5\\xaa/g, 'U')
                .replace(/\\xc5\\xab/g, 'u')
                .replace(/\\xc5\\xac/g, 'U')
                .replace(/\\xc5\\xad/g, 'u')
                .replace(/\\xc5\\xae/g, 'U')
                .replace(/\\xc5\\xaf/g, 'u')
                .replace(/\\xc5\\xb0/g, 'U')
                .replace(/\\xc5\\xb1/g, 'u')
                .replace(/\\xc5\\xb2/g, 'U')
                .replace(/\\xc5\\xb3/g, 'u')
                .replace(/\\xc5\\xb9/g, 'Z')
                .replace(/\\xc5\\xba/g, 'z')
                .replace(/\\xc5\\xbb/g, 'Z')
                .replace(/\\xc5\\xbc/g, 'z')
                .replace(/\\xc5\\xbd/g, 'Z')
                .replace(/\\xc5\\xbe/g, 'z')
                .replace(/\\'/g, '\'')
                .replace(/ß/g, 'ss')
                );
}

function translateHTMLCode(strVar){
   return(strVar.replace(/\\u00ed/g, 'i')
                .replace(/\\u00e9/g, 'e')
                .replace(/\\u00eb/g, 'e')
                .replace(/\\u00e3/g, 'a')
                .replace(/\\u00f1/g, 'n')
                .replace(/\\u00f3/g, 'o')
                .replace(/\\u00e1/g, 'a')
                .replace(/\\u2019/g, '\'')
                .replace(/\\u00ee/g, 'i')
                .replace(/\\u00fa/g, 'u')
                .replace(/\\u0161/g, 's')
                .replace(/\\u00f8/g, 'o')
                .replace(/\\u00e4/g, 'a')
                .replace(/\\u00e7/g, 'c')
                .replace(/\\u00c1/g, 'A')
                .replace(/\\u00fc/g, 'u')
                .replace(/\\u00e6/g, 'a')
                );
}

function defineExcelOrdering(){
    let retVal = new Map();
    let coreBlue = '002060';
    let formulaRed = '8B0606';
    let expectedGreen = '088a47';
    let nameWidth = 25;
    let stdWidth = 9.5;

    retVal.set('Name', { 'position': 0, 'type': 'Text', 'color': coreBlue, 'width': nameWidth, 'alignment': 'left', 'freeze': true});
    retVal.set('Team', { 'position': 1, 'type': 'Text', 'color': coreBlue, 'width': stdWidth, 'freeze': true});
    retVal.set('Price', { 'position': 2, 'type': 'Number', 'precision': 1, 'color': coreBlue, 'width': stdWidth, 'freeze': true});
    retVal.set('Apps', { 'position': 3, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Starts', { 'position': 4, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Mins', { 'position': 5, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Pts', { 'position': 6, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Bonus', { 'position': 7, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BPS', { 'position': 8, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BPS in Starts', { 'position': 9, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Base BPS', { 'position': 10, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Gs', { 'position': 11, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('GIB', { 'position': 14, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('GIB %', { 'position': 15, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Shots', { 'position': 16, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('NP Shots', { 'position': 17, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('SIB', { 'position': 18, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('SOT', { 'position': 19, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Headers', { 'position': 20, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Shot Conv %', { 'position': 21, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('SIB %', { 'position': 22, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Shot Acc %', { 'position': 23, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Header %', { 'position': 24, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BC', { 'position': 25, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BC Scored', { 'position': 26, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BC Miss', { 'position': 27, 'type': 'Number', 'precision': 0, 'color' :coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BC Conv %', { 'position':28, 'type':'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('As', { 'position': 29, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Total As', { 'position': 30, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('KP', { 'position': 31, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('BC Created', { 'position': 32, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('As %', { 'position': 33, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('ICT Index', { 'position': 34, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Goal Inv', { 'position': 35, 'type': 'Percentage', 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('PenTchs', { 'position': 36, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Def Con', { 'position': 37, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Def Con Pts', { 'position': 38, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('DC in Starts', { 'position': 39, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('CS', { 'position': 40, 'type': 'Number', 'precision': 0, 'color': coreBlue, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('fsXG', { 'position': 41, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('fsNPXG', { 'position': 42, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('fsXA', { 'position': 43, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('fsXPts', { 'position': 44, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('fsNPXPts', { 'position': 45, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': true, 'width': stdWidth, 'outlineLevel': 1, 'hidden': true});
    retVal.set('Mins / App', { 'position': 46, 'type': 'Number', 'precision': 0, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('Pts / App', { 'position': 47, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('Bonus / App', { 'position': 48, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('BPS / Start', { 'position': 49, 'type': 'Number', 'precision': 1, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('Base BPS / App', { 'position': 50, 'type': 'Number', 'precision': 1, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('Gs / App', { 'position': 51, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('Shots / App', { 'position': 52, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('NP Shots / App', { 'position': 53, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('SIB / App', { 'position': 54, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('SOT / App', { 'position': 55, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('BC / App', { 'position': 56, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('As / App', { 'position': 57, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('Total As / App', { 'position': 58, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('KP / App', { 'position': 59, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('BCC / App', { 'position': 60, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('ICT Index / App', { 'position': 61, 'type': 'Number', 'precision': 1, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('PnTchs / App', { 'position': 62, 'type': 'Number', 'precision': 1, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('DC / Start', { 'position': 63, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('DC Pts / Start', { 'position': 64, 'type': 'Number', 'precision': 2, 'color': formulaRed, 'grouped': false, 'width': stdWidth});
    retVal.set('fsXA / App', { 'position': 67, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': false, 'width': stdWidth});
    retVal.set('fsXG / App', { 'position': 68, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': false, 'width': stdWidth});
    retVal.set('fsXPts / App', { 'position': 69, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': false, 'width': stdWidth});
    retVal.set('fsNPXPts / App', { 'position': 70, 'type': 'Number', 'precision': 2, 'color': expectedGreen, 'grouped': false, 'width': stdWidth});

    return retVal;
}

function defineExcelTeamColumns(){
    const retVal = [
        'Team',
        'Big Chances Conceded',
        'Shots On Target Conceded',
        'Goals Conceded',
        'xG Conceded',
        'Big Chances Total',
        'Shots On Target',
        'Goals',
        'xG Expected Goals',
        'Games Played'
    ];

    return retVal; 
}

export { buildNameMap, buildArgsMap, removeSpecialChars, getAllProperties, getProp, translateUTFCode, translateHTMLCode, defineExcelOrdering, defineExcelTeamColumns };