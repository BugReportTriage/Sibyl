package ca.uleth.bugtriage.sibyl.heuristic;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class ComponentHeuristic extends HeuristicClassifier {

	public Classification classifyReport(BugReport report) {

		Classification classification;
		//System.out.println(report.getResolution());
		if (report.getResolution().equals("INVALID")) {
			classification = new Classification(CANNOT_CLASSIFY,
					"Not Reliable Report", 1);
		}else{
			String component = report.getComponent();
			classification = new Classification(component,
					"Assigned Component", 1);
		} 
		return classification;
	}

	public void printStats() {
		throw new UnsupportedOperationException();
	}

	public void resetStats() {
		throw new UnsupportedOperationException();
	}
}
