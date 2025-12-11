package baker.soccer.understat;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasSiblingFilter;
import org.htmlparser.filters.TagNameFilter;

import baker.soccer.understat.objects.UnderstatBaseJSONData;
import baker.soccer.understat.objects.UnderstatPlayerObject;
import baker.soccer.understat.objects.UnderstatTeamObject;
import baker.soccer.util.FootballAnalysisConstants;
import baker.soccer.util.FootballAnalysisUtil;
import baker.soccer.util.objects.FPLFSMapObject;

public class UnderstatAction {
	public static void main(String [] args) throws Exception{
		processUnderstatHTML(FootballAnalysisConstants.USTEAMS);
	}

	private static void processUnderstatHTML(int option) throws Exception{
		Parser parser = new Parser(FootballAnalysisConstants.USHTMLFILENAME);
		parser.setEncoding("UTF-8");

		HasAttributeFilter siblingAttribute = (option == FootballAnalysisConstants.USPLAYERS ? new HasAttributeFilter("id", "league-players") : new HasAttributeFilter("data-scheme", "chart"));

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		// Look for all nodes that are script tags and have an sibling attribute as defined above
		NodeFilter [] scriptArray = {new HasSiblingFilter(siblingAttribute), new TagNameFilter("script")};
		
		nodes = parser.parse(new AndFilter(scriptArray));
		
		String xGJSONText = nodes.elementAt(0).getChildren().elementAt(0).getText().trim();
		xGJSONText = xGJSONText.replaceAll("\\\\x20"," ").replaceAll("\\\\x2D","-").replaceAll("\\\\x22","\"").replaceAll("\\\\x3A",":").replaceAll("\\\\x5B","[").replaceAll("\\\\x5D","]").replaceAll("\\\\x7B","{").replaceAll("\\\\x7D","}");

		int headerIndex = (option == FootballAnalysisConstants.USPLAYERS ? xGJSONText.indexOf(FootballAnalysisConstants.USPLAYERHEADERSTRING) : xGJSONText.indexOf(FootballAnalysisConstants.USTEAMHEADERSTRING));

		if(headerIndex != -1){
			if(option == FootballAnalysisConstants.USPLAYERS)
				FootballAnalysisUtil.writeFile(FootballAnalysisConstants.USPLAYEROUTPUTFILENAME, xGJSONText.substring(headerIndex + FootballAnalysisConstants.USPLAYERHEADERSTRING.length(), xGJSONText.length() - FootballAnalysisConstants.USPLAYERFOOTERSTRING.length()));
			else
				FootballAnalysisUtil.writeFile(FootballAnalysisConstants.USTEAMOUTPUTFILENAME, xGJSONText.substring(headerIndex + FootballAnalysisConstants.USTEAMHEADERSTRING.length(), xGJSONText.length() - FootballAnalysisConstants.USTEAMFOOTERSTRING.length()));
		}
		else{
			throw new Exception("Bad JSON String!");
		}
	}

	public static HashMap<String, UnderstatPlayerObject> processUnderstatXGPlayerJSON(int eplSeason, String option) throws Exception{
		return processPlayerJSON(eplSeason, option);
	}

	public static HashMap<String, UnderstatTeamObject> processUnderstatXGTeamJSON() throws Exception{
		processUnderstatHTML(FootballAnalysisConstants.USTEAMS);
		return processTeamJSON(FootballAnalysisConstants.USTEAMOUTPUTFILENAME);
	}

	private static HashMap<String, UnderstatPlayerObject> processPlayerJSON(int eplSeason, String option) throws Exception{
		HashMap<String, UnderstatPlayerObject> retVal = new HashMap<String, UnderstatPlayerObject>();
		String fileName = "";

		if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_ARG)){
			fileName = FootballAnalysisConstants.USPLAYERINPUTFILENAME;
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG)){
			fileName = FootballAnalysisConstants.USPLAYERINPUT6GWFILENAME;
		}		

		ArrayList<String> fileData = FootballAnalysisUtil.getFileDataByLine(fileName);

		for (int i = 0; i < fileData.size(); i++){
			String [] fileVals = fileData.get(i).split(",");
						
			if (fileVals.length == 7 || fileVals.length == 8){
				retVal.put(
						fileVals[0], 
						new UnderstatPlayerObject(
								fileVals[0], 
								fileVals[1], 
								fileVals[2], 
								fileVals[3], 
								fileVals[4], 
								fileVals[5],
								fileVals[6]));
			}
			else{
				for(int j = 0; j < fileVals.length; j++){
					System.out.println(j + " : " + fileVals[j]);
				}
				throw new Exception("FileVals length is " + fileVals.length);
			}
		}

		return retVal;
	}

	private static HashMap<String, UnderstatTeamObject> processTeamJSON(String jsonInputFile) throws Exception{
		HashMap<String, UnderstatTeamObject> retVal = new HashMap<String, UnderstatTeamObject>();
		UnderstatTeamObject newTeamObject;

		//read json file data to String
		byte[] jsonData = Files.readAllBytes(Paths.get(jsonInputFile));

		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode root = objectMapper.readTree(jsonData);
		Iterator<String> rootFieldNames = root.getFieldNames();

		while (rootFieldNames.hasNext()) {
			JsonNode node = root.get(rootFieldNames.next());
			newTeamObject = new UnderstatTeamObject(node.get("title").toString());

			Iterator<JsonNode> historyNodes = node.get("history").getElements();

			while(historyNodes.hasNext()){
				JsonNode historyNode = historyNodes.next();
				newTeamObject.addResult((float)historyNode.get("xG").getDoubleValue(), (float)historyNode.get("xGA").getDoubleValue());
			}

			retVal.put(newTeamObject.getTeamName(), newTeamObject);
		}

		return retVal;
	}
}
