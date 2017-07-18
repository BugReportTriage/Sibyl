package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class HeuristicRewardFunction implements RewardFunction {

    private final HeuristicClassifier classifier;
    
    public HeuristicRewardFunction(HeuristicClassifier hClassifier) {
	classifier = hClassifier;
    }
    
    @Override
    public double reward(State state, Action action, State nextState) {
	System.out.print("Determining reward...");
	BugTriageAction triage = (BugTriageAction) action;	
	BugReport report = (BugReport)state.get(BugTriageState.NEW_REPORT);
	String label = classifier.classify(report).getClassification();
	
	System.out.println(label + " <--> " + triage.getRecommendation());
	
	int score = 0;
	if(triage.getRecommendation().equals(label))
	    score = 100;
		
	return score;
    }

}
