package baker.soccer.fs.objects;

public class FSTeamTableObject {
	private String teamName;
	private int gamesPlayed;
	private int bigChances;
	private int bigChancesConceded;
	private int goalsScored;
	private int goalsConceded;
	private int shotsOnTarget;
	private int shotsOnTargetConceded;
	private float xG;
	private float xGC;
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}
	public int getGamesPlayed() {
		return gamesPlayed;
	}
	public void setGamesPlayed(int gamesPlayed) {
		this.gamesPlayed = gamesPlayed;
	}
	public int getBigChances() {
		return bigChances;
	}
	public void setBigChances(int bigChances) {
		this.bigChances = bigChances;
	}
	public int getBigChancesConceded() {
		return bigChancesConceded;
	}
	public void setBigChancesConceded(int bigChancesConceded) {
		this.bigChancesConceded = bigChancesConceded;
	}
	public int getGoalsScored() {
		return goalsScored;
	}
	public void setGoalsScored(int goalsScored) {
		this.goalsScored = goalsScored;
	}
	public int getGoalsConceded() {
		return goalsConceded;
	}
	public void setGoalsConceded(int goalsConceded) {
		this.goalsConceded = goalsConceded;
	}
	public int getShotsOnTarget() {
		return shotsOnTarget;
	}
	public void setShotsOnTarget(int shotsOnTarget) {
		this.shotsOnTarget = shotsOnTarget;
	}
	public int getShotsOnTargetConceded() {
		return shotsOnTargetConceded;
	}
	public void setShotsOnTargetConceded(int shotsOnTargetConceded) {
		this.shotsOnTargetConceded = shotsOnTargetConceded;
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
		return teamName + "," + bigChancesConceded + "," + shotsOnTargetConceded + "," + goalsConceded + "," +
				xGC + "," + bigChances + "," + shotsOnTarget + "," + goalsScored + "," + xG + "," + gamesPlayed;
	}
	
	@Override
	public String toString() {
		return "FSTeamTableObject [teamName=" + teamName + ", gamesPlayed=" + gamesPlayed + ", bigChances=" + bigChances
				+ ", bigChancesConceded=" + bigChancesConceded + ", goalsScored=" + goalsScored + ", goalsConceded="
				+ goalsConceded + ", shotsOnTarget=" + shotsOnTarget + ", shotsOnTargetConceded="
				+ shotsOnTargetConceded + ", xG=" + xG + ", xGC=" + xGC + "]";
	}	
}