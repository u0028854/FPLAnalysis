package baker.soccer.fs.objects;

import java.util.HashMap;
import java.util.Iterator;

import baker.soccer.util.FootballAnalysisConstants;

public class FSTeamMatch extends FSMatchObject{
	private FSTeamMatch opponentMatch;

	public FSTeamMatch(){
		super();
	}

	public String getTeamName() {
		return super.getMatchName();
	}

	public void setTeamName(String teamName) {
		super.setMatchName(teamName);
	}

	public FSTeamMatch getOpponentMatch() {
		return opponentMatch;
	}

	public void setOpponentMatch(FSTeamMatch opponentMatch) {
		this.opponentMatch = opponentMatch;
	}

	public HashMap<String, String> getDatabaseInsertValues(){
		HashMap<String, String> retVal = super.getDatabaseInsertValues(FootballAnalysisConstants.MATCH_TEAM_PRUNED_VALUES);
		HashMap<String,Float> prunedMatchValues = getPrunedMatchValues(FootballAnalysisConstants.MATCH_TEAM_PRUNED_VALUES);

		retVal.put("OPP_NAME", opponentMatch.getTeamName());

		Iterator<String> iterator = prunedMatchValues.keySet().iterator();

		while (iterator.hasNext()){
			String keyValue = iterator.next();
			retVal.put("OPP_" + keyValue, opponentMatch.getMatchValues().get(keyValue).toString());
		}

		return retVal;
	}

	@Override
	public String toString() {
		return "FSTeamMatch [opponentMatch=" + opponentMatch.getTeamName() + ", toString()="
				+ super.toString() + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((opponentMatch == null) ? 0 : opponentMatch.getTeamName().hashCode());
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
		FSTeamMatch other = (FSTeamMatch) obj;
		if (opponentMatch == null) {
			if (other.opponentMatch != null)
				return false;
		} else if (!opponentMatch.equals(other.opponentMatch))
			return false;
		return true;
	}
}
