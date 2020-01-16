package baker.soccer.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import baker.soccer.fpl.objects.FPLPlayerObject;
import baker.soccer.understat.objects.UnderstatPlayerObject;

public class FootballAnalysisUtil {
	public static String getCellFormat(String columnHeader){
		if(FootballAnalysisConstants.EXCELPERCENTAGECOLUMNS.contains(columnHeader)){
			return "0.0%";
		}
		else if(FootballAnalysisConstants.EXCELWHOLENUMBERCOLUMNS.contains(columnHeader)){
			return "0";
		}
		else if(FootballAnalysisConstants.EXCELHUNDRETHSCOLUMNS.contains(columnHeader)){
			return "0.00";
		}
		else if(FootballAnalysisConstants.EXCELTENTHSCOLUMNS.contains(columnHeader)){
			return "0.0";
		}
		else{
			return "";
		}
	}

	public static String getPlayerStatTableFormulasMap(String positionName, String columnName, int maxColumCount, int maxRowCount, int currentColumCount, int currentRowCount){
		String retVal = null;

		if (columnName != null){
			if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNSIBPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNSHOTS,FootballAnalysisConstants.EXCELCOLUMNSIB, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMN6YDPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNSHOTS,FootballAnalysisConstants.EXCELCOLUMN6YD, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGSHEADPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNSHOTS,FootballAnalysisConstants.EXCELCOLUMNGSHEAD, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNACC)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNSHOTS,FootballAnalysisConstants.EXCELCOLUMNSOT, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNCONV)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNSHOTS,FootballAnalysisConstants.EXCELCOLUMNGS, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNASSPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNKP,FootballAnalysisConstants.EXCELCOLUMNASS, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGSBC)){
				retVal = buildCellMinusCellFormula(FootballAnalysisConstants.EXCELCOLUMNBC,FootballAnalysisConstants.EXCELCOLUMNBCMISS, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBCCONV)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNBC,FootballAnalysisConstants.EXCELCOLUMNGSBC, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMGSBCPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNGS,FootballAnalysisConstants.EXCELCOLUMNGSBC, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBCOPEN)){
				retVal = buildCellMinusSumFormula(FootballAnalysisConstants.EXCELCOLUMNBC,FootballAnalysisConstants.EXCELCOLUMNGSPK, FootballAnalysisConstants.EXCELCOLUMNPKMISS, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBCOPENMISS)){
				retVal = buildCellMinusCellFormula(FootballAnalysisConstants.EXCELCOLUMNBCMISS,FootballAnalysisConstants.EXCELCOLUMNPKMISS, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBCOPENCONV)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNBCOPEN,FootballAnalysisConstants.EXCELCOLUMNBCOPENMISS, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMGIBPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNGS,FootballAnalysisConstants.EXCELCOLUMNGIB, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGSOPENPERCENT)){
				retVal = buildCellDividedByCellFormula(FootballAnalysisConstants.EXCELCOLUMNGS,FootballAnalysisConstants.EXCELCOLUMNGSOPEN, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNPP90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNPTS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNPPAPP)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNAPPS,FootballAnalysisConstants.EXCELCOLUMNPTS, 1, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNGS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBONUS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNBONUS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBPS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNBPS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBASEBPS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNBASEBPS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNPENTCH90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNPENTCH, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNSHOTS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNSHOTS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNSIB90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNSIB, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNS6YD90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMN6YD, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNSOT90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNSOT, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNSHEAD90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNGSHEAD, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNASS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNASS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNFPLASS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNFPLASS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNKP90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNKP, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBCC90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNBCC, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBC90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNBC, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBCMISS90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNBCMISS, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGSBC90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNGSBC, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGIB90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNGIB, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNGSOPEN90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNGSOPEN, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNICT90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNICT, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNXG) || columnName.equals(FootballAnalysisConstants.EXCELCOLUMNXA) || columnName.equals(FootballAnalysisConstants.EXCELCOLUMNXPTS) || columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBASEXPTS) || columnName.equals(FootballAnalysisConstants.EXCELCOLUMNBASEBPS) || columnName.equals(FootballAnalysisConstants.EXCELCOLUMNUXPTS) || columnName.equals(FootballAnalysisConstants.EXCELCOLUMNFSXPTS90)){
				retVal = buildCustomPredefinedFormulas(positionName, columnName, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNFSXG90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNFSXG, 90, maxColumCount, maxRowCount, currentRowCount);
			}
			else if(columnName.equals(FootballAnalysisConstants.EXCELCOLUMNFSXA90)){
				retVal = buildCellDividedByCellMultipledByValueFormula(FootballAnalysisConstants.EXCELCOLUMNMINS,FootballAnalysisConstants.EXCELCOLUMNFSXA, 90, maxColumCount, maxRowCount, currentRowCount);
			}
		}

		return retVal;
	}

	private static String buildCellDividedByCellFormula(String divisorColumn, String dividendColumn, int maxColumnCount, int maxRowCount, int currentRowCount){
		return "IF(HLOOKUP(\""
				+ divisorColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE)=0,0,HLOOKUP(\""
				+ dividendColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE)/HLOOKUP(\""
				+ divisorColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE))";
	}

	private static String buildCellMinusCellFormula(String minuendColumn, String subtrahendColumn, int maxColumnCount, int maxRowCount, int currentRowCount){
		return "HLOOKUP(\""
				+ minuendColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE) - HLOOKUP(\""
				+ subtrahendColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE)";
	}

	private static String buildCellMinusSumFormula(String minuendColumn, String subtrahendColumn1, String subtrahendColumn2, int maxColumnCount, int maxRowCount, int currentRowCount){
		return "HLOOKUP(\""
				+ minuendColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE) - (HLOOKUP(\""
				+ subtrahendColumn1
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE) + HLOOKUP(\""
				+ subtrahendColumn2
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE))";
	}

	private static String buildCellDividedByCellMultipledByValueFormula(String divisorColumn, String dividendColumn, int multiplerFactor, int maxColumnCount, int maxRowCount, int currentRowCount){
		return "IF(HLOOKUP(\""
				+ divisorColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE)=0,0,HLOOKUP(\""
				+ dividendColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE)/HLOOKUP(\""
				+ divisorColumn
				+ "\",A1:"
				+ getColumnAlpha(maxColumnCount)
				+ maxRowCount
				+ ","
				+ currentRowCount
				+ ",FALSE)* "
				+ multiplerFactor
				+ ")";
	}

	private static String buildCustomPredefinedFormulas(String playerPosition, String columnHeader, int maxColumnCount, int maxRowCount, int currentRowCount){
		String retVal = "";
		float PKConv = 0.75f;

		if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNXG)){
			float SIBConv = 0.0f;
			float SOBConv = 0.0f;

			if(playerPosition.equals("MID")){
				SIBConv = 0.142f;
				SOBConv = 0.036f;
			}
			else if(playerPosition.equals("DEF")){
				SIBConv = 0.093f;
				SOBConv = 0.020f;
			}
			else{
				SIBConv = 0.162f;
				SOBConv = 0.043f;
			}

			retVal = "IF(HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNMINS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)=0,0,("
					+ SOBConv
					+ "*(HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNSHOTS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)-HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNSIB
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))+"
					+ SIBConv
					+ "*(HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNSIB
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)-(HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNGSPK
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)+HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNPKMISS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)))+"
					+ PKConv
					+ "*(HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNGSPK
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)+HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNPKMISS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)))/HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNMINS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)*90)";
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNXA)){
			float AConv = 0.0f;

			if(playerPosition.equals("MID")){
				AConv = 0.0885f;
			}
			else if(playerPosition.equals("DEF")){
				AConv = 0.0825f;
			}
			else{
				AConv = 0.103f;
			}

			retVal = AConv
					+ "*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNKP90
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)";
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNXPTS)){
			int pointsPerGoal = 0;

			if(playerPosition.equals("MID")){
				pointsPerGoal = 5;
			}
			else if(playerPosition.equals("DEF")){
				pointsPerGoal = 6;
			}
			else{
				pointsPerGoal = 4;
			}

			retVal = pointsPerGoal
					+ "*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNXG
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)+3*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNXA
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)";
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNBASEXPTS)){
			int pointsPerGoal = 0;

			if(playerPosition.equals("MID")){
				pointsPerGoal = 5;
			}
			else if(playerPosition.equals("DEF")){
				pointsPerGoal = 6;
			}
			else{
				pointsPerGoal = 4;
			}

			retVal = "HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNXPTS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)-("
					+ pointsPerGoal
					+ "*"
					+ PKConv
					+ "*(HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNGSPK
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)+HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNPKMISS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))*90/HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNMINS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))";
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNBASEBPS)){
			int bpsPerGoal = 0;
			int bpsPerCS = 0;

			if(playerPosition.equals("MID")){
				bpsPerGoal = 18;
			}
			else if(playerPosition.equals("DEF")){
				bpsPerGoal = 12;
			}
			else{
				bpsPerGoal = 24;
			}

			if(playerPosition.equals("DEF")){
				bpsPerCS = 12;
			}

			retVal = "HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNBPS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)-((-6*"
					+ "HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNOG
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))+(-6*"
					+ "HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNPKMISS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))+(9*"
					+ "HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNFPLASS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))+("
					+ bpsPerCS
					+ "*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNCS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE))+("
					+ bpsPerGoal
					+ "*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNGS
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)))";
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNUXPTS)){
			int pointsPerGoal = 0;

			if(playerPosition.equals("MID")){
				pointsPerGoal = 5;
			}
			else if(playerPosition.equals("DEF")){
				pointsPerGoal = 6;
			}
			else{
				pointsPerGoal = 4;
			}

			retVal = pointsPerGoal
					+ "*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNUXG
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)+3*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNUXA
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)";
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNFSXPTS90)){
			int pointsPerGoal = 0;

			if(playerPosition.equals("MID")){
				pointsPerGoal = 5;
			}
			else if(playerPosition.equals("DEF")){
				pointsPerGoal = 6;
			}
			else{
				pointsPerGoal = 4;
			}

			retVal = pointsPerGoal
					+ "*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNFSXG90
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)+3*HLOOKUP(\""
					+ FootballAnalysisConstants.EXCELCOLUMNFSXA90
					+ "\",A1:"
					+ getColumnAlpha(maxColumnCount)
					+ maxRowCount
					+ ","
					+ currentRowCount
					+ ",FALSE)";
		}

		return retVal;
	}

	public static String getColumnAlpha(int columnIndex){
		int localColumnIndex = columnIndex - 1;
		StringBuffer retVal = new StringBuffer();

		int remaining = localColumnIndex / 26;

		if (remaining > 0){
			retVal.append(getColumnAlpha(localColumnIndex / 26));
			retVal.append(getAplhaByInt(localColumnIndex % 26));
		}
		else{
			retVal.append(getAplhaByInt(localColumnIndex % 26));
		}

		return retVal.toString();
	}

	private static String getAplhaByInt(int integer){
		return Character.toString((char)(integer + 65));
	}

	public static boolean renameFile(String oldFileName, String newFileName, boolean includePath) throws Exception{
		File file = new File(oldFileName);

		if(includePath){
			newFileName = file.getParent() + "\\" + newFileName;
		}

		// File (or directory) with new name
		File file2 = new File(newFileName);
		if(file2.exists()){
			System.err.println("Rename failed because " + newFileName + " already exists");
			return false;
		}

		// Rename file (or directory)
		System.out.println("Renaming to " + file2);
		return(file.renameTo(file2));
	}

	public static Vector <String> getFiles(String directory, boolean parseDirectory) throws Exception{
		Vector <String> retVal = new Vector<String> ();

		File folder = new File(directory);
		File[] listOfFiles = folder.listFiles();

		System.out.println("Processing " + directory + ": parseDirectory = " + parseDirectory);

		if (listOfFiles != null){
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					retVal.add(directory + listOfFiles[i].getName());
				}
				else if (parseDirectory){
					retVal.addAll(getFiles(directory + listOfFiles[i].getName() + "\\", parseDirectory));
				}
			}
		}

		return retVal;
	}

	public static boolean writeFile(String fileName, String fileData) throws Exception{
		boolean retVal = true;

		try{
			System.out.println("Writing file " + fileName);
			File file = new File(fileName);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			Writer fw = new OutputStreamWriter(new FileOutputStream(file.getAbsoluteFile()), StandardCharsets.UTF_8);

			BufferedWriter bw = new BufferedWriter(fw);

			bw.write(fileData.toString());
			bw.close();
		}
		catch(Exception e){
			retVal = false;
			throw new Exception (e);
		}

		return retVal;
	}

	public static int getMonthNumber(String month){
		if (month.equals("Jan"))
			return Calendar.JANUARY;
		else if (month.equals("Feb"))
			return Calendar.FEBRUARY;
		else if (month.equals("Mar"))
			return Calendar.MARCH;
		else if (month.equals("Apr"))
			return Calendar.APRIL;
		else if (month.equals("May"))
			return Calendar.MAY;
		else if (month.equals("Jun"))
			return Calendar.JUNE;
		else if (month.equals("Jul"))
			return Calendar.JULY;
		else if (month.equals("Aug"))
			return Calendar.AUGUST;
		else if (month.equals("Sep"))
			return Calendar.SEPTEMBER;
		else if (month.equals("Oct"))
			return Calendar.OCTOBER;
		else if (month.equals("Nov"))
			return Calendar.NOVEMBER;
		else if (month.equals("Dec"))
			return Calendar.DECEMBER;
		else return -1;
	}

	public static ArrayList<String> getFileDataByLine(String fileName) throws Exception{
		ArrayList<String> retVal = new ArrayList<String> ();

		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		String line;

		while ((line = reader.readLine()) != null){
			retVal.add(FootballAnalysisUtil.convertCharsetChars(line));
		}

		reader.close();
		return retVal;
	}

	public static double getUnderstatStat(String columnHeader, UnderstatPlayerObject understatPlayerObject){
		if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNUXG)){
			return (double)understatPlayerObject.getxG90();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNUXA)){
			return (double)understatPlayerObject.getxA90();
		}
		else return -99d;
	}

	public static double getEPLStat(String columnHeader, FPLPlayerObject playerObj){
		if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNFPLASS)){
			return (double)playerObj.getAssists();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNPTS)){
			return (double)playerObj.getTotal_points();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNBONUS)){
			return playerObj.getBonus();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNBPS)){
			return playerObj.getBps();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNPRICE)){
			return playerObj.getNow_cost();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNOG)){
			return playerObj.getOwn_goals();
		}
		else if (columnHeader.equals(FootballAnalysisConstants.EXCELCOLUMNCS)){
			return playerObj.getClean_sheets();
		}
		else return -99d;
	}

	public static int getMatchSeason(Date rawMatchDate){
		Calendar matchDate = Calendar.getInstance();
		matchDate.setTime(rawMatchDate);

		if(FootballAnalysisConstants.EPL_FIRST_HALF_MONTHS.contains(matchDate.get(Calendar.MONTH))){
			return matchDate.get(Calendar.YEAR);
		}
		else if(FootballAnalysisConstants.EPL_SECOND_HALF_MONTHS.contains(matchDate.get(Calendar.MONTH))){
			return matchDate.get(Calendar.YEAR) - 1;
		}
		else{
			System.err.println("Month out of range for " + matchDate.get(Calendar.MONTH));
			return 0;
		}
	}

	public static String convertCharsetChars(String playerName) {
		/*StringBuffer retVal = new StringBuffer("");
		
		for(int i = 0; i < playerName.length(); i++){
			if(playerName.codePointAt(i) == 263){
				retVal.append('c');
				System.out.println("Chaning character for " + playerName);
			}
			else if(playerName.codePointAt(i) == 115){
				retVal.append('s');
				System.out.println("Chaning character for " + playerName);
			}
			else{
				retVal.append(playerName.charAt(i));
			}
		}
		
		return retVal.toString();*/
		
		return playerName;
	}
}