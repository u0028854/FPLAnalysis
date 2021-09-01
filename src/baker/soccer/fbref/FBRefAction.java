package baker.soccer.fbref;

import java.util.HashMap;

import org.apache.commons.text.StringEscapeUtils;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.CssSelectorNodeFilter;
import org.htmlparser.filters.HasChildFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;

import baker.soccer.fbref.objects.FBRefPlayerObject;
import baker.soccer.fbref.objects.FBRefTeamObject;
import baker.soccer.util.FootballAnalysisConstants;
import baker.soccer.util.FootballAnalysisUtil;

public class FBRefAction {

	public static void main(String[] args) throws Exception{
		System.out.print(processFBRefTeamHTML());
	}
	
	public static HashMap<String, FBRefPlayerObject> processFBRefPlayerHTML() throws Exception{
		FootballAnalysisUtil.removeHTMLComments(FootballAnalysisConstants.FBREFHTMLPLAYERFILENAME, FootballAnalysisConstants.FBREFPLAYEROUTPUTFILENAME);
		
		HashMap<String, FBRefPlayerObject> retVal = new HashMap<String, FBRefPlayerObject>();
		
		Parser parser = new Parser (FootballAnalysisConstants.FBREFPLAYEROUTPUTFILENAME);
		parser.setEncoding("UTF-8");

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		NodeFilter [] tempAndFilterArray = {new HasChildFilter(new OrFilter(new CssSelectorNodeFilter("td[data-stat=\"minutes\"]"), new OrFilter(new OrFilter(new CssSelectorNodeFilter("td[data-stat=\"player\"]"), new CssSelectorNodeFilter("td[data-stat=\"xa\"]")), new OrFilter(new CssSelectorNodeFilter("td[data-stat=\"xg\"]"),	new CssSelectorNodeFilter("td[data-stat=\"npxg\"]"))))),
				new HasParentFilter(new CssSelectorNodeFilter("table[class=\"min_width sortable stats_table min_width shade_zero\"]"), false), new TagNameFilter("tr")};
		nodes = parser.parse(new AndFilter(tempAndFilterArray));
		
		for (int i = 0; i < nodes.size(); i++){
			// Create data objects
			FBRefPlayerObject playerObject = new FBRefPlayerObject();

			org.htmlparser.util.NodeList tempNodeList = new org.htmlparser.util.NodeList();
			nodes.elementAt(i).collectInto(tempNodeList, 
					new OrFilter(new CssSelectorNodeFilter("td[data-stat=\"minutes\"]"), new OrFilter(new OrFilter(new CssSelectorNodeFilter("td[data-stat=\"player\"]"), new CssSelectorNodeFilter("td[data-stat=\"xa\"]")), new OrFilter(new CssSelectorNodeFilter("td[data-stat=\"xg\"]"), new CssSelectorNodeFilter("td[data-stat=\"npxg\"]")))));
			
			for (int j = 0; j < tempNodeList.size(); j++){
				// Get the nodes and value of attribute "title"
				Tag statElement = (Tag)tempNodeList.elementAt(j);
				String tempAttrValue = StringEscapeUtils.unescapeHtml4(statElement.getAttribute("data-stat"));

				if(tempAttrValue.equals("player")){
					playerObject.setPlayer_name(statElement.getFirstChild().getFirstChild() == null ? "" : statElement.getFirstChild().getFirstChild().getText());
				}
				else if(tempAttrValue.equals("xg")){
					playerObject.setxG(Float.parseFloat(statElement.getFirstChild() == null ? "0.0" : statElement.getFirstChild().getText()));
				}
				else if(tempAttrValue.equals("npxg")){
					playerObject.setNpxG(Float.parseFloat(statElement.getFirstChild() == null ? "0.0" : statElement.getFirstChild().getText()));
				}
				else if(tempAttrValue.equals("xa")){
					playerObject.setxA(Float.parseFloat(statElement.getFirstChild() == null ? "0.0" : statElement.getFirstChild().getText()));
				}
				else if(tempAttrValue.equals("minutes")){
					playerObject.setTime(Integer.parseInt(statElement.getFirstChild() == null ? "0" : statElement.getFirstChild().getText().replaceAll(",", "")));
				}
				else{
					System.err.println("At FBRefAction:processFBRefHTML() Bad data-stat value " + tempAttrValue);
				}
			}

			// If player played on two teams, add together
			if(retVal.containsKey(playerObject.getPlayer_name())){
				FBRefPlayerObject tempPlayerObject = retVal.get(playerObject.getPlayer_name());
				
				playerObject.setTime(playerObject.getTime() + tempPlayerObject.getTime());
				playerObject.setxG(playerObject.getxG() + tempPlayerObject.getxG());
				playerObject.setNpxG(playerObject.getNpxG() + tempPlayerObject.getNpxG());
				playerObject.setxA(playerObject.getxA() + tempPlayerObject.getxA());
			}
			
			retVal.put(playerObject.getPlayer_name(), playerObject);
		}

		parser.reset();
		return retVal;
	}
	
	public static HashMap<String, FBRefTeamObject> processFBRefTeamHTML() throws Exception{
		//FootballAnalysisUtil.removeHTMLComments(FootballAnalysisConstants.FBREFHTMLTEAMFILENAME, FootballAnalysisConstants.FBREFTEAMOUTPUTFILENAME);
		
		HashMap<String, FBRefTeamObject> retVal = new HashMap<String, FBRefTeamObject>();
		
		Parser parser = new Parser (FootballAnalysisConstants.FBREFHTMLTEAMFILENAME);
		parser.setEncoding("UTF-8");

		// Set up node collector
		org.htmlparser.util.NodeList nodes;

		NodeFilter [] tempAndFilterArray = {new HasParentFilter(new CssSelectorNodeFilter("td[data-stat=\"squad\"]")), new TagNameFilter("a")};
		nodes = parser.parse(new AndFilter(tempAndFilterArray));
		
		for (int i = 0; i < 20; i++){
			NodeList parentNode = nodes.elementAt(i).getParent().getParent().getChildren();

			// Create data objects
			FBRefTeamObject teamObject = new FBRefTeamObject();
			
			teamObject.setTeamName(parentNode.elementAt(1).getChildren().elementAt(2).getChildren().elementAt(0).getText().trim());
			teamObject.setxG(Float.parseFloat(parentNode.elementAt(10).getFirstChild() == null ? "0" : parentNode.elementAt(10).getFirstChild().getText()));
			teamObject.setxGC(Float.parseFloat(parentNode.elementAt(11).getFirstChild() == null ? "0" : parentNode.elementAt(11).getFirstChild().getText()));
			
			retVal.put(teamObject.getTeamName(), teamObject);
		}

		parser.reset();
		return retVal;
	}
}
