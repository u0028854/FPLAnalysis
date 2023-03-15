package baker.soccer;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.Vector;

import org.apache.commons.text.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.HasSiblingFilter;
import org.htmlparser.filters.NotFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.RegexFilter;
import org.htmlparser.filters.StringFilter;
import org.htmlparser.filters.TagNameFilter;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import baker.soccer.fbref.FBRefAction;
import baker.soccer.fbref.objects.FBRefPlayerObject;
import baker.soccer.fbref.objects.FBRefTeamObject;
import baker.soccer.fpl.FPLUtil;
import baker.soccer.fpl.objects.FPLPlayerObject;
import baker.soccer.fs.objects.FSMatchReturnObject;
import baker.soccer.fs.objects.FSPlayerMatch;
import baker.soccer.fs.objects.FSPlayerObject;
import baker.soccer.fs.objects.FSTeamMatch;
import baker.soccer.fs.objects.FSTeamObject;
import baker.soccer.fs.objects.FSTeamTableObject;
import baker.soccer.understat.UnderstatAction;
import baker.soccer.understat.objects.UnderstatPlayerObject;
import baker.soccer.understat.objects.UnderstatTeamObject;
import baker.soccer.util.ExcelOutputUtil;
import baker.soccer.util.FootballAnalysisConstants;
import baker.soccer.util.FootballAnalysisUtil;
import baker.soccer.util.objects.ExcelCellObject;
import baker.soccer.util.objects.FPLFSMapObject;

/*****
 * Each year:
 * 
 * Change paths on batch files
 * Add macth numbers to batch file config
 * Change team names in EPLUtil.mapTeams(int teamID)
 * 
 * To add new columns:
 * Add an EXCELCOLUMN**** name to FootballAnalysisConstants
 * Add that column to EPL (defineExcelEPLColumns()), Understat (defineExcelUnderstatColumns())if needed
 * Add that column to appropriate number type (getPercentageColumns, getWholeNumberColumns, getHundrethsColumns, getTenthsColumns)
 * Add that column to the getPlayerStatTableMap() with appropriate data name based upon source of data (EPL, FPL, Understat) or formula
 * Add that column to getAnalysisBlueColumnHeaders(), getAnalysisRedColumnHeaders(), getAnalysisGreenColumnHeaders(), getAnalysisBrownColumnHeaders(), getAnalysisOrangColumnHeaders()
 *
 *If FPL data changes names:
 *Find EXCELCOLUMN**** and change value in getPlayerStatTableMap() that it maps to
 *For Team stats, change name in getTeamDefDataMap()
 *
 */

public class FootballAnalysisAction {
	public static void main(String [] args) throws Exception{
		String option = args[0];
		String years = args[1];
		String matchPlayersOption = (args.length < 3 ? "" : args[2]);
		String gameLocationOption = (args.length < 4 ? "" : args[3]);
		String[] yearsArray = args.length > 4 ? Arrays.copyOfRange(args, 4, args.length) : null;

		if(option.equalsIgnoreCase(FootballAnalysisConstants.PROCESS_FS_PLAYERMATCH_ARG)){
			try{
				/*
				 * Arguments:
				 * option = FootballAnalysisConstants.PROCESS_FS_PLAYERMATCH_ARG
				 * years = Years seperated by a pipe delimiter
				 * matchPlayersOption = FootballAnalysisConstants.FS_MATCH_TEAM_ONLY or FootballAnalysisConstants.FS_MATCH_PLAYERS_ONLY to select
				 * team or players
				 */

				MongoClient mongoClient = new MongoClient(FootballAnalysisConstants.MONGODBHOSTNAME , FootballAnalysisConstants.MONGODBHOSTPORT ); 	
				MongoDatabase db = mongoClient.getDatabase(FootballAnalysisConstants.MONGODATABASENAME);
				MongoCollection<Document> coll = db.getCollection(FootballAnalysisConstants.MONGOPLAYERCOLLECTIONNAME);

				String[] fsYears;
				
				if(years.contains("|")){
					fsYears = years.split("\\|");
				}
				else{
					fsYears = new String[1];
					fsYears[0] = years;
				}

				for (int i=0; i<fsYears.length; i++){
					HashMap<String, FSPlayerMatch> playerHash = processMatchFilesController(fsYears[i], matchPlayersOption).getPlayerObjects();
					Iterator<String> playerIterator = playerHash.keySet().iterator();

					ArrayList<Document> inserts = new ArrayList<Document>();

					while (playerIterator.hasNext()){
						HashMap<String, String> valueHash = playerHash.get(playerIterator.next()).getDatabaseInsertValues();

						Iterator<String> valueIterator = valueHash.keySet().iterator();
						
						Document tempDoc = new Document("_id", new ObjectId());

						//String outputJSON = "{";		

						while(valueIterator.hasNext()){
							String valueKey = valueIterator.next();
							String tempValue = valueHash.get(valueKey);
							
							try{
								tempDoc.append(valueKey, Double.parseDouble(tempValue));
							}
							catch(NumberFormatException e){
								tempDoc.append(valueKey, tempValue);
							}
							catch(NullPointerException e){
								tempDoc.append(valueKey, tempValue);
							}
							//outputJSON += (!outputJSON.equals("{") ? "," : "") + "\"" + valueKey + "\":\"" + valueHash.get(valueKey) + "\"";
						}
						
						
						//outputJSON += "}";
						//inserts.add(Document.parse(outputJSON));
						inserts.add(tempDoc);
						
					}
					coll.insertMany(inserts);
				}

				mongoClient.close();
			}
			catch(Exception e){
				throw new Exception(e);
			}
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.PROCESS_FS_TEAM_MATCH_ARG)){
			/*
			 * Arguments:
			 * option = FootballAnalysisConstants.PROCESS_FS_TEAM_MATCH_ARG
			 * years = Years seperated by a pipe delimiter
			 * matchPlayersOption = FootballAnalysisConstants.FS_MATCH_TEAM_ONLY or FootballAnalysisConstants.FS_MATCH_PLAYERS_ONLY to select
			 * team or players
			 */
			try{
				MongoClient mongoClient = new MongoClient(FootballAnalysisConstants.MONGODBHOSTNAME , FootballAnalysisConstants.MONGODBHOSTPORT ); 	
				MongoDatabase db = mongoClient.getDatabase(FootballAnalysisConstants.MONGODATABASENAME);
				MongoCollection<Document> coll = db.getCollection(FootballAnalysisConstants.MONGOTEAMCOLLECTIONNAME);

				String[] fsYears;

				if(years.contains("|")){
					fsYears = years.split("\\|");
				}
				else{
					fsYears = new String[1];
					fsYears[0] = years;
				}

				for (int i=0; i<fsYears.length; i++){
					HashMap<String, FSTeamObject> teamHash = processMatchFilesController(fsYears[i], matchPlayersOption).getTeamObjects();
					
					Iterator<String> teamIterator = teamHash.keySet().iterator();

					while (teamIterator.hasNext()){
						ArrayList<Document> inserts = new ArrayList<Document>();

						HashSet<FSTeamMatch> teamMatchHash = teamHash.get(teamIterator.next()).getMatchValues();
						Iterator<FSTeamMatch> teamMatchHashIterator = teamMatchHash.iterator();

						while(teamMatchHashIterator.hasNext()){
							HashMap<String, String> valueHash = teamMatchHashIterator.next().getDatabaseInsertValues();
							Iterator<String> valueIterator = valueHash.keySet().iterator();

							String outputJSON = "{";

							while(valueIterator.hasNext()){
								String valueKey = valueIterator.next();
								outputJSON += (!outputJSON.equals("{") ? "," : "") + "\"" + valueKey + "\":\"" + valueHash.get(valueKey) + "\"";
							}
							outputJSON += "}";
							inserts.add(Document.parse(outputJSON));
						}
						coll.insertMany(inserts);
					}
				}

				mongoClient.close();
			}
			catch(Exception e){
				throw new Exception(e);
			}
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.RENAME_FILES_ARG)){
			if (yearsArray == null) throw new Exception ("Option = " + option + " yearsArray is null");
			else{
				for (int i=0; i < yearsArray.length; i++){
					processRenameMatchFiles(FootballAnalysisConstants.MATCHES_BASEDIR + "\\" + yearsArray[i] + "\\");
				}
			}
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_ARG) || option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG) || option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_4GW_ARG)){
			String excelOutputFile = option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_ARG) ? 
					FootballAnalysisConstants.STATS_TABLE_OUTPUT_FILE : 
						(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG) ? 
								FootballAnalysisConstants.STATS_TABLE_OUTPUT_6GW_FILE 
								: FootballAnalysisConstants.STATS_TABLE_OUTPUT_4GW_FILE);

			HashMap<String, HashMap<String, FSPlayerObject>> fsPlayerData = new HashMap<String, HashMap<String, FSPlayerObject>>();
for (int i=0; i < FootballAnalysisConstants.PLAYER_POSITIONS.length; i++){
				fsPlayerData.put(FootballAnalysisConstants.PLAYER_POSITIONS[i], processFantScoutStatTables(FootballAnalysisConstants.PLAYER_POSITIONS[i]));
			}

			// Until determine how to parameterize years, use the year fomr command line
			exportPlayerAnalysisExcel(fsPlayerData, FPLUtil.processEPLPlayerSeasonAction(option), UnderstatAction.processUnderstatXGPlayerJSON(Integer.parseInt(years), option), FBRefAction.processFBRefPlayerHTML(), buildFPLFSPlayerMap(FootballAnalysisUtil.getFileDataByLine(FootballAnalysisConstants.FPL_FS_PLAYER_MAP)), excelOutputFile);
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_TEAM_MATCH_ANALYSIS)){
			String gameWeeks = args.length != 3 ? "" : args[2];
			
			exportTeamTableData(processFantScoutTeamStatTablesWorker(FootballAnalysisConstants.TEAM_STATS_TABLE1), UnderstatAction.processUnderstatXGTeamJSON(), FBRefAction.processFBRefTeamHTML(), FootballAnalysisConstants.EPL_BASE_DIR + "\\" + years + "teamtable" + gameWeeks + ".csv");
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_TEAM_ANALYSIS)){
			/*
			 * years = single year where match files are held
			 * matchPlayerOption = FootballAnalysisConstants.FS_MATCH_TEAM_ONLY or FootballAnalysisConstants.FS_MATCH_PLAYERS_ONLY to select
			 * gameLocationOption = FootballAnalysisConstants.AWAY_ONLY or FootballAnalysisConstants.HOME_ONLY. If any other optionn defaults to all
			 */
			
			/*
			 * 
			 * NEED TO BE REDONE
			 * 
			 * 		
			int gameWeekOption = -1;
			
			if(args.length == 5){
				try{
					int currentGameWeek;
					int numberOfWeeks;
					Integer.parseInt(args[4]);
					Scanner scan= new Scanner(System.in);

					System.out.println("Current game week?");
					currentGameWeek = scan.nextInt();

					System.out.println("Number of weeks?");
					numberOfWeeks = scan.nextInt();

					gameWeekOption = currentGameWeek - numberOfWeeks + 1;
					System.out.println(gameWeekOption);
					scan.close();
				}
				catch(NumberFormatException e){System.out.println(e);}
			}
*/
			processRenameMatchFiles(FootballAnalysisConstants.MATCHES_BASEDIR + "\\" + years + "\\");
			exportTeamData(processMatchFilesController(years, matchPlayersOption), FootballAnalysisConstants.EPL_BASE_DIR + "\\" + years + ".csv", FootballAnalysisConstants.EPL_BASE_DIR + "\\" + years + "sum.csv", gameLocationOption, UnderstatAction.processUnderstatXGTeamJSON());
		}
		else{
			throw new Exception("Illegal command line argument: " + option);
		}
	}

	private static void exportTeamTableData(HashMap<String, FSTeamTableObject> teamData, HashMap<String, UnderstatTeamObject> understatTeamData, HashMap<String, FBRefTeamObject> fbRefTeamData, String fileName) throws Exception {
		String fileData = "Team,OPP Big Chances,OPP ShotsOnTarget,OPP Goals,OPP xG,Big Chances,ShotsOnTarget,Goals,xG,Games Played, UStat xG, UStat OPP xG, FBRef xG, FBRef OPP xG\n"; 
		
		Iterator<String> iterator = teamData.keySet().iterator();
		
		while(iterator.hasNext()){
			String tempStr = iterator.next();
			fileData += teamData.get(tempStr).csvOutput();

			UnderstatTeamObject tempUnderstatTeam = understatTeamData.get(FootballAnalysisConstants.mapUnderstatTeamName(tempStr));
			FBRefTeamObject tempFBRefTeam = fbRefTeamData.get(FootballAnalysisConstants.mapFBRefTeamName(tempStr));
			fileData += "," + tempUnderstatTeam.sumXG() + "," + tempUnderstatTeam.sumXGA() + "," + (tempFBRefTeam == null ? 0 : tempFBRefTeam.getxG()) + "," + (tempFBRefTeam == null ? 0 : tempFBRefTeam.getxGC()) + "\n";
		}
		FootballAnalysisUtil.writeFile(fileName, fileData);
	}
	
	private static void processRenameMatchFiles(String baseFileDirectory) throws Exception{
		Vector<String> fileList = FootballAnalysisUtil.getFiles(baseFileDirectory, true);

		for (int i = 0; i < fileList.size(); i++){
			FootballAnalysisUtil.renameFile((String)fileList.elementAt(i), getFSMatchFileRename((String)fileList.elementAt(i)), true);
		}
	}

	private static FSMatchReturnObject processMatchFilesController(String value, String singleType) throws Exception{
		// Get files to be processed from disk
		Vector<String> seasonFiles = getSeasonFilesController(value);

		// Initialize team and player objects
		HashMap<String, FSTeamObject> teamObjects = new HashMap<String, FSTeamObject>();
		HashMap<String, FSPlayerMatch> playerMatches = new HashMap<String, FSPlayerMatch>();

		System.out.println("Processing " + seasonFiles.size() + " match files");

		if (seasonFiles != null){
			for (int i = 0; i < seasonFiles.size(); i++){
				// Open file for Parsing
				Parser parser = new Parser(seasonFiles.elementAt(i));
				parser.setEncoding("UTF-8");

				//Get match Date
				Calendar matchDate = getMatchDate(parser);

				// Get Gameweek
				String gameWeek = getGameWeek(parser);

				// Check third argument for options. Can do players, team or both
				if (singleType.equals(FootballAnalysisConstants.FS_MATCH_TEAM_ONLY)){
					System.out.println("Getting team data only");

					teamObjects = addTeamHashData(teamObjects, getFFSMatchTeamData(parser, matchDate, gameWeek));
				}
				else if (singleType.equals(FootballAnalysisConstants.FS_MATCH_PLAYERS_ONLY)){
					System.out.println("Getting player data only");

					playerMatches = getFFSMatchPlayerData(playerMatches, parser, matchDate, gameWeek);
				}
				else{
					throw new Exception("Illegal argument for match option: " + singleType);
				}
			}
		}

		FSMatchReturnObject retVal = new FSMatchReturnObject();

		retVal.setTeamObjects(teamObjects);
		retVal.setPlayerObjects(playerMatches);

		return retVal;
	}

	private static HashMap<String, FSPlayerObject> processFantScoutStatTables(String posOption) throws Exception{
		HashMap<String, FSPlayerObject> retVal = new HashMap<String, FSPlayerObject>();
		HashMap<String, FSPlayerObject> tempPlayerData1 = new HashMap<String, FSPlayerObject>();
		HashMap<String, FSPlayerObject> tempPlayerData2 = new HashMap<String, FSPlayerObject>();

		switch (posOption){
		case "FWD":
			retVal = processFantScoutStatTablesWorker(FootballAnalysisConstants.FWD_STATS_TABLE1);
			tempPlayerData1 = processFantScoutStatTablesWorker(FootballAnalysisConstants.FWD_STATS_TABLE2);
			tempPlayerData2 = processFantScoutStatTablesWorker(FootballAnalysisConstants.FWD_STATS_TABLE3);
			break;

		case "MID":
			retVal = processFantScoutStatTablesWorker(FootballAnalysisConstants.MID_STATS_TABLE1);
			tempPlayerData1 = processFantScoutStatTablesWorker(FootballAnalysisConstants.MID_STATS_TABLE2);
			tempPlayerData2 = processFantScoutStatTablesWorker(FootballAnalysisConstants.MID_STATS_TABLE3);
			break;

		case "DEF":
			retVal = processFantScoutStatTablesWorker(FootballAnalysisConstants.DEF_STATS_TABLE1);
			tempPlayerData1 = processFantScoutStatTablesWorker(FootballAnalysisConstants.DEF_STATS_TABLE2);
			tempPlayerData2 = processFantScoutStatTablesWorker(FootballAnalysisConstants.DEF_STATS_TABLE3);
			break;
		}

		Iterator<String> retValIterator = retVal.keySet().iterator();

		while (retValIterator.hasNext()){
			String next = retValIterator.next();

			FSPlayerObject temp1 = retVal.get(next);
			FSPlayerObject temp2 = tempPlayerData1.get(next);
			FSPlayerObject temp3 = tempPlayerData2.get(next);

			// Check to see if both result sets have same player.  If so, combine values into retVal.
			if(temp2 != null){
				temp1.addStatTableData(temp2.getStatTableData());
			}

			if(temp3 != null){
				temp1.addStatTableData(temp3.getStatTableData());
			}

			retVal.put(next, temp1);
		}

		return retVal;
	}

	private static HashMap<String, FSPlayerObject> processFantScoutStatTablesWorker(String fileName) throws Exception{
		HashMap<String, FSPlayerObject> retVal = new HashMap<String, FSPlayerObject>();

		Parser parser = new Parser (fileName);
		parser.setEncoding("UTF-8");

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		NodeFilter [] tempAndFilterArray = {new HasParentFilter(new CssSelectorNodeFilter("table[class=\"stats draggable fixed3\"]"), true), new TagNameFilter("tr"), new NotFilter(new HasChildFilter(new TagNameFilter("th"), true))};
		nodes = parser.parse(new AndFilter(tempAndFilterArray));

		for (int i = 0; i < nodes.size(); i++){
			// Create data objects
			FSPlayerObject playerObject = new FSPlayerObject();
			HashMap<String,String> playerValues = playerObject.getStatTableData();

			org.htmlparser.util.NodeList tempNodeList = new org.htmlparser.util.NodeList();
			nodes.elementAt(i).collectInto(tempNodeList, new CssSelectorNodeFilter("td[class=\"nowrap\"]"));

			playerValues.put("Team",tempNodeList.elementAt(0).getFirstChild().getNextSibling().getNextSibling().getText().trim());

			org.htmlparser.util.NodeList tempNodeList2 = new org.htmlparser.util.NodeList();

			nodes.elementAt(i).collectInto(tempNodeList2, new HasAttributeFilter("title"));

			for (int j = 0; j < tempNodeList2.size(); j++){
				// Get the nodes and value of attribute "title"
				Tag statElement = (Tag)tempNodeList2.elementAt(j);
				String tempAttrValue = StringEscapeUtils.unescapeHtml4(statElement.getAttribute("title"));

				// If player name not created, create it
				if(j == 0 && (playerObject.getName() == null || playerObject.getName().equals(""))){
					playerObject.setName(tempAttrValue);
				}
				// Add stat key/value pair to Hash
				else{
					String [] tempAttrPair = tempAttrValue.split(":");
					playerValues.put(tempAttrPair[0].trim(),tempAttrPair[1].trim());
				}
			}

			playerObject.setStatTableData(playerValues);
			retVal.put(playerObject.getName(), playerObject);
		}

		parser.reset();

		return retVal;
	}
	
	private static HashMap<String, FSTeamTableObject> processFantScoutTeamStatTablesWorker(String fileName) throws Exception{
		HashMap<String, FSTeamTableObject> retVal = new HashMap<String, FSTeamTableObject>();

		Parser parser = new Parser (fileName);
		parser.setEncoding("UTF-8");

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		NodeFilter [] tempAndFilterArray = {new HasParentFilter(new CssSelectorNodeFilter("table[class=\"stats draggable fixed1\"]"), true), new TagNameFilter("tr"), new NotFilter(new HasChildFilter(new TagNameFilter("th"), true))};
		nodes = parser.parse(new AndFilter(tempAndFilterArray));

		for (int i = 0; i < nodes.size(); i++){
			// Create data objects
			FSTeamTableObject teamObject = new FSTeamTableObject();
			
			org.htmlparser.util.NodeList tempNodeList = new org.htmlparser.util.NodeList();
			nodes.elementAt(i).collectInto(tempNodeList, new CssSelectorNodeFilter("td[class=\"first nowrap\"]"));
			
			teamObject.setTeamName(((Tag)tempNodeList.elementAt(0)).getAttribute("title"));

			org.htmlparser.util.NodeList tempNodeList2 = new org.htmlparser.util.NodeList();

			nodes.elementAt(i).collectInto(tempNodeList2, new AndFilter(new HasAttributeFilter("title"), new NotFilter(new HasAttributeFilter("class"))));

			for (int j = 0; j < tempNodeList2.size(); j++){
				// Get the nodes and value of attribute "title"
				Tag statElement = (Tag)tempNodeList2.elementAt(j);
				String tempAttrValue = StringEscapeUtils.unescapeHtml4(statElement.getAttribute("title"));

				String [] tempAttrPair = tempAttrValue.split(":");
				
				switch(tempAttrPair[0]){
					case "Big Chances Conceded":
						teamObject.setBigChancesConceded(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "Big Chances Total":
						teamObject.setBigChances(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "Games Played":
						teamObject.setGamesPlayed(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "Goals":
						teamObject.setGoalsScored(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "Goals Conceded":
						teamObject.setGoalsConceded(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "Shots On Target":
						teamObject.setShotsOnTarget(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "Shots On Target Conceded":
						teamObject.setShotsOnTargetConceded(Integer.parseInt(tempAttrPair[1].trim()));
						break;
					
					case "xG Expected Goals":
						teamObject.setxG(Float.parseFloat(tempAttrPair[1].trim()));
						break;
					
					case "xG Conceded":
						teamObject.setxGC(Float.parseFloat(tempAttrPair[1].trim()));
						break;
					
					default:
						throw new Exception("in processFantScoutTeamStatTablesWorker bad case: " + tempAttrPair[0].trim());
				}
			}

			retVal.put(teamObject.getTeamName(), teamObject);
		}

		parser.reset();

		return retVal;
	}

	private static HashMap<String, FSTeamObject> addTeamHashData(HashMap<String, FSTeamObject> baseHash,  HashMap<String, FSTeamObject> addHash){
		HashMap<String, FSTeamObject> retVal = baseHash;
		Iterator<String> addIterator = addHash.keySet().iterator();

		while (addIterator.hasNext()){
			FSTeamObject newTeam = addHash.get(addIterator.next());

			if(baseHash.containsKey(newTeam.getTeamName())){
				FSTeamObject oldTeam = baseHash.get(newTeam.getTeamName());

				oldTeam.addAllMatchValues(newTeam.getMatchValues());

				retVal.put(oldTeam.getTeamName(), oldTeam);
			}
			else{
				retVal.put(newTeam.getTeamName(), newTeam);
			}
		}

		return retVal;
	}

	/*
	Returns a Vector of file handlers based upon the OPTION variable passed into it. VALUE variable is year being pulled for 
	OPTION = FootballAnalysisConstants.FS_MATCH_GET_ALL_YEAR_ARG
	 */

	private static Vector <String> getSeasonFilesController(String value) throws Exception{
		Vector<String> retVal = new Vector<String> ();

		retVal = FootballAnalysisUtil.getFiles(FootballAnalysisConstants.MATCHES_BASEDIR + "\\" + value + "\\", false);

		return retVal;
	}

	/*
	 * Helper method to get Match Date from a match file. Takes reference to PARSER opened on valid FFS match file.
	 */
	private static Calendar getMatchDate(Parser parser) throws Exception{
		Calendar matchDate = Calendar.getInstance();

		String [] matchDateStrings = parser.parse(new HasParentFilter(new HasAttributeFilter("class", "date"))).elementAt(0).getText().split(" "); 
		matchDate.set(Integer.parseInt(matchDateStrings[2].trim()), FootballAnalysisUtil.getMonthNumber(matchDateStrings[1].trim()), Integer.parseInt(matchDateStrings[0].trim().replaceAll("[a-zA-Z]", "")));

		parser.reset();

		return matchDate;
	}

	/*
	 * Helper method to get Gameweek from a match file. Takes reference to PARSER opened on valid FFS match file.
	 */
	private static String getGameWeek(Parser parser) throws Exception{
		String gameWeek;

		// Get Game Week
		String tempGameWeek = (parser.parse(new StringFilter("gameweek")).elementAt(0).getText().split("gameweek ")[1].trim());
		gameWeek = tempGameWeek.substring(0, tempGameWeek.length() - 1);

		parser.reset();

		return gameWeek;
	}

	private static HashMap<String, FSTeamObject> getFFSMatchTeamData(Parser parser, Calendar matchDate, String gameWeek) throws Exception{
		HashMap<String, FSTeamObject> teamObjects = new HashMap<String, FSTeamObject>();

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		// Setup HashMaps for teams.  Main Key is team name.  Each set map has a hashmap of data values, with the 
		// field names being key values
		HashMap<String, FSTeamMatch> teamObjectsHash = new HashMap<String, FSTeamMatch> ();

		// Get Team Stats
		// Look for all nodes that have an ancestor with id = team-tabs-1:8
		NodeFilter [] subTeamFilterArray = {new HasParentFilter(new HasAttributeFilter("id", "team-tabs-1"), true), new HasParentFilter(new HasAttributeFilter("id", "team-tabs-2"), true),
				new HasParentFilter(new HasAttributeFilter("id", "team-tabs-3"), true), new HasParentFilter(new HasAttributeFilter("id", "team-tabs-4"), true),
				new HasParentFilter(new HasAttributeFilter("id", "team-tabs-5"), true), new HasParentFilter(new HasAttributeFilter("id", "team-tabs-6"), true),
				new HasParentFilter(new HasAttributeFilter("id", "team-tabs-7"), true), new HasParentFilter(new HasAttributeFilter("id", "team-tabs-8"), true)};

		// Get only those nodes with a td child of "first nowrap" class
		NodeFilter [] teamFilterArray = {new OrFilter(subTeamFilterArray), new HasChildFilter(new CssSelectorNodeFilter("td[class=\"first nowrap\"]"))};

		nodes = parser.parse(new AndFilter(teamFilterArray));

		for (int i = 0; i < nodes.size(); i++){
			// Create data objects
			FSTeamMatch teamStats = new FSTeamMatch();

			teamStats.setMatchDate(matchDate.getTime());
			teamStats.setGameWeek(gameWeek);

			// Filter nodes to return only those with attribute "title"
			org.htmlparser.util.NodeList teamNodes = new org.htmlparser.util.NodeList();
			nodes.elementAt(i).collectInto(teamNodes, new HasAttributeFilter("title"));

			for (int j = 0; j < teamNodes.size(); j++){
				// Get the nodes and value of attribute "title"
				Tag statElement = (Tag)teamNodes.elementAt(j);
				String tempAttrValue = StringEscapeUtils.unescapeHtml4(statElement.getAttribute("title"));

				// If team name not created, create it
				if(j == 0 && (teamStats.getTeamName() == null || teamStats.getTeamName().equals(""))){
					teamStats.setTeamName(tempAttrValue);
				}
				// Add stat key/value pair to Hash
				else{
					String [] tempAttrPair = tempAttrValue.split(":");

					if (tempAttrPair[1].trim().equals("INF"))
						teamStats.addMatchValue(tempAttrPair[0].trim(),0.0f);
					else
						teamStats.addMatchValue(tempAttrPair[0].trim(),Float.parseFloat(tempAttrPair[1].trim()));
				}
			}

			// Check to see if Hash Map already has team object created.
			if(teamObjectsHash.containsKey(teamStats.getTeamName())){
				// If so, remove it, edit it, and re-add to hash
				FSTeamMatch temp = teamObjectsHash.get(teamStats.getTeamName());
				temp.addAllMatchValues(teamStats.getMatchValues());
				teamObjectsHash.put(teamStats.getTeamName(), temp);
			}
			else{
				// If not, add to hash
				teamObjectsHash.put(teamStats.getTeamName(), teamStats);
			}
		}

		parser.reset();

		// For all teams in the hash table, create the team object, process the match, and add to the return value
		Iterator<String> teamKeys = teamObjectsHash.keySet().iterator();

		// As we know there are only two teams, we can proceed accordingly
		FSTeamMatch tempOppOne = teamObjectsHash.get(teamKeys.next());
		FSTeamMatch tempOppTwo = teamObjectsHash.get(teamKeys.next());

		// Determine Home/Away Team and set accordingly

		// Look for all nodes that have text in them and a sibling with class="goals".  Blank nodes elimiated with Regex Filter.
		nodes = parser.parse(new HasParentFilter(new HasSiblingFilter(new HasAttributeFilter("class", "goals")))).extractAllNodesThatMatch(new RegexFilter("[a-z]"));

		// Assume home team is first.  Reliant upon page format
		String homeTeam = nodes.elementAt(0).toHtml().trim();
		String awayTeam = nodes.elementAt(1).toHtml().trim();

		if (tempOppOne.getTeamName().equals(homeTeam)){
			tempOppOne.setMatchLocation("HOME");
			tempOppTwo.setMatchLocation("AWAY");
		}
		else if (tempOppTwo.getTeamName().equals(homeTeam)){
			tempOppOne.setMatchLocation("AWAY");
			tempOppTwo.setMatchLocation("HOME");
		}
		else{
			throw new Exception("Home team " + homeTeam + " / Away Team " + awayTeam + " not equal to Team One " + tempOppOne.getTeamName() + " Team Two " + tempOppTwo.getTeamName());
		}

		tempOppOne.setOpponentMatch(tempOppTwo);
		tempOppTwo.setOpponentMatch(tempOppOne);

		teamObjects.put(tempOppOne.getTeamName(), new FSTeamObject(tempOppOne.getTeamName(), tempOppOne));
		teamObjects.put(tempOppTwo.getTeamName(), new FSTeamObject(tempOppTwo.getTeamName(), tempOppTwo));

		parser.reset();

		return teamObjects;
	}

	private static String getFSMatchFileRename(String fileName) throws Exception{
		// Get match file
		Parser parser = new Parser (fileName);
		parser.setEncoding("UTF-8");

		String matchSeason = Integer.toString(getMatchDate(parser).get(Calendar.YEAR));
		String gameWeek = getGameWeek(parser);

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		NodeFilter [] filterArray = {new TagNameFilter("h1"), new HasSiblingFilter(new OrFilter(new OrFilter(new HasAttributeFilter("class", "matches no-header enhanced score"), new HasAttributeFilter("class", "matches no-header enhanced")), new HasAttributeFilter("class", "refresh-note")))};

		nodes = parser.parse(new AndFilter(filterArray));
		System.out.println(fileName);
		String teamOne = nodes.elementAt(0).getChildren().elementAt(0).toHtml().split("vs")[0].trim();
		String teamTwo = nodes.elementAt(0).getChildren().elementAt(0).toHtml().split("vs")[1].trim();

		parser.reset();

		return  matchSeason + "-" + gameWeek + "-" + teamOne + "-" + teamTwo + ".htm";
	}

	private static boolean exportTeamData(FSMatchReturnObject matchData, String weeklyFileName, String sumFilename, String gameOption, HashMap<String, UnderstatTeamObject> understatXGData) throws Exception{
		return exportTeamData(matchData, -1, -1, weeklyFileName, sumFilename, gameOption, understatXGData);
	}

	private static boolean exportTeamData(FSMatchReturnObject matchData, int currentWeek, int exportDuration, String weeklyFileName,  String sumFileName, String gameOption, HashMap<String, UnderstatTeamObject> understatXGData) throws Exception{
		boolean retVal = true;
		int gameWeekRange = -1;

		try{
			gameWeekRange = Integer.parseInt(gameOption);
		}
		catch(NumberFormatException e){}

		StringBuffer fileData = new StringBuffer(FootballAnalysisConstants.CSVTEAMMATCHHEADERS + "\n");
		ArrayList<String> columnHeaders = FootballAnalysisConstants.CSVTEAMOUTPUTHEADERS;

		HashMap<String, FSTeamObject> teamObjects = matchData.getTeamObjects();

		Iterator<String> iterator = teamObjects.keySet().iterator();

		while (iterator.hasNext()){
			Iterator<FSTeamMatch> matchIterator = teamObjects.get(iterator.next()).getMatchValues().iterator();

			while (matchIterator.hasNext()){
				FSTeamMatch tempMatch = matchIterator.next();
				FSTeamMatch tempOpponent = tempMatch.getOpponentMatch();

				if((gameWeekRange <= 0 || Integer.parseInt(tempMatch.getGameWeek()) >= gameWeekRange) && (!(gameOption.equals(FootballAnalysisConstants.AWAY_ONLY) && !tempMatch.getMatchLocation().equals("AWAY")) && !(gameOption.equals(FootballAnalysisConstants.HOME_ONLY) && !tempMatch.getMatchLocation().equals("HOME")))){
					fileData.append(tempMatch.getMatchName());
					fileData.append("," + tempOpponent.getMatchName());
					fileData.append("," + tempMatch.getMatchLocation());
					fileData.append("," + tempMatch.getGameWeek());
					fileData.append("," + tempMatch.getMatchDate());

					for (int i=0; i < columnHeaders.size(); i++){
						fileData.append("," + tempOpponent.getMatchValue(columnHeaders.get(i)));
					}
					for (int i=0; i < columnHeaders.size(); i++){
						fileData.append("," + tempMatch.getMatchValue(columnHeaders.get(i)));
					}

					fileData.append("\n");
				}
			}
		}

		FootballAnalysisUtil.writeFile(weeklyFileName, fileData.toString());

		fileData = new StringBuffer(FootballAnalysisConstants.CSVTEAMSUMHEADERS + "\n");

		iterator = teamObjects.keySet().iterator();
		
		while (iterator.hasNext()){
			HashMap<String,Float> teamSummedValues = new HashMap<String,Float>();
			String temp = iterator.next();
			FSTeamObject tempTeamObj = teamObjects.get(temp);
			Iterator<FSTeamMatch> matchIterator = tempTeamObj.getMatchValues().iterator();

			float gamesPlayed = 0f;

			while (matchIterator.hasNext()){
				FSTeamMatch tempMatch = matchIterator.next();

				if((gameWeekRange <= 0 || Integer.parseInt(tempMatch.getGameWeek()) >= gameWeekRange) && (!(gameOption.equals(FootballAnalysisConstants.AWAY_ONLY) && !tempMatch.getMatchLocation().equals("AWAY")) && !(gameOption.equals(FootballAnalysisConstants.HOME_ONLY) && !tempMatch.getMatchLocation().equals("HOME")))){
					for (int i=0; i < columnHeaders.size(); i++){
						String tempColumn = columnHeaders.get(i);
						Float tempValue = tempMatch.getMatchValue(tempColumn);

						if(teamSummedValues.containsKey(tempColumn)){
							tempValue = tempValue.floatValue() + teamSummedValues.get(tempColumn);
						}

						if(!tempColumn.equals("Games Played"))
							teamSummedValues.put(tempColumn, tempValue);
					}

					FSTeamMatch tempOppMatch = tempMatch.getOpponentMatch();

					for (int i=0; i < columnHeaders.size(); i++){
						String tempColumn = columnHeaders.get(i);
						Float tempValue = tempOppMatch.getMatchValue(tempColumn);

						if(teamSummedValues.containsKey("OPP " + tempColumn) && !tempColumn.equals("Games Played")){
							tempValue = tempValue.floatValue() + teamSummedValues.get("OPP " + tempColumn);
						}

						if(!tempColumn.equals("Games Played"))
							teamSummedValues.put("OPP " + tempColumn, tempValue);
					}

					gamesPlayed++;
					teamSummedValues.put("Games Played", gamesPlayed);
				}
			}

			fileData.append(tempTeamObj.getTeamName());

			for (int i=0; i < columnHeaders.size(); i++){
				if(teamSummedValues.containsKey("OPP " + columnHeaders.get(i))){
					fileData.append(",");
					fileData.append(teamSummedValues.get("OPP " + columnHeaders.get(i)));
				}
			}

			for (int i=0; i < columnHeaders.size(); i++){
				if(teamSummedValues.containsKey(columnHeaders.get(i))){
					fileData.append(",");
					fileData.append(teamSummedValues.get(columnHeaders.get(i)));
				}
			}

			UnderstatTeamObject tempUnderstatTeam = understatXGData.get(FootballAnalysisConstants.mapUnderstatTeamName(tempTeamObj.getTeamName()));

			fileData.append("," + tempUnderstatTeam.sumXG() + "," + tempUnderstatTeam.sumXGA());

			fileData.append("\n");
		}

		FootballAnalysisUtil.writeFile(sumFileName, fileData.toString());

		return retVal;
	}

	private static void exportPlayerAnalysisExcel(HashMap<String, HashMap<String, FSPlayerObject>> playerObjects, HashMap<String, FPLPlayerObject> eplPlayerData, HashMap<String, UnderstatPlayerObject> understatPlayerData, HashMap<String, FBRefPlayerObject> fbRefPlayerData, HashMap<String, FPLFSMapObject> fplFsPlayerMap, String fileName) throws Exception{
		XSSFWorkbook outputWorkbook = new XSSFWorkbook();

		Iterator<String> playerIterator = playerObjects.keySet().iterator();

		while (playerIterator.hasNext()){
			String positionName = playerIterator.next();
			outputWorkbook = ExcelOutputUtil.buildAnalysisExcel(outputWorkbook, exportAnalysisExcelWorker(positionName, playerObjects.get(positionName), eplPlayerData, understatPlayerData, fbRefPlayerData, fplFsPlayerMap), positionName, FootballAnalysisConstants.EXCELBLUECOLUMNCOUNT, FootballAnalysisConstants.EXCELTOTALCOLUMNCOUNT);				
		}

		FileOutputStream out = new FileOutputStream(new File(fileName));	
		outputWorkbook.write(out);
		out.close();
		outputWorkbook.close();
	}

	private static ArrayList<ArrayList<ExcelCellObject>> exportAnalysisExcelWorker(String positionName, HashMap<String, FSPlayerObject> playerObjects, HashMap<String, FPLPlayerObject> eplPlayerData, HashMap<String, UnderstatPlayerObject> understatPlayerData, HashMap<String, FBRefPlayerObject> fbRefPlayerData, HashMap<String, FPLFSMapObject> fplFsPlayerMap) throws Exception{
		ArrayList<ArrayList<ExcelCellObject>> excelData = new ArrayList<ArrayList<ExcelCellObject>>();

		ArrayList<ExcelCellObject> tempRowData = new ArrayList<ExcelCellObject>();
		ExcelCellObject tempCell;

		ArrayList<String> blueColumnHeaders = FootballAnalysisConstants.EXCELBLUECOLUMNHEADERS;

		for (int i = 0; i < blueColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, blueColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.MYBLUE);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}

		ArrayList<String> redColumnHeaders = FootballAnalysisConstants.EXCELREDCOLUMNHEADERS;

		for (int i = 0; i < redColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, redColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.MYRED);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}

		ArrayList<String> greenColumnHeaders = FootballAnalysisConstants.EXCELGREENCOLUMNHEADERS;

		for (int i = 0; i < greenColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, greenColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.MYGREEN);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}

		ArrayList<String> brownColumnHeaders = FootballAnalysisConstants.EXCELBROWNCOLUMNHEADERS;

		for (int i = 0; i < brownColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, brownColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.MYBROWN);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}

		ArrayList<String> orangeColumnHeaders = FootballAnalysisConstants.EXCELORANGECOLUMNHEADERS;

		for (int i = 0; i < orangeColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, orangeColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.MYORANGE);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}
		
		ArrayList<String> purpleColumnHeaders = FootballAnalysisConstants.EXCELPURPLECOLUMNHEADERS;

		for (int i = 0; i < purpleColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, purpleColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.DEEPPURPLE);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}
		
		ArrayList<String> deepBlueColumnHeaders = FootballAnalysisConstants.EXCELDEEPBLUECOLUMNHEADERS;

		for (int i = 0; i < deepBlueColumnHeaders.size(); i++){
			tempCell = new ExcelCellObject(Cell.CELL_TYPE_STRING, deepBlueColumnHeaders.get(i));
			tempCell.setTextBold(true);
			tempCell.setFontColor(FootballAnalysisConstants.TRUEWHITE);
			tempCell.setFillColor(FootballAnalysisConstants.DEEPBLUE);
			tempCell.setThinBorder(true);
			tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
			tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);

			tempRowData.add(tempCell);
		}

		excelData.add(tempRowData);

		ArrayList<String> consolidatedColumnHeaders = new ArrayList<String>();
		consolidatedColumnHeaders.addAll(blueColumnHeaders);
		consolidatedColumnHeaders.addAll(redColumnHeaders);
		consolidatedColumnHeaders.addAll(greenColumnHeaders);
		consolidatedColumnHeaders.addAll(brownColumnHeaders);
		consolidatedColumnHeaders.addAll(orangeColumnHeaders);
		consolidatedColumnHeaders.addAll(purpleColumnHeaders);
		consolidatedColumnHeaders.addAll(deepBlueColumnHeaders);

		Iterator<String> playerIterator = playerObjects.keySet().iterator();

		while(playerIterator.hasNext()){
			tempRowData = new ArrayList<ExcelCellObject>();
			FSPlayerObject tempPlayer = playerObjects.get(playerIterator.next());
			
			System.out.println("FS: " + tempPlayer.getName());

			HashMap<String,String> tempStatTableData = tempPlayer.getStatTableData();
			HashMap<String,String> columnHeaderMap = FootballAnalysisConstants.EXCELPLAYERSTATMAP;

			for (int i = 0; i < consolidatedColumnHeaders.size(); i++){
				if(consolidatedColumnHeaders.get(i).equals(FootballAnalysisConstants.EXCELCOLUMNNAME)){
					tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_STRING, tempPlayer.getName());
					tempCell.setTextBold(false);
					tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);

					if(fplFsPlayerMap.containsKey(tempPlayer.getName()) && !fplFsPlayerMap.get(tempPlayer.getName()).getFsPlayerPosition().equals(fplFsPlayerMap.get(tempPlayer.getName()).getEplPlayerPosition())){
						tempCell.setFillColor(FootballAnalysisConstants.TRUEYELLOW);
					}
					else{
						tempCell.setFillColor(FootballAnalysisConstants.DARKGRAY);
					}

					tempCell.setThinBorder(true);
					tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_LEFT);
					tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
				}
				else if(FootballAnalysisConstants.EXCELUNDERSTATCOLUMNS.contains((consolidatedColumnHeaders.get(i)))){
					String understatName = tempPlayer.getName();

					if(fplFsPlayerMap.containsKey(understatName)){
						understatName = fplFsPlayerMap.get(tempPlayer.getName()).getUnderstatPlayerName();
					}

					System.out.println("US: " + understatName);

					tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_NUMERIC, FootballAnalysisUtil.getUnderstatStat(consolidatedColumnHeaders.get(i), understatPlayerData.get(understatName)));
					tempCell.setTextBold(false);
					tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setFillColor(FootballAnalysisConstants.TRUEWHITE);
					tempCell.setThinBorder(true);
					tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
					tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
				}
				else if(FootballAnalysisConstants.EXCELFBREFCOLUMNS.contains((consolidatedColumnHeaders.get(i)))){
					String fbRefName = tempPlayer.getName();

					if(fplFsPlayerMap.containsKey(fbRefName)){
						fbRefName = fplFsPlayerMap.get(tempPlayer.getName()).getFBRefPlayerName();
					}

					System.out.println("FBREF: " + fbRefName);

					if(fbRefPlayerData.get(fbRefName) != null){
						tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_NUMERIC, FootballAnalysisUtil.getFBRefStat(consolidatedColumnHeaders.get(i), fbRefPlayerData.get(fbRefName)));
						tempCell.setTextBold(false);
						tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
						tempCell.setFillColor(FootballAnalysisConstants.TRUEWHITE);
						tempCell.setThinBorder(true);
						tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
						tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
						tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
					}
					else{
						System.out.println(fbRefName + " is null");
						tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_NUMERIC, 0.00);
					}
				}
				else if(FootballAnalysisConstants.EXCELEPLCOLUMNS.contains((consolidatedColumnHeaders.get(i)))){
					String eplName = tempPlayer.getName();
					
					if(fplFsPlayerMap.containsKey(eplName)){
						eplName = fplFsPlayerMap.get(tempPlayer.getName()).getEplPlayerName();
					}

					System.out.println("EP: " + eplName);
					
					tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_NUMERIC, FootballAnalysisUtil.getEPLStat(consolidatedColumnHeaders.get(i), eplPlayerData.get(eplName)));
					tempCell.setTextBold(false);
					tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setFillColor(FootballAnalysisConstants.TRUEWHITE);
					tempCell.setThinBorder(true);
					tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
					tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
				}
				else if(!FootballAnalysisConstants.FORMULAIDENTIFIER.equals(columnHeaderMap.get(consolidatedColumnHeaders.get(i)))){
					String temp = tempStatTableData.get(columnHeaderMap.get(consolidatedColumnHeaders.get(i)));

					if(temp != null && temp.matches("[-+]?\\d+(\\.\\d+)?")){
						tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_NUMERIC, Double.parseDouble(temp));
						tempCell.setTextBold(false);
						tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
						tempCell.setFillColor(FootballAnalysisConstants.TRUEWHITE);
						tempCell.setThinBorder(true);
						tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
						tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
						tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
					}
					else{
						tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_STRING, tempStatTableData.get(columnHeaderMap.get(consolidatedColumnHeaders.get(i))));
						tempCell.setTextBold(false);
						tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
						tempCell.setFillColor(FootballAnalysisConstants.TRUEWHITE);
						tempCell.setThinBorder(true);
						tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
						tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
						tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
					}
				}
				else if(FootballAnalysisConstants.FORMULAIDENTIFIER.equals(columnHeaderMap.get(consolidatedColumnHeaders.get(i)))){
					tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_FORMULA, FootballAnalysisUtil.getPlayerStatTableFormulasMap(positionName, consolidatedColumnHeaders.get(i), FootballAnalysisConstants.EXCELMAXCOLUMNCOUNT, FootballAnalysisConstants.EXCELMAXROWCOUNT, i, excelData.size() + 1));
					tempCell.setTextBold(false);
					tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setFillColor(FootballAnalysisConstants.TRUEWHITE);
					tempCell.setThinBorder(true);
					tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
					tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
				}
				else{
					tempCell = new ExcelCellObject(XSSFCell.CELL_TYPE_BLANK, null);
					tempCell.setTextBold(false);
					tempCell.setFontColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setFillColor(FootballAnalysisConstants.TRUEGREEN);
					tempCell.setThinBorder(true);
					tempCell.setBorderColor(FootballAnalysisConstants.TRUEBLACK);
					tempCell.setHorizontalAlignment(XSSFCellStyle.ALIGN_CENTER);
					tempCell.setDataFormat(FootballAnalysisUtil.getCellFormat(consolidatedColumnHeaders.get(i)));
				}

				tempRowData.add(tempCell);
			}
			excelData.add(tempRowData);
		}

		return excelData;
	}

	private static HashMap<String, FPLFSMapObject> buildFPLFSPlayerMap(ArrayList<String> fileData) throws Exception{
		HashMap<String, FPLFSMapObject> retVal = new HashMap<String, FPLFSMapObject>();

		for (int i = 0; i < fileData.size(); i++){
			String [] fileVals = fileData.get(i).split(",");
			
			if (fileVals.length == 7){
				retVal.put(fileVals[0], new FPLFSMapObject(fileVals[0], fileVals[1], fileVals[2], fileVals[3], fileVals[4], fileVals[5], fileVals[6]));
			}
			else if(fileVals[0].equals("")){}
			else{
				throw new Exception("FileVals length is " + fileVals.length);
			}
		}

		return retVal;
	}

	/*
	 * Controller method to get player data from a valid FFS Match file. Takes PARSER object opened on valid FFS Match file as well as
	 * a valid PlayerObject Hash.
	 */
	private static HashMap<String, FSPlayerMatch> getFFSMatchPlayerData(HashMap<String, FSPlayerMatch> basePlayerMatches, Parser parser, Calendar matchDate, String gameWeek) throws Exception{
		// Initialize basePlayerObjects if null		
		if (basePlayerMatches == null){
			basePlayerMatches = new HashMap<String, FSPlayerMatch> ();
		}

		HashMap<String, FSPlayerMatch> localPlayerMatches = new HashMap<String, FSPlayerMatch> ();

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		// Get team names and home/away
		HashMap<String,String> teamLocations = new HashMap<String,String>();

		// Get only those <a> nodes with a parent with class matches no-header enhanced score
		NodeFilter [] teamsFilterArray = {new TagNameFilter("a"), new HasParentFilter(new HasAttributeFilter("class", "matches no-header enhanced score"), true)};
		nodes = parser.parse(new AndFilter(teamsFilterArray));

		// First <a> link is HOME, 2nd is AWAY
		teamLocations.put(nodes.elementAt(0).getFirstChild().getText().trim(), "HOME");
		teamLocations.put(nodes.elementAt(1).getFirstChild().getText().trim(), "AWAY");

		// Reset parser to beginning of DOM
		parser.reset();

		// Get Player Stats
		// Look for all nodes that have an ancestor with id = player-tabs-1:11
		NodeFilter [] subPlayerFilter = {new HasParentFilter(new HasAttributeFilter("id", "player-tabs-1"), true), new HasParentFilter(new HasAttributeFilter("id", "player-tabs-2"), true),
				new HasParentFilter(new HasAttributeFilter("id", "player-tabs-3"), true), new HasParentFilter(new HasAttributeFilter("id", "player-tabs-4"), true),
				new HasParentFilter(new HasAttributeFilter("id", "player-tabs-5"), true), new HasParentFilter(new HasAttributeFilter("id", "player-tabs-6"), true),
				new HasParentFilter(new HasAttributeFilter("id", "player-tabs-7"), true), new HasParentFilter(new HasAttributeFilter("id", "player-tabs-8"), true),
				new HasParentFilter(new HasAttributeFilter("id", "player-tabs-9"), true), new HasParentFilter(new HasAttributeFilter("id", "player-tabs-10"), true),
				new HasParentFilter(new HasAttributeFilter("id", "player-tabs-11"), true)};

		// Get only those nodes with a td child of "first" class
		NodeFilter [] playerFilterArray = {new OrFilter(subPlayerFilter), new HasChildFilter(new CssSelectorNodeFilter("td[class=\"first\"]"))};
		nodes = parser.parse(new AndFilter(playerFilterArray));

		// Use Sorted Set to determine top 3 BPS
		TreeMap<Integer, String> playerBonus = new TreeMap<Integer, String>();

		// For each row
		for (int i = 0; i < nodes.size(); i++){
			// Create data objects
			FSPlayerMatch playerStats = new FSPlayerMatch();

			playerStats.setMatchDate(matchDate.getTime());
			playerStats.setGameWeek(gameWeek);

			org.htmlparser.util.NodeList playerNodes = new org.htmlparser.util.NodeList();
			org.htmlparser.util.NodeList nameNode = new org.htmlparser.util.NodeList();

			// Get player position and team name
			nodes.elementAt(i).collectInto(nameNode, new TagNameFilter("div"));
			String [] namePosPair = nameNode.elementAt(0).getLastChild().getText().trim().split("[,()]");

			// Team is 2nd position post split, position in 3rd
			playerStats.setTeamName(namePosPair[1]);
			playerStats.setPlayerPos(namePosPair[2]);

			// Set match location
			if(teamLocations.containsKey(playerStats.getTeamName())){
				playerStats.setMatchLocation(teamLocations.get(playerStats.getTeamName()));

				Object[] teamNames = teamLocations.keySet().toArray();

				if(!teamNames[0].equals(playerStats.getTeamName()))
					playerStats.setOpponentName(teamNames[0].toString());
				else
					playerStats.setOpponentName(teamNames[1].toString());
			}

			// Filter nodes to return only those with attribute "title"
			nodes.elementAt(i).collectInto(playerNodes, new HasAttributeFilter("title"));

			for (int j = 0; j < playerNodes.size(); j++){
				// Get the nodes and value of attribute "title"
				// All data has attribute title EXCEPT team name and position

				Tag statElement = (Tag)playerNodes.elementAt(j);
				String tempAttrValue = StringEscapeUtils.unescapeHtml4(statElement.getAttribute("title"));

				// First element contains the playerName and fsLink and is in a different format (no key/value pair)
				if(j == 0){
					playerStats.setPlayerName(tempAttrValue);
				}
				// Add stat key/value pair to Hash
				else{
					String [] tempAttrPair = tempAttrValue.split(":");

					if (tempAttrPair[1].trim().equals("INF"))
						playerStats.addMatchValue(tempAttrPair[0].trim(),0.0f);
					else
						playerStats.addMatchValue(tempAttrPair[0].trim(), Float.parseFloat(tempAttrPair[1].trim()));

					if(tempAttrPair[0].trim().equals(FootballAnalysisConstants.FSBPSHEADER)){
						Integer playerBPS = new Integer(tempAttrPair[1].trim());
						String playerBPSNames = playerStats.getPlayerName() + playerStats.getTeamName();

						if(playerBonus.containsKey(playerBPS)){
							playerBPSNames = playerBonus.get(playerBPS) + "," + playerBPSNames;
						}
						playerBonus.put(playerBPS, playerBPSNames);
					}
				}
			}

			// Check to see if Hash Map already has player object created.
			if(localPlayerMatches.containsValue(playerStats)){
				// If so, remove it, edit it, and re-add to hash
				FSPlayerMatch temp = localPlayerMatches.get(playerStats.getPlayerName() + playerStats.getTeamName());
				temp.addAllMatchValues(playerStats.getMatchValues());
				localPlayerMatches.put(playerStats.getPlayerName() + playerStats.getTeamName(), temp);
			}
			else{
				// If not, add to hash
				localPlayerMatches.put(playerStats.getPlayerName() + playerStats.getTeamName(), playerStats);
			}
		}

		HashMap<String,Integer> topBPS = new HashMap<String,Integer>();
		int bonusAllocated = 0;

		while(bonusAllocated < 3){
			String [] tempArray = playerBonus.remove(playerBonus.lastKey()).split(",");

			for (int j = 0; j < tempArray.length; j++){
				topBPS.put(tempArray[j], 3 - bonusAllocated);
			}

			bonusAllocated += tempArray.length;
		}

		Iterator<String> bonusIterator = topBPS.keySet().iterator();

		while(bonusIterator.hasNext()){
			String tempPlayerName = bonusIterator.next();
			FSPlayerMatch tempPlayer = localPlayerMatches.get(tempPlayerName);
			tempPlayer.setBonusPoints(topBPS.get(tempPlayerName));
			localPlayerMatches.put(tempPlayer.getPlayerName() + tempPlayer.getTeamName(), tempPlayer);
		}

		Iterator<String> localPlayerMatchesIterator = localPlayerMatches.keySet().iterator();

		while(localPlayerMatchesIterator.hasNext()){
			FSPlayerMatch tempPlayer = localPlayerMatches.get(localPlayerMatchesIterator.next());

			if(basePlayerMatches.containsKey(tempPlayer.getPlayerName() + tempPlayer.hashCode())){
				// If so, remove it, edit it, and re-add to hash
				FSPlayerMatch temp = basePlayerMatches.get(tempPlayer.getPlayerName() + tempPlayer.hashCode());
				temp.addAllMatchValues(tempPlayer.getMatchValues());
				basePlayerMatches.put(tempPlayer.getPlayerName() + tempPlayer.hashCode(), temp);
			}
			else{
				// If not, add to hash
				basePlayerMatches.put(tempPlayer.getPlayerName() + tempPlayer.hashCode(), tempPlayer);
			}
		}

		parser.reset();

		return basePlayerMatches;
	}
}