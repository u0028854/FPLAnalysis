package baker.soccer.fbref.objects;

public class FBRefTeamObject {
	private String teamName;
	private int matchesPlayed;
	private int teamWins;
	private int teamDraws;
	private int goalsScored;
	private int cleanSheets;
	private float xG;
	private float xGC;
	
	public FBRefTeamObject(){}
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getMatchesPlayed() {
		return matchesPlayed;
	}
	public void setMatchesPlayed(int matchesPlayed) {
		this.matchesPlayed = matchesPlayed;
	}
	public int getTeamWins() {
		return teamWins;
	}
	public void setTeamWins(int teamWins) {
		this.teamWins = teamWins;
	}
	public int getTeamDraws() {
		return teamDraws;
	}
	public void setTeamDraws(int teamDraws) {
		this.teamDraws = teamDraws;
	}
	public int getGoalsScored() {
		return goalsScored;
	}
	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}
	public int getCleanSheets() {
		return cleanSheets;
	}
	public void setCleanSheets(int cleanSheets) {
		this.cleanSheets = cleanSheets;
	}
	public float getxG() {
		return xG;
	}
	public void setxG(float xG) {
		this.xG = xG;
	}
	public float getxGC() {
		return xGC;
	}
	public void setxGC(float xGC) {
		this.xGC = xGC;
	}
	
	public String csvOutput(){
		return teamName + "," + xGC + "," + xG;
	}

	@Override
	public String toString() {
		return "FBRefTeamObject [teamName=" + teamName + 
		", matchesPlayed=" + matchesPlayed +
		", teamWins=" + teamWins +
		", teamDraws=" + teamDraws +
		", goalsScored=" + goalsScored +
		", cleanSheets=" + cleanSheets +
		", xG=" + xG + ", xGC=" + xGC + "]";
	}
}
