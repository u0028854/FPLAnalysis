let timeCheck = Date.now();
let xShotArray = [0.04,0.04,0.02,0.05,0.06,0.07,0.07,0.13,0.12,0.07,0.11,0.13,0.14,0.35,0.45,0.61,0.49,0.47,0.1,0.1,0.1,0.1,0.1,];
//let xKPArray = [0.15,0.13,0.11,0.02,0.04,0.03,0.03,0.01,0.04,0.01,0.02,0.04,0.06,0.01,0.03,0.02,0.02];

// for (let i = 0; i < xShotArray.length; i++){
//     xShotArray[i] = xShotArray[i] * 1.5;
// }

// for (let i = 0; i < xKPArray.length; i++){
//     xKPArray[i] = xKPArray[i] * 1.5;
// }

// xShotArray = [];

let csOdds = (1 - 1/100.0);
let xG = 1/100.0;

for (let i = 1; i < 99; i++){
    csOdds = csOdds * (1 - ((i + 1) / 100.0));
    xG += ((i + 1) / 100.0);
}

console.log(csOdds * 100);
console.log(xG);


let outcomeMap = new Array();

// function createBooleanArray(elements){
//     let retVal = new Array();
//     let elementCombos = Math.pow(2, elements);
//     let arrayElementSize = (elementCombos - 1).toString(2).length;

//     for(let i = 0; i < elementCombos; i++){
//         let tempArrayVal = i.toString(2);
//         for (let j = tempArrayVal.length; j < arrayElementSize; j++){
//             tempArrayVal = '0' + tempArrayVal;
//         }

//        retVal.push(tempArrayVal);
//     }

//     return retVal;
// }

// let currentArray = xShotArray;
// let outcomeArray = createBooleanArray(currentArray.length);
// let percentageArray = new Array(currentArray.length + 1);
// percentageArray.fill(0);

// for (let i = 0; i < outcomeArray.length; i++){
//     let eventsOccurred = (outcomeArray[i].match(/1/g)||[]).length;
//     let newProbability = 0;
    
//     for(let j = 0; j < outcomeArray[i].length; j++){
//         if(outcomeArray[i][j] === '1'){
//             newProbability = ((newProbability === 0) ? currentArray[j] : (newProbability *  currentArray[j]));
//         }
//         else{
//             newProbability = ((newProbability === 0) ? (1 - currentArray[j]) : (newProbability *  (1 - currentArray[j])));
//         }
//     }

//     percentageArray[eventsOccurred] = percentageArray[eventsOccurred] + newProbability;
// }

// for (let i = 0; i < percentageArray.length; i++){
//     console.log(i + ':' + percentageArray[i])
// }

console.log(Date.now() - timeCheck);