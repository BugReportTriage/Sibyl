package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import java.util.Map;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

// @tag recommender.eclipse.assignment -author=John -date="enCA:15/10/07"
public class EclipseClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;

		Heuristic heuristic = Heuristic.ECLIPSE;
		String dupSet = EclipseData.DUPLICATES;
		heuristic.getClassifier().setDataset(dupSet);

		String[] trainingSet = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR,
				numMonths, EclipseData.LAST_TRAINING_MONTH);
		String[] profileSet = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR,
				Classifier.NUM_PROFILE_MONTHS, EclipseData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(profileSet,
				Project.PLATFORM);

		TriageClassifier classifier = Classifier.create(
				// ClassifierType.COMPONENT_BASED,
				classifierType, trainingSet, Utils.getTestingSet(
						EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH),
				EclipseData.DEVELOPER_INFO_PACKAGE, heuristic, profile, true);
		heuristic.getClassifier().writeDupsToDownload(EclipseData.DUPLICATES);
		Classifier.saveClassifier("eclipse" + numMonths + "month", classifier);
	}
}
