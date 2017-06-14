package ca.uleth.bugtriage.sibyl.heuristic;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class WontFixHeuristic extends HeuristicClassifier {

	public static final String NEGATIVE = "Wont Fix";
	public static final String POSITIVE = "Will Fix";

	public Classification classifyReport(BugReport report) {

		Classification classification;
		//System.out.println(report.getResolution());
		if (report.getResolution().equals("INVALID") || report.getResolution().equals("WORKSFORME") || report.getResolution().equals("LATER") || report.getResolution().equals("REMIND") || report.getResolution().equals("DUPLICATE")) {
			classification = new Classification(CANNOT_CLASSIFY,
					"Not Reliable Report", 1);
		}else{
			String status = report.getResolution().toString();
			System.out.print(status + " --> ");
			if(status.toLowerCase().equals("WONTFIX".toLowerCase())){
			classification = new Classification(WontFixHeuristic.NEGATIVE,
					"Wont Fix", 1);
			System.out.println(WontFixHeuristic.NEGATIVE);
			}else if(status.toLowerCase().equals("FIXED".toLowerCase())){
				classification = new Classification(WontFixHeuristic.POSITIVE,
						"Will Fix", 1);
			System.out.println(WontFixHeuristic.POSITIVE);
			}else if(status.toLowerCase().equals("ASSIGNED".toLowerCase())){
				classification = new Classification(CANNOT_CLASSIFY,
						"Will Fix", 1);
			System.out.println(WontFixHeuristic.POSITIVE);
			}else{
				classification = new Classification(CANNOT_CLASSIFY,
						"Failure", 1);
			System.out.println("invalid");
			}
		
		
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
