package ca.uleth.bugtriage.sibyl.dataset;

import java.util.Set;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class KDEPlasmaDataset {

    public static void main(String[] args) {

	System.out.println("Getting reports from " + Project.KDE_PLASMA.name);
	BugzillaDataset dataset = new BugzillaDataset(Project.KDE_PLASMA);
	Set<BugReport> reports = dataset.getData();
	System.out.println("Writing reports to " + Project.KDE_PLASMA.dataDir);
	dataset.exportReports();
    }
}
