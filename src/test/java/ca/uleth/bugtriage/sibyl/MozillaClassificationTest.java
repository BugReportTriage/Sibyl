package ca.uleth.bugtriage.sibyl;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxData;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class MozillaClassificationTest {

    private static List<BugReport> reports;

    @Test
    public void testFixedHeuristic() {
	Dataset dataset = new BugzillaDataset(Project.FIREFOX);
	File testData = new File(dataset.getProject().dataDir + "/Firefox_2bugs.json");
	reports = new ArrayList<BugReport>(dataset.importReports(testData));
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

    @Ignore
    @Test
    public void testBuildClassisifer() {
	Dataset dataset = new BugzillaDataset(Project.FIREFOX);
	reports = new ArrayList<BugReport>(dataset.importReports());
	assertEquals(142, reports.size());

	ClassifierType classifierType = ClassifierType.SVM;
	// ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

	Heuristic heuristic = Heuristic.MOZILLA;

	// Use a 90/10 split for training/testing
	assertEquals(reports.size(), dataset.getTrainingReports().size() + dataset.getTestingReports().size());

	Profiles profiles = Classifier.createDeveloperProfiles(dataset);	
    }
}
