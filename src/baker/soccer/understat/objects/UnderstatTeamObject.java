package baker.soccer.understat.objects;

public class UnderstatTeamObject {
	private String teamName;
	private float xG;
	private float xGA;
	private float sixGameXG;
	private float sixGameXGA;

	public UnderstatTeamObject(String teamName) {
		super();
		this.teamName = teamName.replace("\"", "");
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public float getXG(){
		return this.xG;
	}

	public void setXG(float xG){
		this.xG = xG;
	}

	public float getXGA(){
		return this.xGA;
	}

	public void setXGA(float xGA){
		this.xGA = xGA;
	}

	public float getSixGWXG(){
		return this.sixGameXG;
	}

	public void setSixGWXG(float sixGameXG){
		this.sixGameXG = sixGameXG;
	}

	public float getSixGWXGA(){
		return this.sixGameXGA;
	}

	public void setSixGWXGA(float sixGameXGA){
		this.sixGameXGA = sixGameXGA;
	}

	@Override
	public String toString() {
		return "UnderstatTeamObject [teamName=" + teamName + ", xG=" + xG + ", xGA=" + xGA + ", sixGameXG=" + sixGameXG + ", sixGameXGA=" + sixGameXGA + "]";
	}
}