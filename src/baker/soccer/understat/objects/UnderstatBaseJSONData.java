package baker.soccer.understat.objects;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class UnderstatBaseJSONData {
	public UnderstatBaseJSONData(JsonNode jsonRoot){
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			this.playerObjects = objectMapper.readValue(jsonRoot.get("response").get("players"), new TypeReference<List<UnderstatPlayerObject>>(){});
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
	}
	private List<UnderstatPlayerObject> playerObjects;

	public List<UnderstatPlayerObject> getPlayerObjects() {
		return playerObjects;
	}

	public void setPlayerObjects(List<UnderstatPlayerObject> playerObjects) {
		this.playerObjects = playerObjects;
	}
}