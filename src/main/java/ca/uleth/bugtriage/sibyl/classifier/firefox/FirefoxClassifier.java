package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;
		//ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

		Heuristic heuristic = Heuristic.MOZILLA;
		
		String[] trainingSet = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
				numMonths, FirefoxData.LAST_TRAINING_MONTH);
		String[] profileSet  = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
				Classifier.NUM_PROFILE_MONTHS, FirefoxData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(profileSet, Project.FIREFOX);		
	}
}
