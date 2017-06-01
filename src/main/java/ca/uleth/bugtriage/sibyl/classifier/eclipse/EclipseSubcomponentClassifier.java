package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import java.io.File;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseSubcomponentClassifier {

	public static void create(int numMonths) {

		Heuristic heuristic = Heuristic.SUBCOMPONENT;
		String[] trainingSet = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR, numMonths, EclipseData.LAST_TRAINING_MONTH);
		String[] testingSet = Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH);
		
		TriageClassifier classifier = Classifier.create(ClassifierType.COMPONENT_BASED, trainingSet,
				testingSet, EclipseData.DEVELOPER_INFO_PACKAGE, heuristic,
				null, true);
		Classifier.saveClassifier("eclipseSubcomponent" + numMonths + "month", classifier);
		
	}
}
