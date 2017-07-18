package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageQLearning {

    public static void main(String[] args) {

	Dataset dataset = new BugzillaDataset(Project.FIREFOX);
	List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports()).subList(0, 10);

	SADomain triageDomain = new SADomain();
	ActionType assignment = new BugReportAssignment();
	triageDomain.addActionType(assignment);

	BugTriageStateModel model = new BugTriageStateModel(reports);
	RewardFunction rf = new HeuristicRewardFunction(Project.FIREFOX.heuristic.getClassifier());
	TerminalFunction tf = new BugTriageTerminalFunction(reports.get(reports.size()-1));

	triageDomain.setModel(new FactoredModel(model, rf, tf));

	LearningAgent agent = new QLearning(triageDomain, 0.99, new SimpleHashableStateFactory(), 0.0, 1.0);

	//BugTriageEnvironment env = new BugTriageEnvironment(triageDomain, new BugTriageStateGenerator(reports));
	SimulatedEnvironment env = new SimulatedEnvironment(triageDomain, new BugTriageState(reports.get(0)));
	
	// while(brIter.hasNext()){
	for (int i = 0; i < 50; i++) {
	    Episode e = agent.runLearningEpisode(env);
	    System.out.println(i + ": " + e.maxTimeStep());

	    // reset environment for next learning episode
	    env.resetEnvironment();
	    model.init();
	}
    }
}
