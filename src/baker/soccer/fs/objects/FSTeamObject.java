package baker.soccer.fs.objects;

import java.util.HashSet;
import java.util.Iterator;

public class FSTeamObject {
	private String teamName;
	private HashSet<FSTeamMatch> matchValues;
	private int maxGameWeek;

	public FSTeamObject(){
		this("", new HashSet<FSTeamMatch>());
	}

	public FSTeamObject(String teamName, FSTeamMatch matchValue){
		this.teamName = teamName;
		this.matchValues = new HashSet<FSTeamMatch>();
		this.matchValues.add(matchValue);

		checkMaxGameWeek(matchValue);
	}

	public FSTeamObject(String teamName, HashSet<FSTeamMatch> matchValues){
		this.teamName = teamName;
		this.matchValues = matchValues;

		checkMaxGameWeek(matchValues);
	}

	public String getMaxGameWeek() {
		return Integer.toString(maxGameWeek);
	}

	public void setMaxGameWeek(String maxGameWeek) {
		this.maxGameWeek = Integer.parseInt(maxGameWeek);
	}

	public HashSet<FSTeamMatch> getMatchValues() {
		return matchValues;
	}

	public void setMatchValues(HashSet<FSTeamMatch> matchValues) {
		this.matchValues = matchValues;

		checkMaxGameWeek(matchValues);
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public boolean addMatchValue(FSTeamMatch matchValue){
		boolean retVal = false;
		retVal = this.matchValues.add(matchValue);

		checkMaxGameWeek(matchValue);

		return retVal;
	}

	public boolean addAllMatchValues(HashSet<FSTeamMatch> matchValues){
		boolean retVal = false;
		retVal = this.matchValues.addAll(matchValues);

		checkMaxGameWeek(matchValues);

		return  retVal;
	}

	private void checkMaxGameWeek(HashSet<FSTeamMatch> matchValues){
		Iterator<FSTeamMatch> temp = matchValues.iterator();
		int tempGW;

		while(temp.hasNext()){
			if ((tempGW = Integer.parseInt(temp.next().getGameWeek())) > maxGameWeek) maxGameWeek = tempGW;
		}	
	}

	private void checkMaxGameWeek(FSTeamMatch matchValue){
		int temp;

		if ((temp = Integer.parseInt(matchValue.getGameWeek())) > maxGameWeek) maxGameWeek = temp;
	}

	@Override
	public String toString() {
		return "FSTeamObject [teamName=" + teamName + ", matchValues="
				+ matchValues + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((teamName == null) ? 0 : teamName.hashCode());
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
		FSTeamObject other = (FSTeamObject) obj;
		if (teamName == null) {
			if (other.teamName != null)
				return false;
		} else if (!teamName.equals(other.teamName))
			return false;
		return true;
	}
}
