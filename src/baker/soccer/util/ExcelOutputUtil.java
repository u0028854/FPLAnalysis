package baker.soccer.util;

import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import baker.soccer.util.objects.ExcelCellObject;
import baker.soccer.util.objects.ExcelCellStyle;

public class ExcelOutputUtil {
	public static final int columnsFreeze = 3;

	public static XSSFWorkbook buildAnalysisExcel(XSSFWorkbook outputWorkbook, ArrayList<ArrayList<ExcelCellObject>> excelData, String sheetName, int coreDataColumnCount, int totalDataColumnCount) throws Exception{
		XSSFSheet outputSheet = outputWorkbook.createSheet(sheetName);

		int rateDataColumnCount = totalDataColumnCount - coreDataColumnCount;

		for (int i = 0; i < excelData.size(); i++){
			XSSFRow outputRow = outputSheet.createRow(i);
			ArrayList<ExcelCellObject> cellData = excelData.get(i);

			for (int j = 0; j < cellData.size(); j++){
				XSSFCell outputCell = outputRow.createCell(j, XSSFCell.CELL_TYPE_STRING);

				ExcelCellObject individualCell = cellData.get(j);
				ExcelCellStyle cellStyle = new ExcelCellStyle(outputWorkbook.getStylesSource());
				XSSFFont cellFont = outputWorkbook.createFont();

				cellFont.setFontHeightInPoints(FootballAnalysisConstants.DEFAULTEXCELFONTSIZE);
				cellFont.setFontName(FootballAnalysisConstants.DEFAULTEXCELFONTFAMILY);
				cellFont.setColor(individualCell.getFontColor());
				cellFont.setBold(individualCell.isTextBold());
				cellFont.setItalic(individualCell.isTextItalics());

				cellStyle.setFont(cellFont);

				cellStyle.setAlignment(individualCell.getHorizontalAlignment());
				cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);

				if(individualCell.isThinBorder())
					cellStyle.setThinBorder(FootballAnalysisConstants.TRUEBLACK);

				if(individualCell.getDataFormat() != null)
					cellStyle.setDataFormat(outputWorkbook.createDataFormat().getFormat(individualCell.getDataFormat()));

				cellStyle.setPlainFillColor(individualCell.getFillColor());

				outputCell.setCellStyle(cellStyle);
				outputCell.setCellType(individualCell.getCellType());

				switch(outputCell.getCellType()) {
				case Cell.CELL_TYPE_BOOLEAN:
					outputCell.setCellValue((boolean)individualCell.getCellData());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					outputCell.setCellValue((double)individualCell.getCellData());
					break;
				case Cell.CELL_TYPE_STRING:
					outputCell.setCellValue((String)individualCell.getCellData());
					break;
				case Cell.CELL_TYPE_FORMULA:
					outputCell.setCellFormula((String)individualCell.getCellData());
					break;
				case Cell.CELL_TYPE_ERROR:
					break;
				case Cell.CELL_TYPE_BLANK:
					break;
				}
			}
		}

		outputSheet.createFreezePane(columnsFreeze, 1);
		outputSheet.groupColumn(columnsFreeze, coreDataColumnCount - 1);
		outputSheet.setAutoFilter(CellRangeAddress.valueOf("A1:" + FootballAnalysisUtil.getColumnAlpha(coreDataColumnCount + rateDataColumnCount) + "1"));

		return outputWorkbook;
	}
}
