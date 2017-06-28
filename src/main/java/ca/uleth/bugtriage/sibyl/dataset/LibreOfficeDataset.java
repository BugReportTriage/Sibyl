package ca.uleth.bugtriage.sibyl.dataset;

import java.util.Set;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class LibreOfficeDataset {

    public static void main(String[] args) {

	BugzillaDataset dataset = new BugzillaDataset(Project.LIBREOFFICE);
	Set<BugReport> reports = dataset.getData();

    }
}
