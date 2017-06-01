package ca.uleth.bugtriage.sibyl.eval;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class Triage {

	private static final int START_X = 1;

	public static final int TOP_X = 3;

	/*
	 * Statistics
	 */
	// private Profiles threeMonth, fourMonth, sixMonth, twelveMonth;
	public Triage() {
		/*
		 * this.threeMonth = null; this.fourMonth = null; this.sixMonth = null;
		 * this.twelveMonth = null;
		 */
	}

	/*
	 * public void setProfiles(Profiles three, Profiles four, Profiles six,
	 * Profiles twelve) { this.threeMonth = three; this.fourMonth = four;
	 * this.sixMonth = six; this.twelveMonth = twelve; }
	 */
	public void evaluate(String classifierFilename, String[] testingFiles,
			String developerInfoFilename) {

		classifierFilename = Environment.getClassifierDir() + classifierFilename;
		File classifierFile = new File(classifierFilename);
		TriageClassifier classifier= null;
		try {
			classifier = MLClassifier.load(classifierFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//NearestNeighbour.load(classifierFile);

		System.out.println("# classes: " + classifier.numClasses());

		evaluate(classifier, testingFiles, developerInfoFilename);
	}

public void evaluate(TriageClassifier classifier, String[] testingFiles,
			String developerInfoFilename) {

		DeveloperInfo developerInfo = new DeveloperInfo(
				developerInfoFilename);
		Map<Integer, Set<String>> developerInfoMap = developerInfo.getDeveloperInfo();

		Set<String> cantconvert = developerInfo.getCantconvert();
		System.out.println("cant convert (" + cantconvert.size() + "): "
				+ cantconvert);

		System.out.println("Testing: " + classifier.getName());

		Set<BugReport> testReports = developerInfo.getTestingSet(testingFiles);//Utils.getReports(testingFilename);

		int numClasses = classifier.getClasses().size();
		for (int topX = START_X; topX <= TOP_X && topX <= numClasses; topX++) {

			TriageEvaluation eval = new TriageEvaluation(classifier,
					developerInfoMap, testReports, topX);
			eval.run();
			System.out.println("[" + eval.notMapped.size() + "]: " + eval.notMapped);
		}
	}}
