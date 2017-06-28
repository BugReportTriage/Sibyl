package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;

public class FirefoxClassifier {

	public static void create() {

		ClassifierType classifierType = ClassifierType.SVM;
		Heuristic heuristic = Heuristic.MOZILLA;

		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		Profiles profile = Classifier.createDeveloperProfiles(dataset);
		TriageClassifier classifier = Classifier.create(classifierType, dataset.getTrainingReports(),
				dataset.getTestingReports(), FirefoxData.DEVELOPER_INFO, heuristic, profile);
		Classifier.saveClassifier(dataset.getProject(), classifier);
	}
}
