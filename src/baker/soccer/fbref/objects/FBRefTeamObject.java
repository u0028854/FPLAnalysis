package baker.soccer.fbref.objects;

public class FBRefTeamObject {
	private String teamName;
	private float xG;
	private float xGC;
	
	public FBRefTeamObject(){}
	
	public String getTeamName() {
		return teamName;
	}
	public void setTeamName(String teamName) {
		this.teamName = teamName;
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
		return "FBRefTeamObject [teamName=" + teamName + ", xG=" + xG + ", xGC=" + xGC + "]";
	}
}
