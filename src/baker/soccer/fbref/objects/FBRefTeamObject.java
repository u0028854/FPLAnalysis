package baker.soccer.fbref.objects;

import java.util.ArrayList;
import java.util.Iterator;

public class FBRefTeamObject {
	private String teamName;
	private ArrayList<xGResultsObject> results;

	public FBRefTeamObject(String teamName) {
		super();
		this.teamName = teamName.replace("\"", "");
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public ArrayList<xGResultsObject> getResults() {
		return results;
	}

	public void setResults(ArrayList<xGResultsObject> results) {
		this.results = results;
	}

	public void addResult(float xG, float xGA) {
		if(results == null)
			results = new ArrayList<xGResultsObject>();

			results.add(new xGResultsObject(xG, xGA));
	}

	public float sumXG(){
		float retVal = 0.0f;

		if(results != null){

			Iterator<xGResultsObject> iterator = results.iterator();

			while (iterator.hasNext())
				retVal += iterator.next().getxG();
		}

		return retVal;
	}

	public float sumXGA(){
		float retVal = 0.0f;

		if(results != null){

			Iterator<xGResultsObject> iterator = results.iterator();

			while (iterator.hasNext())
				retVal += iterator.next().getxGA();
		}

		return retVal;
	}

	private class xGResultsObject{
		private java.util.Date matchDate;
		private float xG;
		private float xGA;

		public java.util.Date getMatchDate() {
			return matchDate;
		}

		public void setMatchDate(java.util.Date matchDate) {
			this.matchDate = matchDate;
		}

		xGResultsObject(float xG, float xGA){
			this.xG = xG;
			this.xGA = xGA;
		}

		public float getxG() {
			return xG;
		}

		public float getxGA() {
			return xGA;
		}

		@Override
		public String toString() {
			return "xGResultsObject [matchDate=" + matchDate + ", xG=" + xG + ", xGA=" + xGA + "]";
		}
	}

	@Override
	public String toString() {
		return "UnderstatTeamObject [teamName=" + teamName + ", results=" + results + "]";
	}

}
