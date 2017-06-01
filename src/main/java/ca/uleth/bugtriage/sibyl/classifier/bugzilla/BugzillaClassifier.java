package ca.uleth.bugtriage.sibyl.classifier.bugzilla;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class BugzillaClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;
		//ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

		Heuristic heuristic = Heuristic.MOZILLA;
		String[] trainingSet = Utils.getTrainingSet(BugzillaData.BUGZILLA_DIR,
				numMonths, BugzillaData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(trainingSet, Project.BUGZILLA);

		 String dupSet = BugzillaData.DUPLICATES;
		heuristic.getClassifier().setDataset(dupSet);
		TriageClassifier classifier = Classifier.create(
				classifierType, trainingSet, Utils.getTestingSet(
						BugzillaData.BUGZILLA_DIR, BugzillaData.TESTING_MONTH),
				BugzillaData.DEVELOPER_INFO, heuristic, profile, true);
		heuristic.getClassifier().writeDupsToDownload(dupSet);
		Classifier.saveClassifier("bugzilla" + numMonths + "month", classifier);
	}
}
