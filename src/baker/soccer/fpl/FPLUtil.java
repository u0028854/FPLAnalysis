package baker.soccer.fpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import baker.soccer.fpl.objects.FPLBaseJSONData;
import baker.soccer.fpl.objects.FPLPlayerObject;
import baker.soccer.util.FootballAnalysisConstants;

public class FPLUtil {
	public static HashMap<String, FPLPlayerObject> processEPLPlayerSeasonAction(String option) throws Exception{
		String fileName = FootballAnalysisConstants.FPLPLAYERJSON;
		
		if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG)){
			fileName = FootballAnalysisConstants.FPLPLAYERSIXWEEKJSON;
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_4GW_ARG)){
			fileName = FootballAnalysisConstants.FPLPLAYERFOURWEEKJSON;
		}

		return processEPLPlayerSeasonActionWorker(fileName);
	}
	
	private static HashMap<String, FPLPlayerObject> processEPLPlayerSeasonActionWorker(String jsonInputFile) throws Exception{
		HashMap<String, FPLPlayerObject> retVal = new HashMap<String, FPLPlayerObject>();
		FPLPlayerObject newPlayerObject;

		//read json file data to String
		byte[] jsonData = Files.readAllBytes(Paths.get(jsonInputFile));

		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();

		JsonNode root = objectMapper.readTree(jsonData);
		Iterator<JsonNode> nodeIterator = root.get(objectMapper.readTree(jsonData).getFieldNames().next()).getElements();

		while (nodeIterator.hasNext()) {
			JsonNode node = nodeIterator.next();
			
			if(node.get("name") != null && !node.get("name").toString().equals("")){
			
				newPlayerObject = new FPLPlayerObject();
				
				newPlayerObject.setPlayerName(node.get("name").toString().replace("\"", ""));
				newPlayerObject.setBonus(Integer.parseInt(node.get("fplBonus").toString()));
				newPlayerObject.setBps(Integer.parseInt(node.get("bpsPts").toString()));
				newPlayerObject.setClean_sheets(Integer.parseInt(node.get("cleanSheets").toString()));
				newPlayerObject.setNow_cost(Integer.parseInt(node.get("nowCost").toString()));
				newPlayerObject.setOwn_goals(Integer.parseInt(node.get("ownGoals").toString()));
				newPlayerObject.setTotal_points(Integer.parseInt(node.get("fplPts").toString()));
				newPlayerObject.setAssists(Integer.parseInt(node.get("fplAssists").toString()));
	
				retVal.put(newPlayerObject.getPlayerName(), newPlayerObject);
			}
		}

		return retVal;
	}

	private static HashMap<String, FPLPlayerObject> processEPLPlayerSeasonActionWorker() throws Exception{
		HashMap<String, FPLPlayerObject> retVal = new HashMap<String, FPLPlayerObject>();

		//create ObjectMapper instance
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

		HttpURLConnection con = null;

		try {
			URL myurl = new URL(FootballAnalysisConstants.FPLAPIURL);
			con = (HttpURLConnection) myurl.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("GET");
			con.setRequestProperty("User-Agent", "Java client");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			FPLBaseJSONData fplJson = new FPLBaseJSONData(objectMapper.readTree(con.getInputStream()));

			Iterator<FPLPlayerObject> iterator = fplJson.getPlayerObjects().iterator();

			while(iterator.hasNext()){
				FPLPlayerObject tempPlayer = iterator.next();
				retVal.put(tempPlayer.getPlayerName(), tempPlayer);
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
}
