package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import weka.core.Instance;

public class HeuristicRewardFunction implements RewardFunction {
   
    @Override
    public double reward(State state, Action action, State nextState) {
	//System.out.print("Determining reward...");
	BugTriageAction triage = (BugTriageAction) action;	
	Instance report = (Instance)nextState.get(BugReportText.INSTANCE);
	String label = report.toString(report.classAttribute());
	
	//System.out.println(label + " (Actual) <--> (Rec)" + triage.getRecommendation());
	
	int score = 0;
	if(triage.getRecommendation().equals(label))
	    score = 1;
		
	return score;
    }

}
