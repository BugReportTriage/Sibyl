package ca.uleth.bugtriage.sibyl;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ca.uleth.bugtriage.sibyl.bugreport.BugReportJsonTest;
import ca.uleth.bugtriage.sibyl.bugreport.BugzillaDatasetTest;
import ca.uleth.bugtriage.sibyl.classifier.MozillaClassificationTest;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

@RunWith(Suite.class)
@SuiteClasses({ BugReportJsonTest.class, BugzillaDatasetTest.class, MozillaClassificationTest.class })
public class AllTests {

	public static Dataset getFirefox_1Month() {
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		File testData = new File(dataset.getProject().dataDir + "/testing/Firefox_2017-05-01_2017-06-01.json");
		List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports(testData));
		assertEquals(386, reports.size());

		return dataset;
	}

	public static Dataset getFirefox_2Bugs() {
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		File testData = new File(dataset.getProject().dataDir + "/testing/Firefox_2bugs.json");
		List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports(testData));
		assertEquals(2, reports.size());
		
		return dataset;
	}
}
