package ca.uleth.bugtriage.sibyl;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class NonStandardBugzilla {

	public static void main(String[] args) {
		int bugId = 331039;
		System.out.println("Getting " + bugId);
		HeuristicClassifier classifier = Heuristic.EVOLUTION.getClassifier();
		BugReport report = Utils.getReport(Project.EVOLUTION.getUrl(), Login.USER, Login.PASSWORD,
				bugId);
		Classification classification = classifier.classify(report);
		System.out.println(classification.getClassification());
	}
}
