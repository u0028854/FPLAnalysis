package baker.soccer.fs.objects;

import java.util.HashMap;
import java.util.HashSet;

import baker.soccer.util.FootballAnalysisUtil;

public class FSPlayerObject {
	private String name;
	private String playerPos;
	private HashSet<String> playerTeams;
	private HashMap<Integer,HashMap<String, Float>> seasonsSum;
	private HashMap<String,String> statTableData;

	public FSPlayerObject(){
		this.seasonsSum = null;
		this.statTableData = new HashMap<String,String>();
	}

	public FSPlayerObject(String name, String fsLink){
		this();
		this.setName(FootballAnalysisUtil.stripAccents(name));
	}

	public void addTeam(String teamName){
		if(playerTeams == null)
			playerTeams = new HashSet<String> ();

		if(!playerTeams.contains(teamName))
			playerTeams.add(teamName);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = FootballAnalysisUtil.stripAccents(name);
	}

	public HashMap<Integer, HashMap<String,Float>> getSeasonsSum() {
		return seasonsSum;
	}

	public void setSeasonsSum(HashMap<Integer, HashMap<String,Float>> seasonsSum) {
		this.seasonsSum = seasonsSum;
	}

	public HashMap<String, String> getStatTableData() {
		return statTableData;
	}

	public void setStatTableData(HashMap<String, String> statTableData) {
		this.statTableData = statTableData;
	}

	public void addStatTableData(HashMap<String, String> statTableData){
		this.statTableData.putAll(statTableData);
	}

	public String getPlayerPos() {
		return playerPos;
	}

	public void setPlayerPos(String playerPos) {
		this.playerPos = playerPos;
	}

	public HashSet<String> getPlayerTeams() {
		return playerTeams;
	}

	public void setPlayerTeams(HashSet<String> playerTeams) {
		this.playerTeams = playerTeams;
	}
	
	public String getPlayerTeam() {
		return this.statTableData.get("Team");
	}

	public void addMatchData(FSPlayerMatch matchData){

	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((playerPos == null) ? 0 : playerPos.hashCode());
		result = prime * result + ((playerTeams == null) ? 0 : playerTeams.hashCode());
		result = prime * result + ((seasonsSum == null) ? 0 : seasonsSum.hashCode());
		result = prime * result + ((statTableData == null) ? 0 : statTableData.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FSPlayerObject other = (FSPlayerObject) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (playerPos == null) {
			if (other.playerPos != null)
				return false;
		} else if (!playerPos.equals(other.playerPos))
			return false;
		if (playerTeams == null) {
			if (other.playerTeams != null)
				return false;
		} else if (!playerTeams.equals(other.playerTeams))
			return false;
		if (seasonsSum == null) {
			if (other.seasonsSum != null)
				return false;
		} else if (!seasonsSum.equals(other.seasonsSum))
			return false;
		if (statTableData == null) {
			if (other.statTableData != null)
				return false;
		} else if (!statTableData.equals(other.statTableData))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FSPlayerObject [name=" + name + ", playerPos=" + playerPos + ", playerTeams=" + playerTeams
				+ ", seasonsSum=" + seasonsSum + ", statTableData=" + statTableData + "]";
	}
}
