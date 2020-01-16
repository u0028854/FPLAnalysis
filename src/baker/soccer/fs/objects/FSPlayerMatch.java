package baker.soccer.fs.objects;

import java.util.HashMap;

import baker.soccer.util.FootballAnalysisConstants;

public class FSPlayerMatch extends FSMatchObject{
	private String playerName;
	private String playerPos;
	private String teamName;
	private String opponentName;
	private int bonusPoints;

	public FSPlayerMatch(){
		super();

		bonusPoints = 0;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	public String getPlayerPos() {
		return playerPos;
	}

	public void setPlayerPos(String playerPos) {
		this.playerPos = playerPos.trim();
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getOpponentName() {
		return opponentName;
	}

	public void setOpponentName(String opponentName) {
		this.opponentName = opponentName;
	}

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public float getFPLPoints(){
		switch(this.playerPos){
		case "Goalkeeper":
			return (this.getMatchValue(FootballAnalysisConstants.FPLMINS) >= 60 ? 2.0f : 1.0f) + (this.getMatchValue(FootballAnalysisConstants.FPLASSISTS) * 3) + (this.getMatchValue(FootballAnalysisConstants.FPLGS) * 6) + (this.getMatchValue(FootballAnalysisConstants.FPLCS) * 4) + (float)Math.floor(this.getMatchValue(FootballAnalysisConstants.FPLSV) / 3) - 
					(float)Math.floor(this.getMatchValue(FootballAnalysisConstants.FPLGC) / 2) + (this.getMatchValue(FootballAnalysisConstants.FPLYCS) * -1) + (this.getMatchValue(FootballAnalysisConstants.FPLRCS) * -3) + (float)this.bonusPoints;

		case "Defender":
			return (this.getMatchValue(FootballAnalysisConstants.FPLMINS) >= 60 ? 2.0f : 1.0f) + (this.getMatchValue(FootballAnalysisConstants.FPLASSISTS) * 3) + (this.getMatchValue(FootballAnalysisConstants.FPLGS) * 6) + (this.getMatchValue(FootballAnalysisConstants.FPLCS) * 4) - (float)Math.floor(this.getMatchValue(FootballAnalysisConstants.FPLGC) / 2) 
					+ (this.getMatchValue(FootballAnalysisConstants.FPLYCS) * -1) + (this.getMatchValue(FootballAnalysisConstants.FPLRCS) * -3) + (float)this.bonusPoints;

		case "Midfielder":
			return (this.getMatchValue(FootballAnalysisConstants.FPLMINS) >= 60 ? 2.0f : 1.0f) + (this.getMatchValue(FootballAnalysisConstants.FPLASSISTS) * 3) + (this.getMatchValue(FootballAnalysisConstants.FPLGS) * 5) + (this.getMatchValue(FootballAnalysisConstants.FPLCS) * 1) + (this.getMatchValue(FootballAnalysisConstants.FPLYCS) * -1) 
					+ (this.getMatchValue(FootballAnalysisConstants.FPLRCS) * -3) + (float)this.bonusPoints;

		case "Forward":
			return (this.getMatchValue(FootballAnalysisConstants.FPLMINS) >= 60 ? 2.0f : 1.0f) + (this.getMatchValue(FootballAnalysisConstants.FPLASSISTS) * 3) + (this.getMatchValue(FootballAnalysisConstants.FPLGS) * 4) + (this.getMatchValue(FootballAnalysisConstants.FPLYCS) * -1) + (this.getMatchValue(FootballAnalysisConstants.FPLRCS) * -3) 
					+ (float)this.bonusPoints;

		default:
			return 0.0f;
		}
	}

	public HashMap<String, String> getDatabaseInsertValues(){
		HashMap<String, String> retVal = super.getDatabaseInsertValues(FootballAnalysisConstants.MATCH_PLAYERS_PRUNED_VALUES);

		retVal.put("PLAYER_NAME", playerName);
		retVal.put("PLAYER_POS", playerPos);
		retVal.put("TEAM_NAME", teamName);
		retVal.put("OPP_NAME", opponentName);
		retVal.put("Bonus Points", Integer.toString(bonusPoints));
		retVal.put("FPL_POINTS", Float.toString(this.getFPLPoints()));

		return retVal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((playerName == null) ? 0 : playerName.hashCode());
		result = prime * result + ((playerPos == null) ? 0 : playerPos.hashCode());
		result = prime * result + ((teamName == null) ? 0 : teamName.hashCode());
		result = prime * result + super.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FSPlayerMatch other = (FSPlayerMatch) obj;
		if (playerName == null) {
			if (other.playerName != null)
				return false;
		} else if (!playerName.equals(other.playerName))
			return false;
		if (playerPos == null) {
			if (other.playerPos != null)
				return false;
		} else if (!playerPos.equals(other.playerPos))
			return false;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FSPlayerMatch [playerName=" + playerName + ", playerPos=" + playerPos + ", teamName=" + teamName
				+ ", opponentName=" + opponentName + "]";
	}
}