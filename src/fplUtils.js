'use strict';
const textDecoder = require('./textDecode.js');
const fs = require('fs');
const propertiesReader = require('properties-reader');
const properties = propertiesReader('/Users/Corey/workspace/FPLAnalysis/src/fplProps.properties');
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

module.exports.buildNameMap = buildNameMap;
module.exports.buildArgsMap = buildArgsMap;
module.exports.removeSpecialChars = removeSpecialChars;
module.exports.getAllProperties = getAllProperties;
module.exports.getProp = getProp;