import ExcelJS from 'exceljs';
import * as fplUtils from '/Users/cdbak/workspace/FPLAnalysis/src/fplUtils.js';
import ExcelPlayerOutputObject from '/Users/cdbak/workspace/FPLAnalysis/src/excel/ExcelPlayerOutputObject.js';

const BLACK_RGB = '000000';
const WHITE_RGB = 'FFFFFF';
const DEFAULT_BORDER = {
  top: {style:'thin', color: {argb: BLACK_RGB}},
  left: {style:'thin', color: {argb: BLACK_RGB}},
  bottom: {style:'thin', color: {argb: BLACK_RGB}},
  right: {style:'thin', color: {argb: BLACK_RGB}}
};
const PLAYER_EXCEL_FILE = await fplUtils.getProp('playerExcelOutputFile');
const SIXGAME_PLAYER_EXCEL_FILE = await fplUtils.getProp('playerSixWeekExcelOutputFile');

export default async function buildPlayerExcel(playerMap, sixWeekTimeFrame){
    const fwdPlayerMap = playerMap.get('FWD');
    const midPlayerMap = playerMap.get('MID');
    const defPlayerMap = playerMap.get('DEF');

    if(!fwdPlayerMap || !midPlayerMap || !defPlayerMap){
        console.error('Player Map is missing a position map; !fwdPlayerMap: ' + !fwdPlayerMap + ', !midPlayerMap: ' + !midPlayerMap + ', !defPlayerMap: ' + !defPlayerMap);
        return;
    }

    const fwdExcelPlayerOutputObjects = new Map();

    fwdPlayerMap.forEach((value, key) => {
        let excelPlayerOutputObject = new ExcelPlayerOutputObject(value);
        fwdExcelPlayerOutputObjects.set(key, excelPlayerOutputObject);
    });

    const midExcelPlayerOutputObjects = new Map();

    midPlayerMap.forEach((value, key) => {
        let excelPlayerOutputObject = new ExcelPlayerOutputObject(value);
        midExcelPlayerOutputObjects.set(key, excelPlayerOutputObject);
    });

    const defExcelPlayerOutputObjects = new Map();

    defPlayerMap.forEach((value, key) => {
        let excelPlayerOutputObject = new ExcelPlayerOutputObject(value);
        defExcelPlayerOutputObjects.set(key, excelPlayerOutputObject);
    });

    const workbookColumns = fplUtils.defineExcelOrdering();
    const columnHeaderObjects = new Array(workbookColumns.length);

    workbookColumns.forEach((value, key) => {
        let columnHeaderObject = {  header: key, key: key, width: value.width };

        if(value.outlineLevel){
            columnHeaderObject.outlineLevel = value.outlineLevel;
            columnHeaderObject.hidden = value.hidden;
        }

        columnHeaderObject.style = { 
            font:{ bold: true, name: 'Calibri', size: 9, color: { argb: WHITE_RGB } }, 
            alignment : { vertical: 'middle', horizontal: 'center', shrinkToFit: true }
        };

        columnHeaderObjects.splice(value.position, 1, { 'bgColor' : value.color, 'columnHeaders': columnHeaderObject });
    });

    const columnHeaders = [];

    for(let i = 0; i < columnHeaderObjects.length; i++){
        columnHeaders.push(columnHeaderObjects[i].columnHeaders);
    }

    const workbook = new ExcelJS.Workbook();

    workbook.creator = 'Me';

    const fwdWorksheet = workbook.addWorksheet('FWD', {views:[{state: 'frozen', xSplit: 3, ySplit:1, activeCell: 'A2', showGridLines: true}]});
    const midWorksheet = workbook.addWorksheet('MID', {views:[{state: 'frozen', xSplit: 3, ySplit:1, activeCell: 'A2', showGridLines: true}]});
    const defWorksheet = workbook.addWorksheet('DEF', {views:[{state: 'frozen', xSplit: 3, ySplit:1, activeCell: 'A2', showGridLines: true}]});

    await processWorksheet(fwdWorksheet, fwdExcelPlayerOutputObjects, columnHeaders, columnHeaderObjects, workbookColumns);
    await processWorksheet(midWorksheet, midExcelPlayerOutputObjects, columnHeaders, columnHeaderObjects, workbookColumns);
    await processWorksheet(defWorksheet, defExcelPlayerOutputObjects, columnHeaders, columnHeaderObjects, workbookColumns);

    let fileName = sixWeekTimeFrame ? SIXGAME_PLAYER_EXCEL_FILE : PLAYER_EXCEL_FILE;
    await workbook.xlsx.writeFile(fileName);
}

   async function processWorksheet(worksheet, positionExcelPlayerOutputObjects, columnHeaders, columnHeaderObjects, workbookColumns){
    worksheet.columns = columnHeaders;
    const headerRow = worksheet.getRow(1);
    
    for(let i = 0; i < columnHeaderObjects.length; i++){
        const cell = headerRow.getCell(i + 1);
        cell.fill = { type: 'pattern', pattern:'solid', fgColor:{argb: columnHeaderObjects[i].bgColor} };
        cell.border = DEFAULT_BORDER;
    }

    const rows = worksheet.getRows(2, positionExcelPlayerOutputObjects.size);
    const posIterator = positionExcelPlayerOutputObjects.values();

    for(let i = 0; i < rows.length; i++){
        const posPlayerOutputObject = posIterator.next().value;
        const row = rows[i];
        row.height = 15;

        for(let j = 1; j <= columnHeaders.length; j++){
            const columnHeader = columnHeaders[j - 1];
            const headerKey = columnHeader.key;
            let value = posPlayerOutputObject.excelPlayerValues.get(headerKey);
            const excelHeaderObject = workbookColumns.get(headerKey);

            if(value === null || value === undefined){
                console.error('Value is null or undefined for column: ' + headerKey);
                continue;
            }

            if(excelHeaderObject === null || excelHeaderObject === undefined){
                console.error('Excel header object is null or undefined for column: ' + headerKey);
                continue;
            }

            let numberFormat;

            switch(excelHeaderObject.type){
                case 'Text':
                    numberFormat = '@';
                    break;
                case 'Number':
                    numberFormat = '0' + (excelHeaderObject.precision > 0 ? '.' +'0'.repeat(excelHeaderObject.precision) : '');
                    value = Number(value);
                    break;
                case 'Percentage':
                    numberFormat = '0.00%';
                    value = Number(value);
                    break;
                default:
                    console.error('Unknown type for column: ' + headerKey);
            }

            const alignment = excelHeaderObject.alignment || 'center';
    
            const cell = row.getCell(j);
            cell.font = { name: 'Calibri', size: 9, color: { argb: BLACK_RGB } };
            cell.alignment = { vertical: 'middle', horizontal: alignment, shrinkToFit: true };
            cell.border = DEFAULT_BORDER;
            cell.fill = { type: 'pattern', pattern: 'none' };
            cell.value = value;
            cell.numFmt = numberFormat;
        }
    }

    worksheet.autoFilter = {
        from: {
            row: 1,
            column: 1
        },
        to: {
            row: rows.length,
            column:5
        }
    }
}