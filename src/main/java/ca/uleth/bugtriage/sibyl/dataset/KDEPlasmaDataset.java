package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;

public class KDEPlasmaDataset {

	public static void main(String[] args) {
		
		String url = BugzillaDataset.getReports(Project.KDE_PLASMA);
		System.out.println(url);
		
	}
}
