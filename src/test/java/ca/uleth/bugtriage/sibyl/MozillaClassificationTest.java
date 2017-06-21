package ca.uleth.bugtriage.sibyl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxData;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Profiles;

public class MozillaClassificationTest {

	@Test
	public void testFixedHeuristic() {
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		File testData = new File(dataset.getProject().dataDir + "/Firefox_2bugs.json");
		List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports(testData));
		assertEquals(2, reports.size());

		HeuristicClassifier hClassifier = Project.FIREFOX.heuristic.getClassifier();

		BugReport report = reports.get(0);
		assertEquals(569459, report.getId());

		Classification result = hClassifier.classify(report);
		assertEquals("gavin.sharp@gmail.com", result.getClassification());

		report = reports.get(1);
		assertEquals(569360, report.getId());

		result = hClassifier.classify(report);
		assertEquals("mounir@lamouri.fr", result.getClassification());

	}

	@Test
	public void testBuildClassisifer() {
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		File testData = new File(dataset.getProject().dataDir + "/Firefox_2017-05-01_2017-06-01.json");
		List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports(testData));
		assertEquals(386, reports.size());

		ClassifierType classifierType = ClassifierType.SVM;

		Heuristic heuristic = Heuristic.MOZILLA;

		// Use a 90/10 split for training/testing
		assertEquals(reports.size(), dataset.getTrainingReports().size() + dataset.getTestingReports().size());

		dataset.getProject().threshold = 3;
		Profiles profiles = Classifier.createDeveloperProfiles(dataset);

		TriageClassifier classifier = Classifier.create(classifierType, dataset.getTrainingReports(),
				dataset.getTestingReports(), null, heuristic, profiles);
		File classifierFile = Classifier.saveClassifier(dataset.getProject(), classifier);
		Assert.assertTrue(classifierFile.exists());
	}

	@Test
	public void testUseClassifier() throws FileNotFoundException {
		
		/*// Loading not working
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		File classifierFile = new File(Environment.getClassifierDir() + dataset.getProject().name + ".arff");
		TriageClassifier classifier = MLClassifier.load(classifierFile);
		*/
		
		
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		File testData = new File(dataset.getProject().dataDir + "/Firefox_2017-05-01_2017-06-01.json");
		List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports(testData));
		assertEquals(386, reports.size());
		
		ClassifierType classifierType = ClassifierType.SVM;
		Heuristic heuristic = Heuristic.MOZILLA;
		// Use a 90/10 split for training/testing
		assertEquals(reports.size(), dataset.getTrainingReports().size() + dataset.getTestingReports().size());
		dataset.getProject().threshold = 3;
		Profiles profiles = Classifier.createDeveloperProfiles(dataset);
		
		TriageClassifier classifier = Classifier.create(classifierType, dataset.getTrainingReports(),
				dataset.getTestingReports(), null, heuristic, profiles);
		
		for (BugReport testReport : dataset.getTestingReports()) {
			List<Classification> predictions = classifier.classify(testReport);
			Assert.assertEquals(20, predictions.size());
			System.err.println(predictions);
		}
	}
}
