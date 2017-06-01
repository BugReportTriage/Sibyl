package ca.uleth.bugtriage.sibyl.classifier.mylar;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class MylarClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;

		Heuristic heuristic = Heuristic.MYLAR;
		String dupSet = MylarData.DUPLICATES;
		heuristic.getClassifier().setDataset(dupSet);

		String[] trainingSet = Utils.getTrainingSet(MylarData.MYLAR_DIR,
				numMonths, MylarData.LAST_TRAINING_MONTH);
		String[] profileSet = Utils.getTrainingSet(MylarData.MYLAR_DIR,
				Classifier.NUM_PROFILE_MONTHS, MylarData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(profileSet,
				Project.MYLAR);

		TriageClassifier classifier = Classifier.create(
				// ClassifierType.COMPONENT_BASED,
				classifierType, trainingSet, Utils.getTestingSet(
						MylarData.MYLAR_DIR, MylarData.TESTING_MONTH),
				MylarData.DEVELOPER_INFO, heuristic, profile, true);
		heuristic.getClassifier().writeDupsToDownload(MylarData.DUPLICATES);
		Classifier.saveClassifier("mylar" + numMonths + "month", classifier);
	}
}
