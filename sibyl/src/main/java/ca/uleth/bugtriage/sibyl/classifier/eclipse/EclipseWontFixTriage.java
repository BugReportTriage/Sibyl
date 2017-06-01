package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import java.io.File;
import java.io.FileNotFoundException;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.eval.ComponentTriage;
import ca.uleth.bugtriage.sibyl.eval.Triage;
import ca.uleth.bugtriage.sibyl.eval.WontFixTriage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseWontFixTriage {
	
	private static final String CLASSIFIER = Environment.getClassifierDir() + 
	 "eclipseWontFix8month.classifier";
	private static final String[] TEST_DATA = Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH);
	
	
	public static void main(String[] args) {
		WontFixTriage.run(CLASSIFIER, TEST_DATA);
	}
}
