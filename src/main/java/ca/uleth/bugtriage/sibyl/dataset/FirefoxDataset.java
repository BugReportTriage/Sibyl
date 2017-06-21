package ca.uleth.bugtriage.sibyl.dataset;

import java.util.Set;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class FirefoxDataset {

    public static void main(String[] args) {
	System.out.println("Getting reports from " + Project.FIREFOX.name);
	BugzillaDataset dataset = new BugzillaDataset(Project.FIREFOX);
	Set<BugReport> reports = dataset.getData();
	System.out.println("Writing reports to " + Project.FIREFOX.dataDir);
	dataset.exportReports();
    }
}
