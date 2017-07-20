package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.List;

import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageQLearning {

    private static final int NUM_REPORTS = 2000;
    private static final int FACTOR = 2;

    public static void main(String[] args) {

	Dataset dataset = new BugzillaDataset(Project.FIREFOX);
	List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports());//.subList(0, NUM_REPORTS);

	BugTriageDomain triageDomain = new BugTriageDomain(Project.FIREFOX, reports);
	triageDomain.init();

	for (String term : triageDomain.getTerms()) {
	    //System.out.println(term);
	}

	ActionType assignment = new BugReportAssignment(triageDomain);
	triageDomain.addActionType(assignment);

	BugTriageStateModel model = new BugTriageStateModel(triageDomain.getInstances());
	RewardFunction rf = new HeuristicRewardFunction();
	TerminalFunction tf = new BugTriageTerminalFunction(model);

	triageDomain.setModel(new FactoredModel(model, rf, tf));
	
	LearningAgent agent = new QLearning(triageDomain, 0.99, new SimpleHashableStateFactory(), 0.0, 1.0);

	State start = model.sample(null, null); // Parameters not used
	SimulatedEnvironment env = new SimulatedEnvironment(triageDomain, start);

	for (int j = 0; j < 10; j++) {
	    Episode e = null;
	    int totalCorrect = 0;
	    for (int i = 0; i < reports.size() * FACTOR; i++) {	    
		e = agent.runLearningEpisode(env);

		// reset environment for next learning episode
		env.resetEnvironment();
		model.init();
	    }
	    for (double reward : e.rewardSequence) {
		totalCorrect += reward;
	    }
	    System.out.println(totalCorrect + "/" + reports.size() + "(" + totalCorrect * 100 / reports.size() + "%)");
	}
    }
}
