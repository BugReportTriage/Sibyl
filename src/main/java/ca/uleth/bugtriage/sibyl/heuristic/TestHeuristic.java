package ca.uleth.bugtriage.sibyl.heuristic;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class TestHeuristic extends HeuristicClassifier {

	@Override
	public Classification classifyReport(BugReport report) {
		return new Classification("janvik@cs.ubc.ca", "Test classification", 1);
	}
}
