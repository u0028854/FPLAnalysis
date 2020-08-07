import org.apache.commons.lang3.StringUtils;

import baker.soccer.FootballAnalysisAction;
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

		FootballAnalysisAction.exportTeamTableData(FootballAnalysisAction.processFantScoutTeamStatTablesWorker(FootballAnalysisConstants.TEAM_STATS_TABLE1));
	}
}