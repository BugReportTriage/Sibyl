package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;

public class FirefoxDataset {

	public static void main(String[] args) {
		
		String url = BugzillaDataset.constructUrl(Project.FIREFOX);
		//System.out.println(url);
		String data = BugzillaDataset.getData(url);
		System.out.println(data);		
	}
}
