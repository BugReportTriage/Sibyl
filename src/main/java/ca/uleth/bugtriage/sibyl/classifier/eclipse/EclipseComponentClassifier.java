package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Utils;

// @tag recommender.eclipse.component -author=John -date="enCA:15/10/07"
public class EclipseComponentClassifier {

	public static void create(int numMonths) {

		Heuristic heuristic = Heuristic.COMPONENT;
		String[] trainingSet = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR, numMonths, EclipseData.LAST_TRAINING_MONTH);
		String[] testingSet = Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH);
		
		TriageClassifier classifier = Classifier.create(ClassifierType.SVM, trainingSet,
				testingSet, EclipseData.DEVELOPER_INFO_PACKAGE, heuristic,
				null, true);
		Classifier.saveClassifier("eclipseComponent" + numMonths + "month", classifier);
		
	}
}
