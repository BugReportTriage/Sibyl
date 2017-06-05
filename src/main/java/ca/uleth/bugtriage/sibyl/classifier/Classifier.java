package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.Frequency;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.SubcomponentHeuristic;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import ca.uleth.bugtriage.sibyl.utils.Profiles;

public abstract class Classifier {

	public abstract Classification classify(BugReport report);

	public void printStats() {
		throw new UnsupportedOperationException();
	}

	public void resetStats() {
		throw new UnsupportedOperationException();
	}

	public static void saveClassifier(String classifierName,
			TriageClassifier classifier) {
		File classifierFile = new File(Environment.getClassifierDir()
				+ classifierName + ".classifier");
		classifier.save(classifierFile);
	}

	public static final double SUBCOMPONENT_THRESHOLD = 5;

	public static final int NUM_PROFILE_MONTHS = 3;

	public static Profiles createDeveloperProfiles(String[] dataset,
			Project project) {
		Profiles profile = new Profiles(dataset, project.getHeuristic());
		System.out.println("Creating profiles...");
		profile.createProfiles(NUM_PROFILE_MONTHS);
		
		Map<String, FrequencyTable> profiles = profile.getProfiles();
		
		Frequency freqs = new Frequency(Collections.reverseOrder());
		FrequencyTable table = new FrequencyTable();
		for (String string : profiles.keySet()) {
			FrequencyTable devFreq = profiles.get(string);
			//System.out.println(devFreq.getStatistics().getSum());
			
			freqs.addValue(devFreq.getStatistics().getSum());
			for(int i=0; i<devFreq.getStatistics().getSum(); i++)
				table.add(string);
		}
		
		//System.out.println(freqs);
		//System.out.println(table);
		System.out.println("Developers: " + table.getKeys().size());
		
		
		System.err.println("Before Pruning: " + profile.size());
		profile.pruneAverage(project.getThreshold()
				/ Classifier.NUM_PROFILE_MONTHS);
		System.err.println("After Pruning: " + profile.size());
		return profile;
	}

	public static Profiles createSubcomponentProfiles(
			Set<BugReport> trainingReports, Heuristic heuristic) {
		Profiles subcomponentProfile = new Profiles(null, heuristic);
		System.out.println("Creating profiles...");
		subcomponentProfile.createProfile(
				SubcomponentHeuristic.SUBCOMPONENT_PRESENT, trainingReports);
		System.err.println("Before Pruning: " + subcomponentProfile.size());
		subcomponentProfile
				.pruneTotal(ca.uleth.bugtriage.sibyl.classifier.Classifier.SUBCOMPONENT_THRESHOLD);
		System.err.println("After Pruning: " + subcomponentProfile.size());
		return subcomponentProfile;
	}

	private static TriageClassifier createClassifier(String[] trainingSets,
			String[] testingSet, ClassifierType type, String developerInfo,
			Heuristic heuristic, Profiles profile) {
		TriageClassifier classifier = type.getClassifier();
		classifier.setProfile(profile);

		try {
			classifier
					.train(trainingSets, testingSet, developerInfo, heuristic);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return classifier;
	}

	/*
	 * Assumes that training sets are in reverse chronological order
	 */
	public static TriageClassifier create(ClassifierType type,
			String[] trainingSets, String[] testingSet, String developerInfo,
			Heuristic heuristic, Profiles profile, boolean useDupResolver) {
		if (heuristic != null)
			heuristic.getClassifier().useDuplicateResolver(useDupResolver);
		TriageClassifier classifier = createClassifier(trainingSets,
				testingSet, type, developerInfo, heuristic, profile);
		// if (classifier instanceof MLClassifier) {
		// MLClassifier ml_classifier = (MLClassifier) classifier;
		// ml_classifier.crossValidate(10);
		// }
		return classifier;
	}

}
