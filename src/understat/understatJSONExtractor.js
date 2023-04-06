async function convertStrings(data){
    let retVal = data;
    if(data.indexOf('HEXCODE-') !== -1){
        let startIndex = data.indexOf('HEXCODE-');
        var re = new RegExp(data.substring(startIndex, startIndex + 10),"g");
        retVal = await convertStrings(data.replace(re, await convertHexCodeToString(data.substring(startIndex + 8, startIndex + 10))));
    }

    return retVal;
}

async function convertHexCodeToString(hexCode){
    return String.fromCharCode(parseInt(hexCode, 16));
}

async function extractJSON(jsonVar, htmlData, varDeclaration){
    let retVal;

    if(jsonVar && htmlData){
        const varStartText = (varDeclaration ? 'var ' : '') + jsonVar;
        const jsonParseStartText = 'JSON.parse(\'';
        const jsonParseEndText = '\')';

        htmlData = await convertStrings(htmlData.replace(new RegExp('\\\\x',"g"), 'HEXCODE-'));

        let varStart = htmlData.indexOf(varStartText);

        if(varStart === -1){
            return retVal;
        }

        let jsonParseStart = htmlData.indexOf(jsonParseStartText, varStart + varStartText.length);
        
        if(jsonParseStart === -1){
            return retVal;
        }

        let jsonParseEnd = htmlData.indexOf(jsonParseEndText, jsonParseStart + jsonParseStartText.length);
        
        if(jsonParseEnd === -1){
            return retVal;
        }

        let jsonString = htmlData.substring(jsonParseStart + jsonParseStartText.length, jsonParseEnd);

        if(jsonString === -1){
            return retVal;
        }

        retVal = JSON.parse(jsonString);
    }

    return retVal;
}

module.exports = extractJSON;