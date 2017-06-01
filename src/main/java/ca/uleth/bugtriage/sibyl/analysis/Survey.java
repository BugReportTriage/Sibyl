package ca.uleth.bugtriage.sibyl.analysis;

import java.util.Map;

public class Survey extends SubmissionEvent {

	public Map<String, String> getData(){
		return this.data;
	}
	
	public String componentNumRecommendations() {
		return this.getValue("componentNumRecommendations");
	}

	public String componentDifficulty() {
		return this.getValue("componentDifficulty");
	}

	public String componentReasoning() {
		return this.getValue("componentReasoning");
	}

	public boolean componentUsed() {
		return this.recommenderUsed("componentNotUsed");
	}

	public String subcomponentNumRecommendations() {
		return this.getValue("sub-componentNumRecommendations");
	}

	public String subcomponentDifficulty() {
		return this.getValue("sub-componentDifficulty");
	}

	public String subcomponentReasoning() {
		return this.getValue("sub-componentReasoning");
	}

	public String subcomponentMultiple() {
		return this.getValue("sub-componentMultiple");
	}

	public boolean subcomponentUsed() {
		return this.recommenderUsed("sub-componentNotUsed");
	}

	public String assignmentNumRecommendations() {
		return this.getValue("assignmentNumRecommendations");
	}

	public String assignmentDifficulty() {
		return this.getValue("assignmentDifficulty");
	}

	public String assignmentReasoning() {
		return this.getValue("assignmentReasoning");
	}

	public String assignmentMultiple() {
		return this.getValue("assignmentMultiple");
	}

	public boolean assignmentUsed() {
		return this.recommenderUsed("assignmentNotUsed");
	}

	private String getValue(String key) {
		String value = this.data.get(key);
		return (value != null) ? value : "";
	}

	private boolean recommenderUsed(String key) {
		return "yes".equals(this.data.get(key)) == false;
	}
}
