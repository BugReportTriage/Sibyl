package ca.uleth.bugtriage.sibyl.classifier.firefox;

import java.io.File;
import java.io.FileNotFoundException;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.eval.Triage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxTriage {
	
	public static void run(TriageClassifier classifier) {
	    Dataset firefox = new BugzillaDataset(Project.FIREFOX);
		new Triage().evaluate(classifier, firefox.getTestingReports(), FirefoxData.DEVELOPER_INFO);
	}
	
	public static void main(String[] args) {
		try {
			String classifierDir = Environment.getClassifierDir();
			String classifierName = classifierDir + /*"firefox_cb.classifier";*/ "firefox8month.classifier";
			TriageClassifier clasifier = MLClassifier.load(new File(classifierName));
			run(clasifier);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
