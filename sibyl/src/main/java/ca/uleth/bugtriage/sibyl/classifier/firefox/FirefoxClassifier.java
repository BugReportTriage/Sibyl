package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;
		//ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

		Heuristic heuristic = Heuristic.MOZILLA;
		String dupSet = FirefoxData.DUPLICATES;
		heuristic.getClassifier().setDataset(dupSet);
		
		String[] trainingSet = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
				numMonths, FirefoxData.LAST_TRAINING_MONTH);
		String[] profileSet  = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
				Classifier.NUM_PROFILE_MONTHS, FirefoxData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(profileSet, Project.FIREFOX);

		/* 
		TriageClassifier classifier = Classifier.create(
				// ClassifierType.COMPONENT_BASED,
				classifierType, trainingSet, Utils.getTestingSet(
						FirefoxData.FIREFOX_DIR, FirefoxData.TESTING_MONTH),
				FirefoxData.DEVELOPER_INFO, heuristic, profile, true);
				*/
		heuristic.getClassifier().writeDupsToDownload(dupSet);
		//Classifier.saveClassifier("firefox" + numMonths + "month", classifier);
	}
}
