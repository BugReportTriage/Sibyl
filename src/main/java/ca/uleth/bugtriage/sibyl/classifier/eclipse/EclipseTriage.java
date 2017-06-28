package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import java.io.File;
import java.io.FileNotFoundException;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.eval.Triage;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class EclipseTriage {
	
	public static void run(TriageClassifier classifier) {
	    Dataset eclipse = new BugzillaDataset(Project.PLATFORM);
		new Triage().evaluate(classifier, eclipse.getTestingReports(), EclipseData.DEVELOPER_INFO_PACKAGE);
	}
	
	public static void main(String[] args) {
		try {
			String classifierDir = Environment.getClassifierDir();
			String classifierName = classifierDir + "eclipse8month.classifier";
			TriageClassifier clasifier = MLClassifier.load(new File(classifierName));
			run(clasifier);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
