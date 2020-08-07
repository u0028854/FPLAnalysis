package baker.soccer.fpl;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import baker.soccer.fpl.objects.FPLBaseJSONData;
import baker.soccer.fpl.objects.FPLPlayerObject;
import baker.soccer.util.FootballAnalysisConstants;

public class FPLUtil {
	public static HashMap<String, FPLPlayerObject> processEPLPlayerSeasonAction(boolean outputData) throws Exception{
		return processEPLPlayerSeasonActionWorker();
	}

	public static HashMap<String, FPLPlayerObject> processEPLPlayerSeasonActionWorker() throws Exception{
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
