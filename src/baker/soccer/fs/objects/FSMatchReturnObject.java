package baker.soccer.fs.objects;

import java.util.HashMap;

public class FSMatchReturnObject {
	HashMap<String, FSTeamObject> teamObjects;
	HashMap<String, FSPlayerMatch> playerMatches; 

	public FSMatchReturnObject(){
		teamObjects = new HashMap<String, FSTeamObject>();
		playerMatches = new HashMap<String, FSPlayerMatch>();
	}

	public HashMap<String, FSTeamObject> getTeamObjects() {
		return teamObjects;
	}

	public void setTeamObjects(HashMap<String, FSTeamObject> teamObjects) {
		this.teamObjects = teamObjects;
	}

	public HashMap<String, FSPlayerMatch> getPlayerObjects() {
		return playerMatches;
	}

	public void setPlayerObjects(HashMap<String, FSPlayerMatch> playerObjects) {
		this.playerMatches = playerObjects;
	}

	@Override
	public String toString() {
		return "FSMatchReturnObject [teamObjects=" + teamObjects
				+ ", playerMatches=" + playerMatches + "]";
	}
}
