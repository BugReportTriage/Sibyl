package ca.uleth.bugtriage.sibyl.eval;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class CCTriage {

	private static final int MAX_RECOMMENDATIONS = 7;

	public static void run(String classifierFile, String[] testSet) {
		TriageClassifier classifier = null;
		try {
			classifier = MLClassifier.load(new File(classifierFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Getting test reports [" + testSet.length + "]");
		Set<BugReport> testReports = null;//Utils.getReports(testSet);

		double precision, recall, adjustedRecall, correct;
		DescriptiveStatistics precisionStats, recallStats, adjustedRecallStats;
		List<DescriptiveStatistics> precisions = new ArrayList<DescriptiveStatistics>();
		List<DescriptiveStatistics> recalls = new ArrayList<DescriptiveStatistics>();
		List<DescriptiveStatistics> adjustedRecalls = new ArrayList<DescriptiveStatistics>();

		for (int numRecommendations = 1; numRecommendations <= MAX_RECOMMENDATIONS; numRecommendations++) {
			precisions.add(new DescriptiveStatistics());
			recalls.add(new DescriptiveStatistics());
			adjustedRecalls.add(new DescriptiveStatistics());
		}

		System.out.println("Test set before CC check: " + testReports.size());
		// Remove any reports that don't have CCs
		Set<BugReport> ccReports = new HashSet<BugReport>();
		for (BugReport report : testReports) {
			if (report.getCCList().isEmpty())
				continue;
			ccReports.add(report);
		}
		System.out.println("Test set after CC check: " + ccReports.size());

		List<String> classes = classifier.getClasses();
		System.out.println("Num Classes: " + classes.size());

		for (BugReport report : ccReports) {
			precision = 0;
			recall = 0;
			List<String> actual = report.getCCList();
			// System.out.println(report.getId() + ": " + actual);
			List<Classification> predictions = classifier.classify(report);
			int unknown = 0;
			for (String cc : actual) {
				if (!classes.contains(cc))
					unknown++;
			}

			for (int numRecommendations = 1; numRecommendations <= MAX_RECOMMENDATIONS; numRecommendations++) {
				precisionStats = precisions.get(numRecommendations - 1);
				recallStats = recalls.get(numRecommendations - 1);
				adjustedRecallStats = adjustedRecalls
						.get(numRecommendations - 1);
				correct = 0;
				for (int index = 0; index < numRecommendations; index++) {
					String predicted = predictions.get(index)
							.getClassification();
					// System.out.println("\t" + predicted);
					for (String cc : actual) {
						if (predicted.equals(cc)) {
							correct++;
						}
					}
				}

				precisionStats.addValue(correct / numRecommendations);
				recallStats.addValue(correct / actual.size());
				int adjustedGroupSize = actual.size() - unknown;
				if (adjustedGroupSize > 0) {
					//System.out.println("Adjusted Size: " + adjustedGroupSize);
					adjustedRecall = correct / adjustedGroupSize;
					//System.out.println("Adjusted Recall: " + adjustedRecall);
					adjustedRecallStats.addValue(adjustedRecall);
				}
			}
		}

		for (int numRecommendations = 0; numRecommendations < MAX_RECOMMENDATIONS; numRecommendations++) {
			precision = precisions.get(numRecommendations).getMean();
			recall = recalls.get(numRecommendations).getMean();
			adjustedRecall = adjustedRecalls.get(numRecommendations).getMean();
			System.out.println((numRecommendations + 1) + ": " + precision
					+ " [" + recall + "] {" + adjustedRecall + " - " + adjustedRecalls.get(numRecommendations).getN() + "}");
		}
	}
}
