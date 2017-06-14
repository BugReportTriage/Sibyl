package ca.uleth.bugtriage.sibyl.heuristic;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class SubcomponentHeuristic extends HeuristicClassifier {

	public static final String SUBCOMPONENT_PRESENT = "Present";

	public Classification classifyReport(BugReport report) {

		String subcomponent = report.subcomponent();
		if (subcomponent != null)
			return new Classification(subcomponent, "Subcomponent", 1);

		return new Classification(HeuristicClassifier.CANNOT_CLASSIFY, /*report
				.getSummary()*/ "No subcomponent found", 1);

	}

	public void printStats() {
		throw new UnsupportedOperationException();
	}

	public void resetStats() {
		throw new UnsupportedOperationException();
	}
}
