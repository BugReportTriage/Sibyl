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

    public static void saveClassifier(Project project, TriageClassifier classifier) {
	File classifierFile = new File(Environment.getClassifierDir() + project.name + ".classifier");
	classifier.save(classifierFile);
    }

    public static final double SUBCOMPONENT_THRESHOLD = 5;

    public static final int NUM_PROFILE_MONTHS = 3;

    public static Profiles createDeveloperProfiles(Dataset dataset) {
	Profiles profiles = new Profiles(dataset.getTestingReports(), dataset.getProject().heuristic);
	System.out.println("Creating profiles...");
	profiles.create();

	System.out.println("Developers: " + profiles.size());

	System.err.println("Before Pruning: " + profiles.size());
	profiles.pruneTotal(dataset.getProject().threshold);
	System.err.println("After Pruning: " + profiles.size());
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
