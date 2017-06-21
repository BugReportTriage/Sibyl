package ca.uleth.bugtriage.sibyl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxData;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class MozillaClassificationTest {

	private static List<BugReport> reports;

	@Test
	public void testFixedHeuristic() {
		File testFile = new File(Project.FIREFOX.dataDir + "/" + Project.FIREFOX.name + "_2010-06-01.json");
		reports = BugzillaDataset.importReports(testFile);
		assertEquals(2, reports.size());

		HeuristicClassifier hClassifier = Project.FIREFOX.heuristic.getClassifier();

		BugReport report = reports.get(0);
		assertEquals(569360, report.getId());

		Classification result = hClassifier.classify(report);
		assertEquals("mounir@lamouri.fr", result.getClassification());

		report = reports.get(1);
		assertEquals(569459, report.getId());

		result = hClassifier.classify(report);
		assertEquals("gavin.sharp@gmail.com", result.getClassification());
	}

	@Test
	public void testBuildClassisifer(){		
		File testFile = new File(Project.FIREFOX.dataDir + "/" + Project.FIREFOX.name + "_2016-01-01-2weeks.json");
		reports = BugzillaDataset.importReports(testFile);
		assertEquals(142, reports.size());

		ClassifierType classifierType = ClassifierType.SVM;
		//ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

		Heuristic heuristic = Heuristic.MOZILLA;
		
		// Use a 90/10 split for training/testing
		List<BugReport> training = reports.subList(0, 128);
		List<BugReport> testing = reports.subList(128, reports.size());
		assertEquals(reports.size(), training.size() + testing.size());
		
		Profiles profile = Classifier.createDeveloperProfiles(testing, Project.FIREFOX);	
	}
}
