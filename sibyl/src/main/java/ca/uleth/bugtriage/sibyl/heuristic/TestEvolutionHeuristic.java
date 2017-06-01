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

public class TestEvolutionHeuristic {

	private HeuristicClassifier heuristicClassifier;

	@Before
	public void setUp() throws Exception {
		this.heuristicClassifier = Heuristic.EVOLUTION.getClassifier();
	}

	@Test
	public void testFixed() {

		List<ClassificationTestReport> tests = new ArrayList<ClassificationTestReport>();
		tests.add(new ClassificationTestReport(312348, "kharish@novell.com"));
		tests.add(new ClassificationTestReport(330157, "sragavan@novell.com"));
		tests.add(new ClassificationTestReport(333864, "sragavan@novell.com"));
		tests.add(new ClassificationTestReport(334966, "sragavan@novell.com"));
		tests.add(new ClassificationTestReport(351374, "sragavan@novell.com"));
		tests.add(new ClassificationTestReport(354875, "ben@mw0.ath.cx"));

		runTests(tests);
	}

	@Test
	public void testDiscussed() {
		/*
		 * These are bugs that can't be classified since they are resolved after
		 * a lot of discussion. This covers resolutions: WONTFIX, NOTABUG,
		 * NOTGNOME
		 */

		List<ClassificationTestReport> tests = new ArrayList<ClassificationTestReport>();
		// WONTFIX
		// tests.add(new ClassificationTestReport(205927,
		// HeuristicClassifier.CANNOT_CLASSIFY));
		// tests.add(new ClassificationTestReport(354921,
		// HeuristicClassifier.CANNOT_CLASSIFY));
		// tests.add(new ClassificationTestReport(356709,
		// HeuristicClassifier.CANNOT_CLASSIFY));

		// NOTABUG
		tests.add(new ClassificationTestReport(363641,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(363919,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(364121,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(366621,
				HeuristicClassifier.CANNOT_CLASSIFY));

		// NOTGNOME
		tests.add(new ClassificationTestReport(360747,
				HeuristicClassifier.CANNOT_CLASSIFY));

		runTests(tests);
	}

	@Test
	public void testTriagerIntercepted() {

		/*
		 * These are bugs that can't be classified since the triager intercepts
		 * them and they (generally) never reach the developer This covers
		 * resolutions: INCOMPLETE, INVALID, OBSOLETE
		 */
		List<ClassificationTestReport> tests = new ArrayList<ClassificationTestReport>();

		// INCOMPLETE
		tests.add(new ClassificationTestReport(366641,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(366689,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(366770,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(366913,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(366949,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(366728,
				HeuristicClassifier.CANNOT_CLASSIFY));
		// CLOSED bug
		tests.add(new ClassificationTestReport(45503,
				HeuristicClassifier.CANNOT_CLASSIFY));

		// INVALID
		tests.add(new ClassificationTestReport(369247,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(371770,
				HeuristicClassifier.CANNOT_CLASSIFY));

		// OBSOLETE
		tests.add(new ClassificationTestReport(369247,
				HeuristicClassifier.CANNOT_CLASSIFY));
		tests.add(new ClassificationTestReport(371770,
				HeuristicClassifier.CANNOT_CLASSIFY));

		runTests(tests);
	}

	private void runTests(List<ClassificationTestReport> tests) {
		BugReport report;
		String classification;
		for (ClassificationTestReport test : tests) {
			if(test.reportId == 45503){
				System.out.println("here");
			}
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
