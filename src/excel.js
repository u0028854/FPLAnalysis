const ExcelJS = require('exceljs');

async function main(){
    const workbook = new ExcelJS.Workbook();

    workbook.creator = 'Me';

    const worksheet = workbook.addWorksheet('My Sheet', {views:[{state: 'frozen', xSplit: 3, ySplit:1}]});
    const integerColumns = [
        { header: 'Name', key: 'Name', width: 8 },
        { header: 'Team', key: 'Team', width: 8 },
        { header: 'Price', key: 'Price', width: 8 },
        { header: 'Mins', key: 'Mins', width: 8 },
        { header: 'Apps', key: 'Apps', width: 8 },
        { header: 'Pts', key: 'Pts', width: 8 },
        { header: 'Gs', key: 'Gs', width: 8 },
        { header: 'Bonus', key: 'Bonus', width: 8 },
        { header: 'BPS', key: 'BPS', width: 8 },
        { header: 'Base BPS', key: 'Base BPS', width: 8 },
        { header: 'OG', key: 'OG', width: 8 },
        { header: 'Def Con', key: 'Def Con', width: 8 },
        { header: 'Def Con Pts', key: 'Def Con Pts', width: 8 },
        { header: 'CS', key: 'CS', width: 8 },
        { header: 'PenTchs', key: 'PenTchs', width: 8 },
        { header: 'Shots', key: 'Shots', width: 8 },
        { header: 'NP Shots', key: 'NP Shots', width: 8 },
        { header: 'SIB', key: 'SIB', width: 8 },
        { header: 'SIB %', key: 'SIB %', width: 8 },
        { header: 'Headers', key: 'Headers', width: 8 },
        { header: 'Headers %', key: 'Headers %', width: 8 },
        { header: 'SOT', key: 'SOT', width: 8 },
        { header: 'Acc', key: 'Acc', width: 8 },
        { header: 'Conv', key: 'Conv', width: 8 },
        { header: 'As', key: 'As', width: 8 },
        { header: 'FPL As', key: 'FPL As', width: 8 },
        { header: 'KP', key: 'KP', width: 8 },
        { header: 'As %', key: 'AS %', width: 8 },
        { header: 'ICT Index', key: 'ICT Index', width: 8 },
        { header: 'Goal Involvement', key: 'Goal Involvement', width: 8 },
        { header: 'BC Created', key: 'BC Created', width: 8 },
        { header: 'BC', key: 'BC', width: 8 },
        { header: 'BC Miss', key: 'BC Miss', width: 8 },
        { header: 'BC Conv', key: 'BC Conv', width: 8 },
        { header: 'Gs - BC', key: 'Gs - BC', width: 8 },
        { header: 'BC G %', key: 'BC G %', width: 8 },
        { header: 'PK Gs', key: 'PK Gs', width: 8 },
        { header: 'PK Miss', key: 'PK Miss', width: 8 },
        { header: 'GIB', key: 'GIB', width: 8 },
        { header: 'GIB %', key: 'GIB %', width: 8 },
        { header: 'fsXG', key: 'fsXG', width: 8 },
        { header: 'fsNPXG', key: 'fsNPXG', width: 8 },
        { header: 'fsXA', key: 'fsXA', width: 8 }
    ];

    const forumlaColumns = [
        { header: 'PP90', key: 'PP90', width: 8 },
        { header: 'PPAPP', key: 'PPAPP', width: 8 },
        { header: 'Gs/90', key: 'Gs-90', width: 8 },
        { header: 'Bonus/90', key: 'Bonus-90', width: 8 },
        { header: 'BPS/90', key: 'BPS-90', width: 8 },
        { header: 'Base BPS/90', key: 'Base BPS-90', width: 8 },
        { header: 'OG/90', key: 'OG-90', width: 8 },
        { header: 'Def Con/App', key: 'DefCon-App', width: 8 },
        { header: 'PenTchs/90', key: 'PenTchs-90', width: 8 },
        { header: 'Shots/90', key: 'Shots-90', width: 8 },
        { header: 'NP Shots/90', key: 'NP Shots-90', width: 8 },
        { header: 'SIB/90', key: 'SIB-90', width: 8 },
        { header: 'Headers/90', key: 'Headers-90', width: 8 },
        { header: 'SOT/90', key: 'SOT-90', width: 8 },
        { header: 'BC/90', key: 'BC-90', width: 8 },
        { header: 'Gs - BC/90', key: 'Gs - BC-90', width: 8 },
        { header: 'GIB/90', key: 'GIB-90', width: 8 },
        { header: 'NPXG/SH', key: 'NPXG-SH', width: 8 },
        { header: 'As/90', key: 'As-90', width: 8 },
        { header: 'FPL As/90', key: 'FPL As-90', width: 8 },
        { header: 'KP/90', key: 'KP-90', width: 8 },
        { header: 'BCC/90', key: 'BCC-90', width: 8 },
        { header: 'ICT Index/90', key: 'ICT Index-90', width: 8 }
    ];

    const fsXGColumns = [
        { header: 'fsXG/90', key: 'fsXG-90', width: 8 },
        { header: 'fsNPXG/90', key: 'fsNPXG-90', width: 8 },
        { header: 'fsXA/90', key: 'fsXA-90', width: 8 },
        { header: 'fsXPTS/90', key: 'fsXPTS-90', width: 8 },
        { header: 'fsNPXPTS/90', key: 'fsNPXPTS-90', width: 8 }
    ];
    
    const uXGColumns = [
        { header: 'uXG/90', key: 'uXG-90', width: 8 },
        { header: 'uNPXG/90', key: 'uNPXG-90', width: 8 },
        { header: 'uXA/90', key: 'uXA-90', width: 8 },
        { header: 'uXPTS/90', key: 'uXPTS-90', width: 8 },
        { header: 'uXNPPTS/90', key: 'uXNPPTS-90', width: 8 },
    ];
    
    const avgXGColumns = [
        { header: 'avgXG/90', key: 'avgXG-90', width: 8 },
        { header: 'avgNPXG/90', key: 'avgNPXG-90', width: 8 },
        { header: 'avgXA/90', key: 'avgXA-90', width: 8 },
        { header: 'avgXPTS/90', key: 'avgXPTS-90', width: 8 },
        { header: 'avgNPXPTS/90', key: 'avgNPXPTS-90', width: 8 }
    ];

    const consolidatedColumns = integerColumns.concat(forumlaColumns).concat(fsXGColumns).concat(uXGColumns).concat(avgXGColumns);

    consolidatedColumns.forEach(column => {
        column.style = { 
            font:{ bold: true, name: 'Calibri' }, 
            alignment : { vertical: 'middle', horizontal: 'center' },
            fill: { type: 'pattern', pattern:'solid', fgColor:{argb:'FF0000'} }
        };
    });

    worksheet.columns = consolidatedColumns;

    await workbook.xlsx.writeFile('test.xlsx');
}

main();