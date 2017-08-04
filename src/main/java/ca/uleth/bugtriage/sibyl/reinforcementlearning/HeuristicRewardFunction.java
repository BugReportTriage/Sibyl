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
		// System.out.print("Determining reward...");
		BugTriageAction triage = (BugTriageAction) action;
		Instance report = (Instance) nextState.get(BugReportTerm.INSTANCE);
		String label = report.toString(report.classAttribute());

		// System.out.println(label + " (Actual) <--> (Rec)" +
		// triage.getRecommendation());

		double score = 0;
		if (triage != null) {
			String correct = triage.getRecommendation();
			if (label.equals(correct)) {
				double terms = numTerms(report);
				score = 1 / terms;
			}
		}
		return score;
	}

	private int numTerms(Instance report) {
		int numTerms = 0;
		for (int i = 0; i < report.numAttributes(); i++) {
			if (i == report.classIndex())
				continue;
			if (report.value(i) > 0) {
				numTerms++;
			}
		}
		return numTerms;
	}
}
