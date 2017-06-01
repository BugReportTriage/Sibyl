package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxCCClassifier {

	public static void main(String[] args) {

		String[] trainingSet = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR, 3, FirefoxData.LAST_TRAINING_MONTH);
		String[] testingSet = Utils.getTestingSet(FirefoxData.FIREFOX_DIR, FirefoxData.TESTING_MONTH);
		
		TriageClassifier classifier = Classifier.create(ClassifierType.CC, trainingSet,
				testingSet, FirefoxData.DEVELOPER_INFO, null,
				null, true);
		Classifier.saveClassifier("firefoxCC", classifier);
		
	}
}
