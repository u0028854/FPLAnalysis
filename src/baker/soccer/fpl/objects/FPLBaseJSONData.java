package baker.soccer.fpl.objects;

import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class FPLBaseJSONData {
	public FPLBaseJSONData(JsonNode jsonRoot){
		try{
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);

			this.playerObjects = objectMapper.readValue(jsonRoot.get("elements"), new TypeReference<List<FPLPlayerObject>>(){});
		}
		catch(Exception e){
			e.printStackTrace(System.err);
		}
	}
	private List<FPLPlayerObject> playerObjects;

	public List<FPLPlayerObject> getPlayerObjects() {
		return playerObjects;
	}

	public void setPlayerObjects(List<FPLPlayerObject> playerObjects) {
		this.playerObjects = playerObjects;
	}
}