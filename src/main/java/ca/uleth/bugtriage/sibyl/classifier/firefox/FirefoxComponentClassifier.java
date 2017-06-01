package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxComponentClassifier {

	public static void create(int numMonths) {

		Heuristic heuristic = Heuristic.COMPONENT;
		String[] trainingSet = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR, numMonths, FirefoxData.LAST_TRAINING_MONTH);
		String[] testingSet = Utils.getTestingSet(FirefoxData.FIREFOX_DIR, FirefoxData.TESTING_MONTH);
		
		TriageClassifier classifier = Classifier.create(ClassifierType.SVM, trainingSet,
				testingSet, FirefoxData.DEVELOPER_INFO, heuristic,
				null, false);
		Classifier.saveClassifier("firefoxComponent" + numMonths + "month", classifier);
	}
}
