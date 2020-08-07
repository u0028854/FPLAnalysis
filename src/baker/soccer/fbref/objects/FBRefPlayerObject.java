package baker.soccer.fbref.objects;

import baker.soccer.util.FootballAnalysisUtil;

public class FBRefPlayerObject {
	private String player_name;
	private int time;
	private float xG;
	private float npxG;
	private float xA;

	public int getTime() {
		return time;
	}

	public float getxG() {
		return xG;
	}

	public float getNpxG() {
		return npxG;
	}

	public float getxA() {
		return xA;
	}

	public String getPlayer_name() {
		return player_name;
	}

	public void setPlayer_name(String player_name) {
		this.player_name = FootballAnalysisUtil.stripAccents(player_name);
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

	@Override
	public String toString() {
		return "FBRefPlayerObject [player_name=" + player_name + ", time=" + time + ", xG=" + xG + ", npxG=" + npxG
				+ ", xA=" + xA + "]";
	}
}
