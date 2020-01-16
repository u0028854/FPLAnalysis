import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

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
		
		TreeMap<Integer,java.util.Date> temp = FootballAnalysisUtil.getGameweekDates();
		
		Iterator<Integer> iterator = temp.keySet().iterator();
		
		while(iterator.hasNext()){
			System.out.println(temp.get(iterator.next()));
		}
	}
}