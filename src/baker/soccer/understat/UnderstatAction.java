package baker.soccer.understat;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
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

public class UnderstatAction {
	public static void main(String [] args) throws Exception{
		HashMap<String, UnderstatPlayerObject> temp = UnderstatAction.processUnderstatXGPlayerJSON(FootballAnalysisConstants.EPL_SEASONS[FootballAnalysisConstants.EPL_SEASONS.length - 1], FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_ARG);

		Iterator<String> iterator = temp.keySet().iterator();

		while(iterator.hasNext()){
			UnderstatPlayerObject tempPlayer = temp.get(iterator.next());

			System.out.println(tempPlayer.getPlayer_name() + " xg: " + tempPlayer.getxG90() + " npxg:" + tempPlayer.getnpxG90() + " xa: " + tempPlayer.getxA90());
		}
	}

	private static void processUnderstatHTML(int option) throws Exception{
		Parser parser = new Parser(FootballAnalysisConstants.USHTMLFILENAME);
		parser.setEncoding("UTF-8");

		HasAttributeFilter siblingAttribute = (option == FootballAnalysisConstants.USPLAYERS ? new HasAttributeFilter("id", "league-players") : new HasAttributeFilter("data-scheme", "chart"));

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		// Look for all nodes that have an ancestor with id = team-tabs-1:8
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

	public static HashMap<String, UnderstatPlayerObject> processPlayerJSON(int eplSeason, String option) throws Exception{
		HashMap<String, UnderstatPlayerObject> retVal = new HashMap<String, UnderstatPlayerObject>();

		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		HttpURLConnection con = null;
		String urlParameters = "season=" + Integer.toString(eplSeason);

		for (int i=0; i < FootballAnalysisConstants.USAPIPARAMS.length; i++){
			urlParameters += "&" + FootballAnalysisConstants.USAPIPARAMS[i];
		}
		
		if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG) || option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_4GW_ARG)){
			java.util.Date periodStartDate = FootballAnalysisUtil.getGameweekStart(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG) ? 6 : 4);
		}
		
		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

		try {
			URL myurl = new URL(FootballAnalysisConstants.USAPIURL);
			con = (HttpURLConnection) myurl.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "Java client");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}

			UnderstatBaseJSONData uStatJson = new UnderstatBaseJSONData(objectMapper.readTree(con.getInputStream()));

			Iterator<UnderstatPlayerObject> iterator = uStatJson.getPlayerObjects().iterator();

			while(iterator.hasNext()){
				UnderstatPlayerObject tempPlayer = iterator.next();
				//retVal.put(tempPlayer.getPlayer_name(), new UnderstatPlayerObject(tempPlayer.getPlayer_name(), tempPlayer.getxG90(), tempPlayer.getnpxG90(), tempPlayer.getxA90()));
				retVal.put(tempPlayer.getPlayer_name(), tempPlayer);
			}
		}
		catch(Exception e){
			System.err.println(e);
		}
		finally {
			con.disconnect();
		}

		return retVal;
	}

	public static HashMap<String, UnderstatTeamObject> processTeamJSON(String jsonInputFile) throws Exception{
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
