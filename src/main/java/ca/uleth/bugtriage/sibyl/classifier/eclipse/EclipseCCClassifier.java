package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseCCClassifier {

	public static void main(String[] args) {

		String[] trainingSet = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR, 3, EclipseData.LAST_TRAINING_MONTH);
		String[] testingSet = Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH);
		
		TriageClassifier classifier = Classifier.create(ClassifierType.CC, trainingSet,
				testingSet, EclipseData.DEVELOPER_INFO_PACKAGE, null,
				null, true);
		Classifier.saveClassifier("eclipseCC", classifier);
		
	}
}
