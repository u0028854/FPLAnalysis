package baker.soccer.util;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.xssf.usermodel.XSSFColor;

public class FootballAnalysisConstants {
	// Command Line Options
	public static final String PROCESS_FS_PLAYERMATCH_ARG = "process_fs_player_matches";
	public static final String PROCESS_FS_TEAM_MATCH_ARG = "process_fs_team_matches";
	public static final String RENAME_FILES_ARG = "rename_files";
	//public static final String FS_MATCH_GET_ALL_YEAR_ARG = "get_all_year";
	public static final String FS_MATCH_TEAM_ONLY = "team_data_only";
	public static final String FS_MATCH_PLAYERS_ONLY = "player_data_only";
	public static final String FS_PLAYER_ANALYSIS_EXCEL_ARG = "fs_player_analysis_excel";
	public static final String FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG = "fs_player_analysis_excel_six_week";
	public static final String FS_PLAYER_ANALYSIS_EXCEL_4GW_ARG = "fs_player_analysis_excel_four_week";
	public static final String FS_TEAM_ANALYSIS = "week_team";
	public static final String FS_TEAM_MATCH_ANALYSIS = "match_team";
	public static final String AWAY_ONLY = "away";
	public static final String HOME_ONLY = "home";

	// Data set constants
	public static final int[] EPL_SEASONS = {2011, 2012, 2013, 2014, 2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025};
	public static final String[] PLAYER_POSITIONS = {"DEF", "FWD", "MID"};
	public static final ArrayList<Integer> EPL_FIRST_HALF_MONTHS = getCalendarMonths(true);
	public static final ArrayList<Integer> EPL_SECOND_HALF_MONTHS = getCalendarMonths(false);
	public static final ArrayList<java.util.Date> FPL_GAMEWEEK_DATES = getGameweekDates();

	// Path constants
	public static final String EPL_BASE_DIR = "c:\\EPLJava";
	public static final String MATCHES_BASEDIR = EPL_BASE_DIR + "\\fsmatches";
	public static final String FWD_STATS_TABLE1 = EPL_BASE_DIR + "\\stat_tables\\fwd.html";
	public static final String FWD_STATS_TABLE2 = EPL_BASE_DIR + "\\stat_tables\\fwd2.html";
	public static final String FWD_STATS_TABLE3 = EPL_BASE_DIR + "\\stat_tables\\fwd3.html";
	public static final String MID_STATS_TABLE1 = EPL_BASE_DIR + "\\stat_tables\\mid.html";
	public static final String MID_STATS_TABLE2 = EPL_BASE_DIR + "\\stat_tables\\mid2.html";
	public static final String MID_STATS_TABLE3 = EPL_BASE_DIR + "\\stat_tables\\mid3.html";
	public static final String DEF_STATS_TABLE1 = EPL_BASE_DIR + "\\stat_tables\\def.html";
	public static final String DEF_STATS_TABLE2 = EPL_BASE_DIR + "\\stat_tables\\def2.html";
	public static final String DEF_STATS_TABLE3 = EPL_BASE_DIR + "\\stat_tables\\def3.html";
	public static final String FWD_STATS_SBxG = EPL_BASE_DIR + "\\stat_tables\\fwdSBxG.html";
	public static final String FWD_STATS_SBxA = EPL_BASE_DIR + "\\stat_tables\\fwdSBxA.html";
	public static final String MID_STATS_SBxG = EPL_BASE_DIR + "\\stat_tables\\midSBxG.html";
	public static final String MID_STATS_SBxA = EPL_BASE_DIR + "\\stat_tables\\midSBxA.html";
	public static final String DEF_STATS_SBxG = EPL_BASE_DIR + "\\stat_tables\\defSBxG.html";
	public static final String DEF_STATS_SBxA = EPL_BASE_DIR + "\\stat_tables\\defSBxA.html";

	public static final String TEAM_STATS_TABLE1 = EPL_BASE_DIR + "\\stat_tables\\team.html";
	public static final String FPL_FS_PLAYER_MAP = EPL_BASE_DIR + "\\stat_tables\\player_map.csv";
	public static final String FPL_GW_MAP = EPL_BASE_DIR + "\\stat_tables\\GWSchedule.csv";
	public static final String STATS_TABLE_OUTPUT_FILE = EPL_BASE_DIR + "\\EPL Excels\\Analysis.xlsx";
	public static final String STATS_TABLE_OUTPUT_6GW_FILE = EPL_BASE_DIR + "\\EPL Excels\\6GW_Analysis.xlsx";
	public static final String STATS_TABLE_OUTPUT_4GW_FILE = EPL_BASE_DIR + "\\EPL Excels\\4GW_Analysis.xlsx";
	public static final String USHTMLFILENAME = EPL_BASE_DIR + "\\stat_tables\\understat.html";
	public static final String USPLAYERINPUTFILENAME = EPL_BASE_DIR + "\\stat_tables\\playerUnderstat.csv";
	public static final String USPLAYERINPUT6GWFILENAME = EPL_BASE_DIR + "\\stat_tables\\playerUnderstat6GW.csv";
	public static final String USPLAYEROUTPUTFILENAME = EPL_BASE_DIR + "\\stat_tables\\playerUnderstat.json";
	public static final String USTEAMOUTPUTFILENAME = EPL_BASE_DIR + "\\stat_tables\\teamUnderstat.json";
	public static final String FBREFPLAYEROUTPUTFILENAME = EPL_BASE_DIR + "\\stat_tables\\fbref.htm";
	public static final String FBREFTEAMOUTPUTFILENAME = EPL_BASE_DIR + "\\stat_tables\\fbref_team.htm";
	public static final String FBREFHTMLPLAYERFILENAME = EPL_BASE_DIR + "\\stat_tables\\fbref.html";
	public static final String FBREFHTMLTEAMFILENAME = EPL_BASE_DIR + "\\stat_tables\\fbref_team.html";
	public static final String FPLPLAYERJSON = EPL_BASE_DIR + "\\stat_tables\\fpl.json";
	public static final String FPLPLAYERFOURWEEKJSON = EPL_BASE_DIR + "\\stat_tables\\4weeks_fpl.json";
	public static final String FPLPLAYERSIXWEEKJSON = EPL_BASE_DIR + "\\stat_tables\\6weeks_fpl.json";
	
	// FPL Constants
	public static final String FPLAPIURL = "https://fantasy.premierleague.com/api/bootstrap-static/";

	//  Understat Constants
	public static final String USPLAYERHEADERSTRING = "var playersData	= JSON.parse('";
	public static final String USTEAMHEADERSTRING = "var teamsData = JSON.parse('";
	public static final String USPLAYERFOOTERSTRING = "');";
	public static final String USTEAMFOOTERSTRING = "');";
	public static final int USPLAYERS = 1;
	public static final int USTEAMS = 2;
	public static final String USAPIURL = "https://understat.com/main/getPlayersStats/";
	public static final String[] USAPIPARAMS = {"league=EPL"};

	// Database insert constants
	public static final String MATCH_PLAYERS_PRUNED_VALUES = "player_pruned";
	public static final String MATCH_TEAM_PRUNED_VALUES = "team_pruned";

	public static final ArrayList<String> DBPLAYERSTATLIST = getDBPlayerStatList();
	public static final ArrayList<String> DBTEAMSTATLIST = getDBTeamStatList();

	// Mongo DB Constants
	public static final String MONGODBHOSTNAME = "localhost";
	public static final int MONGODBHOSTPORT = 27017;
	public static final String MONGODATABASENAME = "FFSData";
	public static final String MONGOPLAYERCOLLECTIONNAME = "FFSPlayerData";
	public static final String MONGOTEAMCOLLECTIONNAME = "FFSTeamData";

	// Excel Format Options    
	public static final XSSFColor MYBLUE = new XSSFColor(new Color(0,32,96));
	public static final XSSFColor MYRED = new XSSFColor(new Color (192,0,0));
	public static final XSSFColor MYGREEN = new XSSFColor(new Color (0,176,80));
	public static final XSSFColor MYBROWN = new XSSFColor(new Color (128,96,0));
	public static final XSSFColor MYORANGE = new XSSFColor(new Color (204,104,0));
	public static final XSSFColor TRUEWHITE = new XSSFColor(new Color(254,255,255));
	public static final XSSFColor TRUEBLACK = new XSSFColor(new Color(1,0,0));
	public static final XSSFColor TRUEGREEN = new XSSFColor(new Color (0,255,0));
	public static final XSSFColor TRUEYELLOW = new XSSFColor(new Color (255,255,102));
	public static final XSSFColor DARKGRAY = new XSSFColor(new Color (217,217,217));
	public static final XSSFColor DEEPPURPLE = new XSSFColor(new Color (102,0,102));
	public static final XSSFColor DEEPBLUE = new XSSFColor(new Color (31,78,120));
	public static final short DEFAULTEXCELFONTSIZE = 9;
	public static final String DEFAULTEXCELFONTFAMILY = "Calibri";
	public static final String FORMULAIDENTIFIER = "FORMULA-CELL";
	public static final int EXCELMAXROWCOUNT = 300;
	public static final int EXCELMAXCOLUMNCOUNT = 100;

	// Excel Content Headers
	public static final String EXCELCOLUMNNAME ="Name";
	public static final String EXCELCOLUMNTEAM ="Team";
	public static final String EXCELCOLUMNPRICE ="Price";
	public static final String EXCELCOLUMNMINS ="Mins";
	public static final String EXCELCOLUMNAPPS ="Apps";
	public static final String EXCELCOLUMNPTS ="Pts";
	public static final String EXCELCOLUMNGS ="Gs";
	public static final String EXCELCOLUMNBONUS ="Bonus";
	public static final String EXCELCOLUMNBPS ="BPS";
	public static final String EXCELCOLUMNBASEBPS ="Base BPS";
	public static final String EXCELCOLUMNCS ="CS";
	public static final String EXCELCOLUMNDEFCON ="Def Con";
	public static final String EXCELCOLUMNDEFCONPTS ="Def Con Pts";
	public static final String EXCELCOLUMNDEFCONPTSAPP ="Def Con/App";
	public static final String EXCELCOLUMNOG ="OG";
	public static final String EXCELCOLUMNPENTCH ="PenTchs";
	public static final String EXCELCOLUMNSHOTS ="Shots";
	public static final String EXCELCOLUMNNPSHOTS ="NP Shots";
	public static final String EXCELCOLUMNSIB ="SIB";
	public static final String EXCELCOLUMNSIBPERCENT ="SIB %";
	public static final String EXCELCOLUMN6YD ="S6YD";
	public static final String EXCELCOLUMN6YDPERCENT ="S6YD %";
	public static final String EXCELCOLUMNSOT ="SOT";
	public static final String EXCELCOLUMNGSHEAD ="Headers";
	public static final String EXCELCOLUMNGSHEADPERCENT ="Headers %";
	public static final String EXCELCOLUMNACC ="Acc";
	public static final String EXCELCOLUMNCONV ="Conv";
	public static final String EXCELCOLUMNASS ="As";
	public static final String EXCELCOLUMNFPLASS ="FPL As";
	public static final String EXCELCOLUMNKP ="KP";
	public static final String EXCELCOLUMNASSPERCENT ="As %";
	public static final String EXCELCOLUMNBCC ="BC Created";
	public static final String EXCELCOLUMNBC ="BC";
	public static final String EXCELCOLUMNBCMISS ="BC Miss";
	public static final String EXCELCOLUMNBCCONV ="BC Conv";
	public static final String EXCELCOLUMNGSBC ="Gs - BC";
	public static final String EXCELCOLUMGSBCPERCENT ="BC G %";
	public static final String EXCELCOLUMNGSPK ="PK Gs";
	public static final String EXCELCOLUMNPKMISS ="PK Miss";
	public static final String EXCELCOLUMNBCOPEN ="BC - Open";
	public static final String EXCELCOLUMNBCOPENMISS ="BC - Open Miss";
	public static final String EXCELCOLUMNBCOPENCONV ="BC Open Conv";
	public static final String EXCELCOLUMNGIB ="GIB";
	public static final String EXCELCOLUMGIBPERCENT ="GIB %";
	public static final String EXCELCOLUMNGSOPEN ="Gs - Open";
	public static final String EXCELCOLUMNGSOPENPERCENT ="Gs - Open %";
	public static final String EXCELCOLUMNGGOALINV ="Goal Involvement";
	public static final String EXCELCOLUMNICT ="ICT Index";
	public static final String EXCELCOLUMNPP90 ="PP90";
	public static final String EXCELCOLUMNPPAPP ="PPAPP";
	public static final String EXCELCOLUMNGS90 ="Gs/90";
	public static final String EXCELCOLUMNBONUS90 ="Bonus/90";
	public static final String EXCELCOLUMNBPS90 ="BPS/90";
	public static final String EXCELCOLUMNBASEBPS90 ="Base BPS/90";
	public static final String EXCELCOLUMNPENTCH90 ="PTs/90";
	public static final String EXCELCOLUMNSHOTS90 ="Shots/90";
	public static final String EXCELCOLUMNNPSHOTS90 ="NP Shots/90";
	public static final String EXCELCOLUMNSIB90 ="SIB/90";
	public static final String EXCELCOLUMNS6YD90 ="S6YD/90";
	public static final String EXCELCOLUMNSOT90 ="SOT/90";
	public static final String EXCELCOLUMNNPXGSHOT ="NPXG/SH";
	public static final String EXCELCOLUMNSHEAD90 ="Headers/90";
	public static final String EXCELCOLUMNASS90 ="As/90";
	public static final String EXCELCOLUMNFPLASS90 ="FPL As/90";
	public static final String EXCELCOLUMNKP90 ="KP/90";
	public static final String EXCELCOLUMNBCC90 ="BCC/90";
	public static final String EXCELCOLUMNBC90 ="BC/90";
	public static final String EXCELCOLUMNBCMISS90 ="BC Miss/90";
	public static final String EXCELCOLUMNGSBC90 ="Gs - BC/90";
	public static final String EXCELCOLUMNGIB90 ="GIB/90";
	public static final String EXCELCOLUMNGSOPEN90 ="Gs - Open/90";
	public static final String EXCELCOLUMNICT90 ="ICT/90";
	public static final String EXCELCOLUMNXG ="xG/90";
	public static final String EXCELCOLUMNXA ="xA/90";
	public static final String EXCELCOLUMNXPTS ="xPts/90";
	public static final String EXCELCOLUMNBASEXPTS ="Base xPts/90";
	public static final String EXCELCOLUMNUXG ="uXG/90";
	public static final String EXCELCOLUMNUNPXG ="uNPXG/90";
	public static final String EXCELCOLUMNUXA ="uXA/90";
	public static final String EXCELCOLUMNUXPTS ="uXPTS/90";
	public static final String EXCELCOLUMNUXNPPTS ="uXNPPTS/90";
	public static final String EXCELCOLUMNFSXG ="fsXG";
	public static final String EXCELCOLUMNFSNPXG ="fsNPXG";
	public static final String EXCELCOLUMNFSXA ="fsXA";
	public static final String EXCELCOLUMNSBXG ="SB xG";
	public static final String EXCELCOLUMNSBNPXG ="SB NPxG";
	public static final String EXCELCOLUMNSBXA ="SB xA";
	public static final String EXCELCOLUMNFSXG90 ="fsXG/90";
	public static final String EXCELCOLUMNFSNPXG90 ="fsNPXG/90";
	public static final String EXCELCOLUMNFSXA90 ="fsXA/90";
	public static final String EXCELCOLUMNFSXPTS90 ="fsXPTS/90";
	public static final String EXCELCOLUMNFSNPXPTS90 ="fsNPXPTS/90";
	public static final String EXCELCOLUMNSBXG90 ="sbXG/90";
	public static final String EXCELCOLUMNSBNPXG90 ="sbNPXG/90";
	public static final String EXCELCOLUMNSBXA90 ="sbXA/90";
	public static final String EXCELCOLUMNSBXPTS90 ="sbXPTS/90";
	public static final String EXCELCOLUMNSBNPXPTS90 ="sbNPXPTS/90";
	public static final String EXCELCOLUMNFBREFXG ="fbrefXG/90";
	public static final String EXCELCOLUMNFBREFNPXG ="fbrefNPXG/90";
	public static final String EXCELCOLUMNFBREFXA ="fbrefXA/90";
	public static final String EXCELCOLUMNFBREFXPTS ="fbrefXPTS/90";
	public static final String EXCELCOLUMNFBREFNPXPTS ="fbrefNPXPTS/90";
	public static final String EXCELCOLUMNAVGXG ="avgXG/90";
	public static final String EXCELCOLUMNAVGNPXG ="avgNPXG/90";
	public static final String EXCELCOLUMNAVGXA ="avgXA/90";
	public static final String EXCELCOLUMNAVGXPTS ="avgXPTS/90";
	public static final String EXCELCOLUMNAVGNPXPTS ="avgNPXPTS/90";

	// Excel Column Groupings and Data
	public static final ArrayList<String> EXCELEPLCOLUMNS = defineExcelEPLColumns();
	public static final ArrayList<String> EXCELUNDERSTATCOLUMNS = defineExcelUnderstatColumns();
	public static final ArrayList<String> EXCELFBREFCOLUMNS = defineExcelFBRefColumns();
	public static final ArrayList<String> EXCELPERCENTAGECOLUMNS = getPercentageColumns();
	public static final ArrayList<String> EXCELWHOLENUMBERCOLUMNS = getWholeNumberColumns();
	public static final ArrayList<String> EXCELHUNDRETHSCOLUMNS = getHundrethsColumns();
	public static final ArrayList<String> EXCELTENTHSCOLUMNS = getTenthsColumns();

	public static final ArrayList<String> EXCELBLUECOLUMNHEADERS = getAnalysisBlueColumnHeaders();
	public static final ArrayList<String> EXCELREDCOLUMNHEADERS = getAnalysisRedColumnHeaders();
	public static final ArrayList<String> EXCELGREENCOLUMNHEADERS = getAnalysisGreenColumnHeaders();
	public static final ArrayList<String> EXCELORANGECOLUMNHEADERS = getAnalysisOrangeColumnHeaders();
	public static final ArrayList<String> EXCELBROWNCOLUMNHEADERS = getAnalysisBrownColumnHeaders();
	public static final ArrayList<String> EXCELPURPLECOLUMNHEADERS = getAnalysisPurpleColumnHeaders();
	public static final ArrayList<String> EXCELDEEPBLUECOLUMNHEADERS = getAnalysisDeepBlueColumnHeaders();

	public static final int EXCELBLUECOLUMNCOUNT = EXCELBLUECOLUMNHEADERS.size();
	public static final int EXCELREDCOLUMNCOUNT = EXCELREDCOLUMNHEADERS.size();
	public static final int EXCELGREENCOLUMNCOUNT = EXCELGREENCOLUMNHEADERS.size();
	public static final int EXCELORANGECOLUMNCOUNT = EXCELORANGECOLUMNHEADERS.size();
	public static final int EXCELBROWNCOLUMNCOUNT = EXCELBROWNCOLUMNHEADERS.size();
	public static final int EXCELPURPLECOLUMNCOUNT = EXCELPURPLECOLUMNHEADERS.size();
	public static final int EXCELDEEPBLUECOLUMNCOUNT = EXCELDEEPBLUECOLUMNHEADERS.size();
	public static final int EXCELTOTALCOLUMNCOUNT = EXCELBLUECOLUMNCOUNT + EXCELREDCOLUMNCOUNT + EXCELGREENCOLUMNCOUNT + EXCELORANGECOLUMNCOUNT + EXCELBROWNCOLUMNCOUNT + EXCELPURPLECOLUMNCOUNT + EXCELDEEPBLUECOLUMNCOUNT;

	// Excel Column Data 
	public static final HashMap <String, String> EXCELPLAYERSTATMAP = getExcelPlayerStatTableMap();

	// CSV Column Headers
	public static final ArrayList<String> CSVTEAMOUTPUTHEADERS = getTeamDefDataMap();
	public static final String CSVTEAMSUMHEADERS = getTeamSumHeaders();
	public static final String CSVTEAMMATCHHEADERS = getTeamMatchHeaders();

	// FFS Stat Names
	public static final String FPLASSISTS = "Assists - Opta";
	public static final String FPLCS = "Clean Sheets";
	public static final String FPLGC = "Goals Conceded";
	public static final String FPLGS = "Goals";
	public static final String FPLSV = "Saves";
	public static final String FPLMINS = "Time Played - FPL";
	public static final String FSBPSHEADER = "BPS";
	public static final String FPLYCS = "Premier League Yellow Cards";
	public static final String FPLRCS = "Premier League Total Red Cards";
	public static final String FPLPENSVS = "Saves From Penalty";
	
	private static final ArrayList<Integer> getCalendarMonths (boolean firstHalf){
		ArrayList<Integer> retVal = new ArrayList<Integer>();

		if(firstHalf){
			retVal.add(new Integer(Calendar.AUGUST));
			retVal.add(new Integer(Calendar.SEPTEMBER));
			retVal.add(new Integer(Calendar.OCTOBER));
			retVal.add(new Integer(Calendar.NOVEMBER));
			retVal.add(new Integer(Calendar.DECEMBER));
		}
		else{
			retVal.add(new Integer(Calendar.JANUARY));
			retVal.add(new Integer(Calendar.FEBRUARY));
			retVal.add(new Integer(Calendar.MARCH));
			retVal.add(new Integer(Calendar.APRIL));
			retVal.add(new Integer(Calendar.MAY));
			retVal.add(new Integer(Calendar.JUNE));
			retVal.add(new Integer(Calendar.JULY));
		}
		return retVal;
	}

	private static final ArrayList<String> defineExcelEPLColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNPTS);
		retVal.add(EXCELCOLUMNFPLASS);
		retVal.add(EXCELCOLUMNBONUS);
		retVal.add(EXCELCOLUMNBPS);
		retVal.add(EXCELCOLUMNPRICE);
		retVal.add(EXCELCOLUMNCS);
		retVal.add(EXCELCOLUMNOG);

		return retVal;
	}

	private static final ArrayList<String> defineExcelUnderstatColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNUXG);
		retVal.add(EXCELCOLUMNUNPXG);
		retVal.add(EXCELCOLUMNUXA);

		return retVal;
	}

	private static final ArrayList<String> defineExcelFBRefColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNFBREFXG);
		retVal.add(EXCELCOLUMNFBREFNPXG);
		retVal.add(EXCELCOLUMNFBREFXA);

		return retVal;
	}
	
	private static ArrayList<String> getPercentageColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNSIBPERCENT);
		retVal.add(EXCELCOLUMN6YDPERCENT);
		retVal.add(EXCELCOLUMNGSHEADPERCENT);
		retVal.add(EXCELCOLUMNACC);
		retVal.add(EXCELCOLUMNCONV);
		retVal.add(EXCELCOLUMNASSPERCENT);
		retVal.add(EXCELCOLUMNBCCONV);
		retVal.add(EXCELCOLUMGSBCPERCENT);
		retVal.add(EXCELCOLUMNBCOPENCONV);
		retVal.add(EXCELCOLUMGIBPERCENT);
		retVal.add(EXCELCOLUMNGSOPENPERCENT);

		return retVal;
	}

	private static ArrayList<String> getWholeNumberColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNMINS);
		retVal.add(EXCELCOLUMNAPPS);
		retVal.add(EXCELCOLUMNPTS);
		retVal.add(EXCELCOLUMNGS);
		retVal.add(EXCELCOLUMNBONUS);
		retVal.add(EXCELCOLUMNBPS);
		retVal.add(EXCELCOLUMNBASEBPS);
		retVal.add(EXCELCOLUMNDEFCON);
		retVal.add(EXCELCOLUMNDEFCONPTS);
		retVal.add(EXCELCOLUMNPENTCH);
		retVal.add(EXCELCOLUMNSHOTS);
		retVal.add(EXCELCOLUMNNPSHOTS);
		retVal.add(EXCELCOLUMNSIB);
		retVal.add(EXCELCOLUMN6YD);
		retVal.add(EXCELCOLUMNSOT);
		retVal.add(EXCELCOLUMNGSHEAD);
		retVal.add(EXCELCOLUMNASS);
		retVal.add(EXCELCOLUMNFPLASS);
		retVal.add(EXCELCOLUMNKP);
		retVal.add(EXCELCOLUMNBCC);
		retVal.add(EXCELCOLUMNBC);
		retVal.add(EXCELCOLUMNBCMISS);
		retVal.add(EXCELCOLUMNGSBC);
		retVal.add(EXCELCOLUMNGSPK);
		retVal.add(EXCELCOLUMNPKMISS);
		retVal.add(EXCELCOLUMNBCOPEN);
		retVal.add(EXCELCOLUMNBCOPENMISS);
		retVal.add(EXCELCOLUMNGIB);
		retVal.add(EXCELCOLUMNGSOPEN);

		return retVal;
	}

	private static ArrayList<String> getHundrethsColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNPP90);
		retVal.add(EXCELCOLUMNPPAPP);
		retVal.add(EXCELCOLUMNGS90);
		retVal.add(EXCELCOLUMNBONUS90);
		retVal.add(EXCELCOLUMNBPS90);
		retVal.add(EXCELCOLUMNBASEBPS90);
		retVal.add(EXCELCOLUMNDEFCONPTSAPP);
		retVal.add(EXCELCOLUMNPENTCH90);
		retVal.add(EXCELCOLUMNSHOTS90);
		retVal.add(EXCELCOLUMNNPSHOTS90);
		retVal.add(EXCELCOLUMNSIB90);
		retVal.add(EXCELCOLUMNS6YD90);
		retVal.add(EXCELCOLUMNSOT90);
		retVal.add(EXCELCOLUMNNPXGSHOT);
		retVal.add(EXCELCOLUMNSHEAD90);
		retVal.add(EXCELCOLUMNASS90);
		retVal.add(EXCELCOLUMNFPLASS90);
		retVal.add(EXCELCOLUMNBCC90);
		retVal.add(EXCELCOLUMNKP90);
		retVal.add(EXCELCOLUMNBC90);
		retVal.add(EXCELCOLUMNBCMISS90);
		retVal.add(EXCELCOLUMNGSBC90);
		retVal.add(EXCELCOLUMNGIB90);
		retVal.add(EXCELCOLUMNGSOPEN90);
		retVal.add(EXCELCOLUMNICT90);
		retVal.add(EXCELCOLUMNXG);
		retVal.add(EXCELCOLUMNXA);
		retVal.add(EXCELCOLUMNXPTS);
		retVal.add(EXCELCOLUMNBASEXPTS);
		retVal.add(EXCELCOLUMNUXG);
		retVal.add(EXCELCOLUMNUNPXG);
		retVal.add(EXCELCOLUMNUXA);
		retVal.add(EXCELCOLUMNUXPTS);
		retVal.add(EXCELCOLUMNUXNPPTS);
		retVal.add(EXCELCOLUMNSBXG);
		retVal.add(EXCELCOLUMNSBNPXG);
		retVal.add(EXCELCOLUMNSBXA);
		retVal.add(EXCELCOLUMNSBXG90);
		retVal.add(EXCELCOLUMNSBNPXG90);
		retVal.add(EXCELCOLUMNSBXA90);
		retVal.add(EXCELCOLUMNSBXPTS90);
		retVal.add(EXCELCOLUMNSBNPXPTS90);
		retVal.add(EXCELCOLUMNFSXG);
		retVal.add(EXCELCOLUMNFSNPXG);
		retVal.add(EXCELCOLUMNFSXA);
		retVal.add(EXCELCOLUMNFSXG90);
		retVal.add(EXCELCOLUMNFSNPXG90);
		retVal.add(EXCELCOLUMNFSXA90);
		retVal.add(EXCELCOLUMNFSXPTS90);
		retVal.add(EXCELCOLUMNFSNPXPTS90);
		retVal.add(EXCELCOLUMNAVGXG);
		retVal.add(EXCELCOLUMNAVGNPXG);
		retVal.add(EXCELCOLUMNAVGXA);
		retVal.add(EXCELCOLUMNAVGXPTS);
		retVal.add(EXCELCOLUMNAVGNPXPTS);
		return retVal;
	}

	private static ArrayList<String> getTenthsColumns(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNPRICE);
		retVal.add(EXCELCOLUMNICT);
		retVal.add(EXCELCOLUMNGGOALINV);

		return retVal;
	}

	private static HashMap <String, String> getExcelPlayerStatTableMap(){
		HashMap <String, String> retVal = new HashMap<String, String> ();

		retVal.put(EXCELCOLUMNNAME,"Name");
		retVal.put(EXCELCOLUMNTEAM,"Team");
		retVal.put(EXCELCOLUMNBC,"Big Chances Total");
		retVal.put(EXCELCOLUMNBCMISS,"Big Chances Missed");
		retVal.put(EXCELCOLUMNKP,"Chances Created");
		retVal.put(EXCELCOLUMNGS,"Goals");
		retVal.put(EXCELCOLUMNGSPK,"Goals From Penalties");
		retVal.put(EXCELCOLUMNSHOTS,"Shots");
		retVal.put(EXCELCOLUMNNPSHOTS, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSIB,"Shots - Inside Box");
		retVal.put(EXCELCOLUMN6YD,"Shots - Six Yard Box");
		retVal.put(EXCELCOLUMNSOT,"Shots On Target");
		retVal.put(EXCELCOLUMNGSHEAD,"Headed Goal Attempts");
		retVal.put(EXCELCOLUMNBCC,"Big Chances Created");
		retVal.put(EXCELCOLUMNGGOALINV,"FPL Goal Involvement");
		retVal.put(EXCELCOLUMNICT,"ICT Index");
		retVal.put(EXCELCOLUMNASS,"Assists - Opta");
		retVal.put(EXCELCOLUMNGIB,"Goals From Inside Box");
		retVal.put(EXCELCOLUMNGSOPEN,"Goals From Open Play");
		retVal.put(EXCELCOLUMNPKMISS,"Penalties Missed");
		retVal.put(EXCELCOLUMNMINS,"Time Played - FPL");
		retVal.put(EXCELCOLUMNAPPS,"Appearances");
		retVal.put(EXCELCOLUMNPENTCH,"Touches - Penalty Area");
		retVal.put(EXCELCOLUMNDEFCON,"Defensive Contributions");
		retVal.put(EXCELCOLUMNDEFCONPTS,"Defensive Contribution Points");
		retVal.put(EXCELCOLUMNDEFCONPTSAPP,"Defensive Contributions Per Appearance");
		retVal.put(EXCELCOLUMNSIBPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMN6YDPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNACC, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNCONV, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGSHEADPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNASSPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGSBC, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBCCONV, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMGSBCPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBCOPEN, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBCOPENMISS, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBCOPENCONV, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMGIBPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGSOPENPERCENT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNPP90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNPPAPP, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNPENTCH90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSHOTS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNNPSHOTS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSIB90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNS6YD90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSOT90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNNPXGSHOT, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSHEAD90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNASS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNFPLASS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNKP90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSOT90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNICT90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBCC90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBC90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBCMISS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGSBC90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGIB90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNGSOPEN90, FORMULAIDENTIFIER);

		retVal.put(EXCELCOLUMNBONUS,"");
		retVal.put(EXCELCOLUMNBPS,"");
		retVal.put(EXCELCOLUMNBASEBPS, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBONUS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBPS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBASEBPS90, FORMULAIDENTIFIER);

		retVal.put(EXCELCOLUMNXG, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNXA, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNXPTS, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNBASEXPTS, FORMULAIDENTIFIER);

		retVal.put(EXCELCOLUMNUXG, "");
		retVal.put(EXCELCOLUMNUNPXG, "");
		retVal.put(EXCELCOLUMNUXA, "");
		retVal.put(EXCELCOLUMNUXPTS, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNUXNPPTS, FORMULAIDENTIFIER);

		retVal.put(EXCELCOLUMNFSXG, "xG Expected Goals");
		retVal.put(EXCELCOLUMNFSNPXG, "xG Non-Penalty");
		retVal.put(EXCELCOLUMNFSXA, "xA Expected Assists");
		retVal.put(EXCELCOLUMNFSXG90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNFSNPXG90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNFSXA90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNFSXPTS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNFSNPXPTS90, FORMULAIDENTIFIER);
		
		retVal.put(EXCELCOLUMNSBXG, "SB-xG Expected Goals");
		retVal.put(EXCELCOLUMNSBNPXG, "SB-xG Non-Penalty");
		retVal.put(EXCELCOLUMNSBXA, "SB-xG Assisted");	
		retVal.put(EXCELCOLUMNSBXG90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSBNPXG90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSBXA90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSBXPTS90, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNSBNPXPTS90, FORMULAIDENTIFIER);
		
		retVal.put(EXCELCOLUMNAVGXG, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNAVGNPXG, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNAVGXA, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNAVGXPTS, FORMULAIDENTIFIER);
		retVal.put(EXCELCOLUMNAVGNPXPTS, FORMULAIDENTIFIER);

		return retVal;
	}

	private static ArrayList <String> getDBPlayerStatList(){
		ArrayList <String> retVal = new ArrayList<String> ();

		retVal.add(FootballAnalysisConstants.FPLASSISTS);
		retVal.add(FootballAnalysisConstants.FPLCS);
		retVal.add(FootballAnalysisConstants.FPLGS);
		retVal.add(FootballAnalysisConstants.FPLSV);
		retVal.add(FootballAnalysisConstants.FPLMINS);
		retVal.add(FootballAnalysisConstants.FPLPENSVS);
		
		retVal.add("Appearances");
		retVal.add("Assists - Opta");
		retVal.add("Attempts From Set Plays");
		retVal.add("Big Chances Created");
		retVal.add("Big Chances Missed");
		retVal.add("Big Chances Scored");
		retVal.add("Big Chances Total");
		retVal.add("Bonus Points");
		retVal.add("BPS");
		retVal.add("BPS Baseline");
		retVal.add("CBI");
		retVal.add("Chances Created");
		retVal.add("Corners");
		retVal.add("FPL Goal Involvement");
		retVal.add("GAME_WEEK");
		retVal.add("Goal Attempts");
		retVal.add("Goal Involvement");
		retVal.add("Goals Conceded");
		retVal.add("Goals From Inside Box");
		retVal.add("Goals From Outside Box");
		retVal.add("Goals From Penalties");
		retVal.add("Goals From Set Plays");
		retVal.add("Headed Attempts From Set Plays");
		retVal.add("Headed Goal Attempts");
		retVal.add("Headed Goals");
		retVal.add("MATCH_DATE");
		retVal.add("MATCH_LOCATION");
		retVal.add("OPP_NAME");
		retVal.add("Penalties Won");
		retVal.add("PLAYER_NAME");
		retVal.add("PLAYER_POS");
		retVal.add("Shots - Inside Box");
		retVal.add("Shots Off Target");
		retVal.add("Shots On Target");
		retVal.add("Starts");
		retVal.add("Subbed Off");
		retVal.add("Subbed On");
		retVal.add("TEAM_NAME");
		retVal.add("Assists - Opta");
		retVal.add("Premier League Total Red Cards");
		retVal.add("Premier League Yellow Cards");
		retVal.add("Touches - Penalty Area");
		retVal.add("xA Expected Assists");
		retVal.add("xG Expected Goals");
		retVal.add("xGI Expected Goal Invovlement");

		return retVal;
	}

	private static ArrayList <String> getDBTeamStatList(){
		ArrayList <String> retVal = new ArrayList<String> ();

		retVal.add("Attempts From Direct Free-kick");
		retVal.add("Attempts From Set Plays");
		retVal.add("Bad Touches");
		retVal.add("Big Chances Scored");
		retVal.add("Big Chances Total");
		retVal.add("Cross Completion");
		retVal.add("Crosses");
		retVal.add("Dispossessed");
		retVal.add("Errors");
		retVal.add("Errors Leading to Goal");
		retVal.add("Goal Attempts");
		retVal.add("Goals");
		retVal.add("Goals From Direct Free-kick");
		retVal.add("Goals From Inside Box");
		retVal.add("Goals From Outside Box");
		retVal.add("Goals From Penalties");
		retVal.add("Goals From Set Plays");
		retVal.add("Passes");
		retVal.add("Passes - Final Third - Successful");
		retVal.add("Passes - Opponents Half - Successful");
		retVal.add("Passes - Successful");
		retVal.add("Possession");
		retVal.add("Possession - Final Third");
		retVal.add("Possession - Opponents Half");
		retVal.add("Shots - Inside Box");
		retVal.add("Shots Blocked");
		retVal.add("Shots On Target");
		retVal.add("xG Expected Goals");
		retVal.add("GAME_WEEK");
		retVal.add("MATCH_DATE");
		retVal.add("MATCH_LOCATION");
		retVal.add("OPP_NAME");
		retVal.add("TEAM_NAME");

		return retVal;
	}

	private static ArrayList<String> getAnalysisBlueColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add(EXCELCOLUMNNAME);
		retVal.add(EXCELCOLUMNTEAM);
		retVal.add(EXCELCOLUMNPRICE);
		retVal.add(EXCELCOLUMNMINS);
		retVal.add(EXCELCOLUMNAPPS);
		retVal.add(EXCELCOLUMNPTS);
		retVal.add(EXCELCOLUMNGS);
		retVal.add(EXCELCOLUMNBONUS);
		retVal.add(EXCELCOLUMNBPS);
		retVal.add(EXCELCOLUMNBASEBPS);
		retVal.add(EXCELCOLUMNOG);
		retVal.add(EXCELCOLUMNDEFCON);
		retVal.add(EXCELCOLUMNDEFCONPTS);
		retVal.add(EXCELCOLUMNCS);
		retVal.add(EXCELCOLUMNPENTCH);
		retVal.add(EXCELCOLUMNSHOTS);
		retVal.add(EXCELCOLUMNNPSHOTS);
		retVal.add(EXCELCOLUMNSIB);
		retVal.add(EXCELCOLUMNSIBPERCENT);
		retVal.add(EXCELCOLUMN6YD);
		retVal.add(EXCELCOLUMN6YDPERCENT);
		retVal.add(EXCELCOLUMNGSHEAD);
		retVal.add(EXCELCOLUMNGSHEADPERCENT);
		retVal.add(EXCELCOLUMNSOT);
		retVal.add(EXCELCOLUMNACC);
		retVal.add(EXCELCOLUMNCONV);
		retVal.add(EXCELCOLUMNASS);
		retVal.add(EXCELCOLUMNFPLASS);
		retVal.add(EXCELCOLUMNKP);
		retVal.add(EXCELCOLUMNASSPERCENT);
		retVal.add(EXCELCOLUMNICT);
		retVal.add(EXCELCOLUMNGGOALINV);
		retVal.add(EXCELCOLUMNBCC);
		retVal.add(EXCELCOLUMNBC);
		retVal.add(EXCELCOLUMNBCMISS);
		retVal.add(EXCELCOLUMNBCCONV);
		retVal.add(EXCELCOLUMNGSBC);
		retVal.add(EXCELCOLUMGSBCPERCENT);
		retVal.add(EXCELCOLUMNGSPK);
		retVal.add(EXCELCOLUMNPKMISS);
		retVal.add(EXCELCOLUMNBCOPEN);
		retVal.add(EXCELCOLUMNBCOPENMISS);
		retVal.add(EXCELCOLUMNBCOPENCONV);
		retVal.add(EXCELCOLUMNGIB);
		retVal.add(EXCELCOLUMGIBPERCENT);
		retVal.add(EXCELCOLUMNGSOPEN);
		retVal.add(EXCELCOLUMNGSOPENPERCENT);
		retVal.add(EXCELCOLUMNFSXG);
		retVal.add(EXCELCOLUMNFSNPXG);
		retVal.add(EXCELCOLUMNFSXA);
		retVal.add(EXCELCOLUMNSBXG);
		retVal.add(EXCELCOLUMNSBNPXG);
		retVal.add(EXCELCOLUMNSBXA);		

		return retVal;
	}

	private static ArrayList<String> getAnalysisRedColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();		

		retVal.add(EXCELCOLUMNPP90);
		retVal.add(EXCELCOLUMNPPAPP);
		retVal.add(EXCELCOLUMNGS90);
		retVal.add(EXCELCOLUMNBONUS90);
		retVal.add(EXCELCOLUMNBPS90);
		retVal.add(EXCELCOLUMNBASEBPS90);
		retVal.add(EXCELCOLUMNDEFCONPTSAPP);
		retVal.add(EXCELCOLUMNPENTCH90);
		retVal.add(EXCELCOLUMNSHOTS90);
		retVal.add(EXCELCOLUMNNPSHOTS90);
		retVal.add(EXCELCOLUMNSIB90);
		retVal.add(EXCELCOLUMNS6YD90);
		retVal.add(EXCELCOLUMNSHEAD90);
		retVal.add(EXCELCOLUMNSOT90);
		retVal.add(EXCELCOLUMNNPXGSHOT);
		retVal.add(EXCELCOLUMNASS90);
		retVal.add(EXCELCOLUMNFPLASS90);
		retVal.add(EXCELCOLUMNBCC90);
		retVal.add(EXCELCOLUMNKP90);
		retVal.add(EXCELCOLUMNBC90);
		retVal.add(EXCELCOLUMNBCMISS90);
		retVal.add(EXCELCOLUMNGSBC90);
		retVal.add(EXCELCOLUMNGIB90);
		retVal.add(EXCELCOLUMNGSOPEN90);
		retVal.add(EXCELCOLUMNICT90);

		return retVal;
	}

	private static ArrayList<String> getAnalysisGreenColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();		

		retVal.add(EXCELCOLUMNXG);
		retVal.add(EXCELCOLUMNXA);
		retVal.add(EXCELCOLUMNXPTS);
		retVal.add(EXCELCOLUMNBASEXPTS);

		return retVal;
	}

	private static ArrayList<String> getAnalysisOrangeColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();		

		retVal.add(EXCELCOLUMNUXG);
		retVal.add(EXCELCOLUMNUNPXG);
		retVal.add(EXCELCOLUMNUXA);
		retVal.add(EXCELCOLUMNUXPTS);
		retVal.add(EXCELCOLUMNUXNPPTS);

		return retVal;
	}

	private static ArrayList<String> getAnalysisBrownColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();		

		retVal.add(EXCELCOLUMNFSXG90);
		retVal.add(EXCELCOLUMNFSNPXG90);
		retVal.add(EXCELCOLUMNFSXA90);
		retVal.add(EXCELCOLUMNFSXPTS90);
		retVal.add(EXCELCOLUMNFSNPXPTS90);

		return retVal;
	}
	
	private static ArrayList<String> getAnalysisPurpleColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();		

		retVal.add(EXCELCOLUMNSBXG90);
		retVal.add(EXCELCOLUMNSBNPXG90);
		retVal.add(EXCELCOLUMNSBXA90);
		retVal.add(EXCELCOLUMNSBXPTS90);
		retVal.add(EXCELCOLUMNSBNPXPTS90);
		
		return retVal;
	}

	private static ArrayList<String> getAnalysisDeepBlueColumnHeaders(){
		ArrayList<String> retVal = new ArrayList<String>();		

		retVal.add(EXCELCOLUMNAVGXG);
		retVal.add(EXCELCOLUMNAVGNPXG);
		retVal.add(EXCELCOLUMNAVGXA);
		retVal.add(EXCELCOLUMNAVGXPTS);
		retVal.add(EXCELCOLUMNAVGNPXPTS);

		return retVal;
	}
	
	private static String getTeamMatchHeaders(){
		return "Team,"
				+ "Opp,"
				+ "Location,"
				+ "Gameweek,"
				+ "Date" +
				getTeamDefDataMapString(true);
	}

	private static String getTeamSumHeaders(){
		return "Team" 
				+ getTeamDefDataMapString(false)
				+ ",UxG,UxGA";
	}

	private static String getTeamDefDataMapString(boolean oppGamePlayedToggle){
		String retVal = "";

		Iterator<String> iterator = getTeamDefDataMap().iterator();
		String oppHeaders = "";
		String mainHeaders = "";
		String temp = "";

		while(iterator.hasNext()){
			temp = iterator.next();

			if (oppGamePlayedToggle || (!oppGamePlayedToggle && !temp.equals("Games Played"))){
				oppHeaders = oppHeaders + ",OPP " + temp;
			}

			mainHeaders = mainHeaders + ", " + temp;
		}

		retVal = retVal + oppHeaders + mainHeaders;

		return retVal;
	}

	private static ArrayList<String> getTeamDefDataMap(){
		ArrayList<String> retVal = new ArrayList<String>();

		retVal.add("Passes - Final Third - Successful");
		retVal.add("Touches - Penalty Area");
		retVal.add("Goal Attempts");
		retVal.add("Shots - Inside Box");
		retVal.add("Big Chances Total");
		retVal.add("Shots On Target");
		retVal.add("xG Expected Goals");
		retVal.add("Goals");
		retVal.add("Games Played");

		return retVal;
	}

	public static String mapUnderstatTeamName(String fsTeamName){
		switch(fsTeamName){
			case "Brighton and Hove Albion":
				return "Brighton";
	
			case "Cardiff City":
				return "Cardiff";
	
			case "Huddersfield Town":
				return "Huddersfield";
				
			case "Ipswich Town":
				return "Ipswich";
			
			case "Leeds United":
				return "Leeds";
			
			case "Leicester City":
				return "Leicester";
			
			case "Luton Town":
				return "Luton";
	
			case "Norwich City":
				return "Norwich";
	
			case "Tottenham Hotspur":
				return "Tottenham";
	
			case "West Ham United":
				return "West Ham";
	
			default:
				return fsTeamName;
		}
	}
	
	public static String mapFBRefTeamName(String fsTeamName){
		switch(fsTeamName){
			case "Brighton and Hove Albion":
				return "Brighton";
			
			case "Manchester United":
				return "Manchester Utd";
			
			case "Newcastle United":
				return "Newcastle Utd";
			
			case "Nottingham Forest":
				return "Nott\\'ham Forest";
			
			case "Sheffield United":
				return "Sheffield Utd";
			
			case "Tottenham Hotspur":
				return "Tottenham";
	
			case "West Bromwich Albion":
				return "West Brom";
	
			case "West Ham United":
					return "West Ham";
	
			case "Wolverhampton Wanderers":
				return "Wolves";
	
			default:
				return fsTeamName;
		}
	}

	private static ArrayList<java.util.Date> getGameweekDates(){
		ArrayList<java.util.Date> retVal = new ArrayList<java.util.Date>();
		
		try{
			ArrayList<String> fileData = FootballAnalysisUtil.getFileDataByLine(FootballAnalysisConstants.FPL_GW_MAP);
			
			for (int i = 0; i < fileData.size(); i++){
				String[]tempStrings = fileData.get(i).split(",");
				
				if (tempStrings.length != 2) throw new Exception("Gameweek Date format incorrect in " + FootballAnalysisConstants.FPL_GW_MAP);
				retVal.add(new java.util.Date(Long.parseLong(tempStrings[1])));
			}
		}
		catch(Exception e){
			System.err.println("In FootballAnalysisConstants.getGameweekDates(): " + e.toString());
		}
		
		retVal.sort(null);
		return retVal;
	}
	
	public static String getFFTeamMap(String longTeamName){
		switch(longTeamName){
			case "Arsenal":
				return "ARS";

			case "Aston Villa":
				return "AVL";

			case "Brentford":
				return "BRE";

			case "Brighton and Hove Albion":
				return "BHA";

			case "Bournemouth":
				return "BOU";

			case "Burnley":
				return "BUR";

			case "Chelsea":
				return "CHE";

			case "Crystal Palace":
				return "CRY";

			case "Everton":
				return "EVE";

			case "Fulham":
				return "FUL";

			case "Leeds United":
				return "LEE";

			case "Liverpool":
				return "LIV";

			case "Manchester United":
				return "MUN";

			case "Manchester City":
				return "MCI";

			case "Newcastle United":
				return "NEW";

			case "Nottingham Forest":
				return "NFO";

			case "Sunderland":
				return "SUN";

			case "Tottenham Hotspur":
				return "TOT";

			case "West Ham United":
				return "WHU";

			case "Wolverhampton Wanderers":
				return "WOL";

			default:
				return "";
		}
	}
}