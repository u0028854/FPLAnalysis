package baker.soccer.fpl.objects;

import baker.soccer.util.FootballAnalysisUtil;

public class FPLPlayerObject {
	private String first_name;
	private String second_name;
	private int assists;
	private int bonus;
	private int bps;
	private int clean_sheets;
	private int cost_change_start;
	private int goals_conceded;
	private int goals_scored;
	private int minutes;
	private int now_cost;
	private int own_goals;
	private int penalties_missed;
	private int penalties_saved;
	private int red_cards;
	private int saves;
	private String selected_by_percent;
	private int team;
	private int total_points;
	private int yellow_cards;

	public FPLPlayerObject(){}

	public int getAssists() {
		return assists;
	}
	public void setAssists(int assists) {
		this.assists = assists;
	}
	public int getBonus() {
		return bonus;
	}
	public void setBonus(int bonus) {
		this.bonus = bonus;
	}
	public int getBps() {
		return bps;
	}
	public void setBps(int bps) {
		this.bps = bps;
	}
	public int getClean_sheets() {
		return clean_sheets;
	}
	public void setClean_sheets(int clean_sheets) {
		this.clean_sheets = clean_sheets;
	}
	public int getCost_change_start() {
		return cost_change_start;
	}
	public void setCost_change_start(int cost_change_start) {
		this.cost_change_start = cost_change_start;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public int getGoals_conceded() {
		return goals_conceded;
	}
	public void setGoals_conceded(int goals_conceded) {
		this.goals_conceded = goals_conceded;
	}
	public int getGoals_scored() {
		return goals_scored;
	}
	public void setGoals_scored(int goals_scored) {
		this.goals_scored = goals_scored;
	}
	public int getMinutes() {
		return minutes;
	}
	public void setMinutes(int minutes) {
		this.minutes = minutes;
	}
	public float getNow_cost() {
		return now_cost / 10.0f;
	}
	public void setNow_cost(int now_cost) {
		this.now_cost = now_cost;
	}
	public int getOwn_goals() {
		return own_goals;
	}
	public void setOwn_goals(int own_goals) {
		this.own_goals = own_goals;
	}
	public int getPenalties_missed() {
		return penalties_missed;
	}
	public void setPenalties_missed(int penalties_missed) {
		this.penalties_missed = penalties_missed;
	}
	public int getPenalties_saved() {
		return penalties_saved;
	}
	public void setPenalties_saved(int penalties_saved) {
		this.penalties_saved = penalties_saved;
	}
	public int getRed_cards() {
		return red_cards;
	}
	public void setRed_cards(int red_cards) {
		this.red_cards = red_cards;
	}
	public int getSaves() {
		return saves;
	}
	public void setSaves(int saves) {
		this.saves = saves;
	}
	public String getSecond_name() {
		return second_name;
	}
	public void setSecond_name(String second_name) {
		this.second_name = second_name;
	}
	public float getSelected_by_percent() {
		return Float.parseFloat(selected_by_percent);
	}
	public void setSelected_by_percent(String selected_by_percent) {
		this.selected_by_percent = selected_by_percent;
	}
	public String getTeam() {
		switch(team){
		case(1):
			return "ARS";
		case(2):
			return "AVL";
		case(3):
			return "BOU";
		case(4):
			return "BHA";
		case(5):
			return "BUR";
		case(6):
			return "CHE";
		case(7):
			return "CRY";
		case(8):
			return "EVE";
		case(9):
			return "LEI";
		case(10):
			return "LIV";
		case(11):
			return "MCI";
		case(12):
			return "MUN";
		case(13):
			return "NEW";
		case(14):
			return "NOR";
		case(15):
			return "SHU";
		case(16):
			return "SOU";
		case(17):
			return "TOT";
		case(18):
			return "WAT";
		case(19):
			return "WHU";
		case(20):
			return "WOL";
		default:
			return "ERROR";
		}
	}

	public void setTeam(int team) {
		this.team = team;
	}

	public int getTotal_points() {
		return total_points;
	}
	public void setTotal_points(int total_points) {
		this.total_points = total_points;
	}
	public int getYellow_cards() {
		return yellow_cards;
	}
	public void setYellow_cards(int yellow_cards) {
		this.yellow_cards = yellow_cards;
	}

	public String getPlayerName(){
		return first_name + " " + second_name;
	}
	
	public void cleanName(){
		this.first_name = FootballAnalysisUtil.convertCharsetChars(this.first_name);
		this.second_name = FootballAnalysisUtil.convertCharsetChars(this.second_name);
	}
	
	@Override
	public String toString() {
		return "FPLPlayerObject [first_name=" + first_name + ", second_name=" + second_name + ", assists="
				+ assists + ", bonus=" + bonus + ", bps=" + bps + ", clean_sheets=" + clean_sheets
				+ ", cost_change_start=" + cost_change_start + ", goals_conceded=" + goals_conceded
				+ ", goals_scored=" + goals_scored + ", minutes=" + minutes + ", now_cost=" + now_cost
				+ ", own_goals=" + own_goals + ", penalties_missed=" + penalties_missed + ", penalties_saved="
				+ penalties_saved + ", red_cards=" + red_cards + ", saves=" + saves + ", selected_by_percent="
				+ selected_by_percent + ", team=" + team + ", total_points=" + total_points + ", yellow_cards="
				+ yellow_cards + "]";
	}
}
