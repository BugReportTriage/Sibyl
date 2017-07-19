package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.action.Action;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageAction implements Action {

    private final String recommendation;    
    
    public BugTriageAction(String recommendation) {
	this.recommendation = recommendation;
	
    }
    
    @Override
    public String actionName() {	
	return "New Report Recommendation";
    }

    @Override
    public Action copy() {
	return new BugTriageAction(getRecommendation());
    }

    public String getRecommendation() {
	return recommendation;
    }
}
