package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;

// @tag recommender.eclipse.assignment -author=John -date="enCA:15/10/07"
public class EclipseClassifier {

	public static void create() {

		ClassifierType classifierType = ClassifierType.SVM;
		Heuristic heuristic = Heuristic.ECLIPSE;
		
		Dataset dataset = new BugzillaDataset(Project.PLATFORM);		
		Profiles profile = Classifier.createDeveloperProfiles(dataset);

		TriageClassifier classifier = Classifier.create(				
				classifierType, dataset.getTrainingReports(), dataset.getTestingReports(),
				EclipseData.DEVELOPER_INFO_PACKAGE, heuristic, profile);
		heuristic.getClassifier().writeDupsToDownload(EclipseData.DUPLICATES);
		Classifier.saveClassifier(Project.PLATFORM, classifier);
	}
}
