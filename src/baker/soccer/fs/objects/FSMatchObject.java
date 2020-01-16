package baker.soccer.fs.objects;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import baker.soccer.util.FootballAnalysisConstants;
import baker.soccer.util.FootballAnalysisUtil;

public class FSMatchObject{
	private Date matchDate;
	private int matchSeason;
	private String gameWeek;
	private String matchName;
	private String matchLocation;
	private HashMap<String,Float> matchValues;

	public FSMatchObject(){
		matchValues = new HashMap<String,Float>();
	}

	public Date getMatchDate() {
		return matchDate;
	}

	public void setMatchDate(Date matchDate) {
		this.matchDate = matchDate;

		this.setMatchSeason(FootballAnalysisUtil.getMatchSeason(this.matchDate));
	}

	public int getMatchSeason() {
		return matchSeason;
	}

	public void setMatchSeason(int matchSeason) {
		this.matchSeason = matchSeason;
	}

	public String getGameWeek() {
		return gameWeek;
	}

	public void setGameWeek(String gameWeek) {
		this.gameWeek = gameWeek;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}

	public String getMatchLocation() {
		return matchLocation;
	}

	public void setMatchLocation(String matchLocation) {
		this.matchLocation = matchLocation;
	}

	public HashMap<String, Float> getMatchValues() {
		return matchValues;
	}

	public void setMatchValues(HashMap<String, Float> matchValues) {
		this.matchValues = matchValues;
	}

	public void addMatchValue(String key, Float value){
		matchValues.put(key, value);
	}

	public void addAllMatchValues(HashMap<String,Float> matchValues){
		this.matchValues.putAll(matchValues);
	}

	public Float getMatchValue(String key) {
		return matchValues.get(key);
	}

	public HashMap<String,Float> getPrunedMatchValues(String option){
		HashMap<String,Float> retVal = matchValues;

		ArrayList<String> dbStatList = option.equals(FootballAnalysisConstants.MATCH_PLAYERS_PRUNED_VALUES) ? FootballAnalysisConstants.DBPLAYERSTATLIST : FootballAnalysisConstants.DBTEAMSTATLIST;
		Iterator<String> iterator = retVal.keySet().iterator();

		while(iterator.hasNext()){
			if(!dbStatList.contains(iterator.next()))
				iterator.remove();
		}

		return retVal;
	}

	public HashMap<String,String> getDatabaseInsertValues(String option){
		HashMap<String,String> retVal = new HashMap<String, String>();
		HashMap<String,Float> prunedMatchValues = getPrunedMatchValues(option);

		Calendar matchDateFormatter = Calendar.getInstance();
		matchDateFormatter.setTime(matchDate);

		NumberFormat nf = NumberFormat.getInstance(); 
		nf.setMinimumIntegerDigits(2);  

		retVal.put("MATCH_DATE", matchDateFormatter.get(Calendar.YEAR) + "-" + nf.format(matchDateFormatter.get(Calendar.MONTH)) + "-" + nf.format(matchDateFormatter.get(Calendar.DAY_OF_MONTH)) + "T" + nf.format(matchDateFormatter.get(Calendar.HOUR_OF_DAY)) + ":" + nf.format(matchDateFormatter.get(Calendar.MINUTE)) + ":" + nf.format(matchDateFormatter.get(Calendar.SECOND)) + "-6:00");
		retVal.put("MATCH_SEASON", Integer.toString(matchSeason));
		retVal.put("GAME_WEEK", gameWeek);
		retVal.put("MATCH_NAME", matchName);
		retVal.put("MATCH_LOCATION", matchLocation);

		Iterator<String> iterator = prunedMatchValues.keySet().iterator();

		while (iterator.hasNext()){
			String keyValue = iterator.next();
			retVal.put(keyValue, prunedMatchValues.get(keyValue).toString());
		}

		return retVal;

	}

	@Override
	public String toString() {
		return "FSMatchObject [matchDate=" + matchDate + ", matchSeason=" + matchSeason + ", gameWeek=" + gameWeek
				+ ", matchName=" + matchName + ", matchLocation=" + matchLocation + ", matchValues=" + matchValues
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gameWeek == null) ? 0 : gameWeek.hashCode());
		result = prime * result + ((matchDate == null) ? 0 : matchDate.hashCode());
		result = prime * result + ((matchLocation == null) ? 0 : matchLocation.hashCode());
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
		FSMatchObject other = (FSMatchObject) obj;
		if (gameWeek == null) {
			if (other.gameWeek != null)
				return false;
		} else if (!gameWeek.equals(other.gameWeek))
			return false;
		if (matchDate == null) {
			if (other.matchDate != null)
				return false;
		} else if (!matchDate.equals(other.matchDate))
			return false;
		if (matchLocation == null) {
			if (other.matchLocation != null)
				return false;
		} else if (!matchLocation.equals(other.matchLocation))
			return false;
		return true;
	}
}
