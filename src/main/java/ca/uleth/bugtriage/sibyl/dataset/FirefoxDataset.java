package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;

public class FirefoxDataset {

	public static void main(String[] args) {		
		String data = BugzillaDataset.getReports(Project.FIREFOX);
		System.out.println(data);		
	}
}
