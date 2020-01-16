package baker.soccer.understat.objects;

public class UnderstatPlayerObject {
	private String player_name;
	private int time;
	private float xG;
	private float npxG;
	private float xA;
	private String position;
	private String team_title;

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = player_name;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public void setxG(float xG) {
		this.xG = xG;
	}

	public void setxA(float xA) {
		this.xA = xA;
	}

	public void setNpxG(float npxG) {
		this.npxG = npxG;
	}

	public float getxG90(){
		return xG / time * 90.0f;
	}

	public float getnpxG90(){
		return npxG / time * 90.0f;
	}

	public float getxA90(){
		return xA / time * 90.0f;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getTeam_title() {
		return team_title;
	}

	public void setTeam_title(String team_title) {
		this.team_title = team_title;
	}

	@Override
	public String toString() {
		return "UnderstatPlayerObject [player_name=" + player_name + ", time=" + time + ", xG=" + xG + ", npxG="
				+ npxG + ", xA=" + xA + "]";
	}
}
