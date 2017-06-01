package ca.uleth.bugtriage.sibyl.heuristic;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class TestEvolutionHeuristicOptimized {

	private HeuristicClassifier heuristicClassifier;

	@Before
	public void setUp() throws Exception {
		this.heuristicClassifier = Heuristic.EVOLUTION.getClassifier();
	}

	@Test
	public void testAssigned() {

		List<ClassificationTestReport> tests = new ArrayList<ClassificationTestReport>();
		tests.add(new ClassificationTestReport(274234, "evolution-calendar-maintainers@ximian.com"));

		runTests(tests);
	}

	private void runTests(List<ClassificationTestReport> tests) {
		BugReport report;
		String classification;
		for (ClassificationTestReport test : tests) {
			
			System.out.println("Getting report " + test.reportId);
			report = Utils.getReport(Project.EVOLUTION.getUrl(), Login.USER,
					Login.PASSWORD, test.reportId);
			classification = this.heuristicClassifier.classify(report)
					.getClassification();
			assertTrue(test.reportId + " - Expected: " + test.classification + " Actual: "
					+ classification, classification
					.equals(test.classification));
		}
	}
}
