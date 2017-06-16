package ca.uleth.bugtriage.sibyl.dataset;

import java.util.List;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class FirefoxDataset {

	public static void main(String[] args) {
		System.out.println("Getting reports from " + Project.FIREFOX.name);
		List<BugReport> reports = BugzillaDataset.getData(Project.FIREFOX);
		System.out.println("Writing reports to " + Project.FIREFOX.dataDir);
		BugzillaDataset.exportReports(Project.FIREFOX, reports);				
	}
}
