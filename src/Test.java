import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Random;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.io.*;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

import javax.script.*;

import baker.soccer.FootballAnalysisAction;
import baker.soccer.fpl.FPLUtil;
import baker.soccer.util.FootballAnalysisConstants;
import baker.soccer.util.FootballAnalysisUtil;

public class Test{
	public static void main(String args[]) throws Exception{
		/*HttpURLConnection con = null;
		String urlParameters = "username=SuperGrover&password=u0028854&url=members.fantasyfootballscout.co.uk&login=Login";

		byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

		try {
			CookieHandler.setDefault( new CookieManager( null, CookiePolicy.ACCEPT_ALL ) );
			URL myurl = new URL("https://members.fantasyfootballscout.co.uk/");
			con = (HttpURLConnection) myurl.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", "PostmanRuntime/7.15.2");
			con.setRequestProperty("Content-Type", "multipart/form-data; boundary=--------------------------904586797819126295023254");
			con.setRequestProperty("username", "SuperGrover");
			con.setRequestProperty("password", "u0028854");

			try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
				wr.write(postData);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) 
				System.out.println(inputLine);
			
			in.close();
		}
		catch(Exception e){
			System.err.println(e);
		}
		finally {
			con.disconnect();
		}*/
		
		/*ArrayList<java.util.Date> temp = FootballAnalysisConstants.FPL_GAMEWEEK_DATES;
		
		for(int i=0; i < temp.size(); i++){
			System.out.println(i + ": " + temp.get(i));
		}*/

		//System.out.println(FootballAnalysisUtil.getGameweekStart(100));

		//FootballAnalysisAction.exportTeamTableData(FootballAnalysisAction.processFantScoutTeamStatTablesWorker(FootballAnalysisConstants.TEAM_STATS_TABLE1));
		/*System.out.println(new java.util.Date(Long.parseLong("1609128000000")));
		System.out.println(FootballAnalysisUtil.getGameweekStart(6));
		
		SimpleDateFormat temp = new SimpleDateFormat("YYYY-MM-dd");
		temp.setLenient(false);
		Calendar cal = temp.getCalendar();
		cal.setTime(FootballAnalysisUtil.getGameweekStart(6));
		System.out.println(cal.getTime());
		System.out.println(temp.format(cal.getTime()));
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(FootballAnalysisUtil.getGameweekStart(6)) + "+00%3A00%3A00");*/
		
//		Random random = new Random(new java.util.Date().getTime());
//		int iterationCount = random.nextInt() % 10;
//		
//		ConcurrentSkipListMap<Float, String> leagueList = new ConcurrentSkipListMap<Float, String>();
//		String[] teamList = {"All That Ales You","stpauler","Wiseowl65","Sir Patrick Bamford"};
//		Random rand = new Random(new java.util.Date().getTime());
//		
//		for (int i = 0; i < teamList.length; i++){
//			for(int j = 0; j < iterationCount; j++) rand.nextFloat();
//			leagueList.put(rand.nextFloat(), teamList[i]);
//		}
//		
//		Iterator<Float> orderedTeams = leagueList.descendingKeySet().iterator();
//		
//		while(orderedTeams.hasNext())
//			System.out.println(leagueList.get(orderedTeams.next()));
		
		System.out.println(FootballAnalysisConstants.FPLPLAYERJSON);
	}
}