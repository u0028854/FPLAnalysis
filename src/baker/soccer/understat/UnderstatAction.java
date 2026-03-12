package baker.soccer.understat;

import java.util.ArrayList;
import java.util.HashMap;

import baker.soccer.understat.objects.UnderstatPlayerObject;
import baker.soccer.understat.objects.UnderstatTeamObject;
import baker.soccer.util.FootballAnalysisConstants;
import baker.soccer.util.FootballAnalysisUtil;

public class UnderstatAction {
	public static void main(String [] args) throws Exception{}

	public static HashMap<String, UnderstatPlayerObject> processUnderstatXGPlayerJSON(int eplSeason, String option) throws Exception{
		return processPlayerJSON(eplSeason, option);
	}

	public static HashMap<String, UnderstatTeamObject> processUnderstatXGTeamJSON() throws Exception{
		return processTeamJSON(FootballAnalysisConstants.USTEAMOUTPUTFILENAME);
	}

	private static HashMap<String, UnderstatPlayerObject> processPlayerJSON(int eplSeason, String option) throws Exception{
		HashMap<String, UnderstatPlayerObject> retVal = new HashMap<String, UnderstatPlayerObject>();
		String fileName = "";

		if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_ARG)){
			fileName = FootballAnalysisConstants.USPLAYERINPUTFILENAME;
		}
		else if(option.equalsIgnoreCase(FootballAnalysisConstants.FS_PLAYER_ANALYSIS_EXCEL_6GW_ARG)){
			fileName = FootballAnalysisConstants.USPLAYERINPUT6GWFILENAME;
		}		

		ArrayList<String> fileData = FootballAnalysisUtil.getFileDataByLine(fileName);

		for (int i = 0; i < fileData.size(); i++){
			String [] fileVals = fileData.get(i).split(",");
						
			if (fileVals.length == 7 || fileVals.length == 8){
				retVal.put(
						fileVals[0], 
						new UnderstatPlayerObject(
								fileVals[0], 
								fileVals[1], 
								fileVals[2], 
								fileVals[3], 
								fileVals[4], 
								fileVals[5],
								fileVals[6]));
			}
			else{
				for(int j = 0; j < fileVals.length; j++){
					System.out.println(j + " : " + fileVals[j]);
				}
				throw new Exception("FileVals length is " + fileVals.length);
			}
		}

		return retVal;
	}

	private static HashMap<String, UnderstatTeamObject> processTeamJSON(String jsonInputFile) throws Exception{
		HashMap<String, UnderstatTeamObject> retVal = new HashMap<String, UnderstatTeamObject>();
		UnderstatTeamObject newTeamObject;	

		ArrayList<String> fileData = FootballAnalysisUtil.getFileDataByLine(jsonInputFile);

		for (int i = 0; i < fileData.size(); i++){
			String [] fileVals = fileData.get(i).split(",");
						
			if (fileVals.length == 5){
				newTeamObject = new UnderstatTeamObject(fileVals[0]);
				newTeamObject.setXG(Float.parseFloat(fileVals[1]));
				newTeamObject.setXGA(Float.parseFloat(fileVals[2]));
				newTeamObject.setSixGWXG(Float.parseFloat(fileVals[3]));
				newTeamObject.setSixGWXGA(Float.parseFloat(fileVals[4]));

				retVal.put(
					newTeamObject.getTeamName(), 
					newTeamObject
				);				
			}
			else{
				for(int j = 0; j < fileVals.length; j++){
					System.out.println(j + " : " + fileVals[j]);
				}
				throw new Exception("FileVals length is " + fileVals.length);
			}
		}

		return retVal;
	}
}
