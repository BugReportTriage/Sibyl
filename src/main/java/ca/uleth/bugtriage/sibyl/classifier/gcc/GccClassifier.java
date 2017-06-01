package ca.uleth.bugtriage.sibyl.classifier.gcc;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class GccClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;
		//ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

		Heuristic heuristic = Heuristic.GCC;
		String dupSet = GccData.DUPLICATES;
		heuristic.getClassifier().setDataset(dupSet);
		
		String[] trainingSet = Utils.getTrainingSet(GccData.GCC_DIR,
				numMonths, GccData.LAST_TRAINING_MONTH);
		String[] profileSet = Utils.getTrainingSet(GccData.GCC_DIR,
				Classifier.NUM_PROFILE_MONTHS, GccData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(profileSet, Project.GCC);

		 
		TriageClassifier classifier = Classifier.create(
				// ClassifierType.COMPONENT_BASED,
				classifierType, trainingSet, Utils.getTestingSet(
						GccData.GCC_DIR, GccData.TESTING_MONTH),
				GccData.DEVELOPER_INFO, heuristic, profile, true);
		heuristic.getClassifier().writeDupsToDownload(dupSet);
		Classifier.saveClassifier("gcc" + numMonths + "month", classifier);
	}
}
