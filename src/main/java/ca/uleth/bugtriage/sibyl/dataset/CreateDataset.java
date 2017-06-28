package ca.uleth.bugtriage.sibyl.dataset;

import java.io.File;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;

public class CreateDataset {

	public static void combineFiles(Project p, File dir){
		if(dir.exists() && dir.isDirectory()){
			String[] files = dir.list();
			Dataset dataset = new BugzillaDataset(p);
			for(String file : files){
				File dataFile = new File(dir.getAbsolutePath() + "/" + file);
				if(dataFile.exists()){
					dataset.importReports(dataFile);
					System.out.println("Total Imported: " + dataset.reports.size() + " reports.");					
				}else{
					System.err.println(dir.getAbsolutePath() + "/" + file + " not found.");
				}
			}
			dataset.exportReports();
		}
	}
	
	public static void main(String[] args) {
		combineFiles(Project.FIREFOX, new File(Project.FIREFOX.dataDir+"/partials"));
	}
}