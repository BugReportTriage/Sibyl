package ca.uleth.bugtriage.sibyl.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;

public class ChangeEvent extends SubmissionEvent {

	@Override
	public String toString() {
		return this.reportId() + "(CE):" + this.getDate();
	}

	private BugActivity activityLog;

	public boolean commentAdded() {
		return this.data.containsKey("comment");
	}

	public String reportId() {
		return this.data.get("id");
	}

	public int assignmentRank() {
		
		List<String> recommendations = null;//this.assignmentRecommendations();
	
		/*
		if(recommendations.size() == 1 && recommendations.get(0).contains("Control Report")) {
			return 0;
		}
		*/
		return this.rank(this.activityLog.getAllAssignedTo(), recommendations);
	}

	public int componentRank() {
		return this.rank(this.activityLog.getComponentChanges(), null /*this.componentRecommendations()*/);
	}

	public int subcomponentRank() {
		
		if(this.product().equals("Platform") == false) {
			System.err.println("Subcomponent Rank: Not \"Platform\"");
			return 0;
		}
		
		/*
		 * Developers are not always consistant in spelling of subcomponents
		 */
		List<String> subcomponentChanges = new ArrayList<String>();
		List<String> recommendations = new ArrayList<String>();
		for(String change : this.activityLog.getSubcomponentChanges()){
			// remove spaces and put all in same case
			subcomponentChanges.add(change.replace(" ","").toLowerCase()); 
		}
		/*
		for(String recommendation : this.subcomponentRecommendations()){
			// remove spaces and put all in same case
			recommendations.add(recommendation.replace(" ","").toLowerCase()); 
		}*/
		return this.rank(subcomponentChanges, recommendations);
	}
	
	private int rank(List<String> changes, List<String> recommendations) {
		if (changes.isEmpty()) {
			return 0;
		}
		
		//System.out.println("Checking: " + this.reportId());

		int rank = 0;
		for (String recommendation : recommendations) {
			rank++;
			if (changes.contains(recommendation))
				return rank;
		}
		return -1;
	}

	public List<String> assignmentRecommendations() {
		return this
				.recommendations(""/*BugzillaPage.LOGGED_DEVELOPER_RECOMMENDATION_KEY*/);
	}

	public List<String> componentRecommendations() {
		return this
				.recommendations(""/*BugzillaPage.LOGGED_COMPONENT_RECOMMENDATION_KEY*/);
	}

	public List<String> subcomponentRecommendations() {
		return this
				.recommendations(""/*BugzillaPage.LOGGED_SUBCOMPONENT_RECOMMENDATION_KEY*/);
	}
	
	public List<String> ccRecommendations() {
		return this
				.recommendations(""/*BugzillaPage.LOGGED_CC_RECOMMENDATION_KEY*/);
	}

	private List<String> recommendations(String formKey) {
		List<String> recommendations = new ArrayList<String>();
		List<String> keys = new ArrayList<String>();
		for (String key : this.data.keySet()) {
			if (key.startsWith(formKey)) {
				keys.add(key);
			}
		}

		Collections.sort(keys, new RecommendationKeyComparator());

		for (String key : keys) {
			recommendations.add(this.data.get(key));
		}

		return recommendations;
	}

	public void setActivityLog(BugActivity activityLog) {
		this.activityLog = activityLog;
	}

	class RecommendationKeyComparator implements Comparator<String> {

		public int compare(String key1, String key2) {
			Pattern pattern = Pattern.compile("\\d+");
			Matcher matcherKey1 = pattern.matcher(key1 + "");
			Matcher matcherKey2 = pattern.matcher(key2 + "");

			String key1RankStr = null, key2RankStr = null;
			int key1Rank = 0, key2Rank = 0;
			if (matcherKey1.find()) {
				key1RankStr = matcherKey1.group();
				key1Rank = Integer.parseInt(key1RankStr);
			}

			if (matcherKey2.find()) {
				key2RankStr = matcherKey2.group();
				key2Rank = Integer.parseInt(key2RankStr);
			}

			return (key1Rank > key2Rank) ? 1 : ((key1Rank < key2Rank) ? -1 : 0);
		}

	}

	public String assigned() {
		return this.data.get("assigned_to");
	}

	public String ccAdded() {
		String ccAdded = this.data.get("newcc");
		return (ccAdded != null) ? ccAdded : "";
	}

	public BugActivity getActivityLog() {
		return activityLog;
	}

	public String component() {
		return this.data.get("component");
		
	}
	
	public String product() {
		return this.data.get("product");
		
	}

	public int ccRank() {
		return this.rank(this.activityLog.getCCAdded(), this.ccRecommendations());
	}

}
