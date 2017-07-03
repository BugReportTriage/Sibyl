package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Profiles;

public abstract class Classifier {

	public abstract Classification classify(BugReport report);

	public void printStats() {
		throw new UnsupportedOperationException();
	}

	public void resetStats() {
		throw new UnsupportedOperationException();
	}

	public static File saveClassifier(Project project, TriageClassifier classifier) {		
		return classifier.save(project);		
	}

	public static final double SUBCOMPONENT_THRESHOLD = 5;

	public static Profiles createDeveloperProfiles(Dataset dataset) {
		Profiles profiles = new Profiles(dataset.getTrainingReports(), dataset.getProject().heuristic);
		System.out.println("Creating profiles...");
		profiles.create();

		System.out.println("Developers: " + profiles.size());
		System.err.println(profiles);

		System.err.println("Before Pruning (threshold): " + profiles.size());
		profiles.pruneTotal(dataset.getProject().thresholdLow, dataset.getProject().thresholdHigh);
		System.err.println("After Pruning (threshold): " + profiles.size());
		
		return profiles;
	}

	public static TriageClassifier create(ClassifierType type, Set<BugReport> trainingReports,
			Set<BugReport> testingReports, String developerInfo, Heuristic heuristic, Profiles profile) {
		TriageClassifier classifier = type.getClassifier();
		classifier.setProfile(profile);

		try {
			classifier.train(trainingReports, testingReports, developerInfo, heuristic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classifier;
	}
}
