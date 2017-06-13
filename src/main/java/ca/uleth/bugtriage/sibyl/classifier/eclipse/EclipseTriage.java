package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import java.io.File;
import java.io.FileNotFoundException;

import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.eval.Triage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseTriage {
	
	public static void run(TriageClassifier classifier) {
		new Triage().evaluate(classifier, Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH), EclipseData.DEVELOPER_INFO_PACKAGE);
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
