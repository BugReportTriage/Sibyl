package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;

public class LibreOfficeDataset {

	public static void main(String[] args) {
		
		String url = BugzillaDataset.constructUrl(Project.LIBREOFFICE);
		System.out.println(url);
		
	}
}
