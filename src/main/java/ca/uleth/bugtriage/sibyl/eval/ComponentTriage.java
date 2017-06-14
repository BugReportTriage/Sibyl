package ca.uleth.bugtriage.sibyl.eval;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class ComponentTriage {

	private static final int MAX_RECOMMENDATIONS = 5;

	public static void run(String classifierFile, String[] testSet) {
		System.out.println("--- Component Triage ---");
		TriageClassifier classifier = null;
		try {
			classifier = MLClassifier
					.load(new File(classifierFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("Getting test set");
		Set<BugReport> testReports = null;//Utils.getReports(testSet);

		double precision, recall;
		DescriptiveStatistics precisionStats, recallStats; 
		List<DescriptiveStatistics> precisions = new ArrayList<DescriptiveStatistics>();
		List<DescriptiveStatistics> recalls = new ArrayList<DescriptiveStatistics>();
		
		
		for (int numRecommendations = 1; numRecommendations <= MAX_RECOMMENDATIONS; numRecommendations++) {
			precisions.add(new DescriptiveStatistics());
			recalls.add(new DescriptiveStatistics());
		}
		
		System.out.println("Running Tests...");
		for (BugReport report : testReports) {
			precision = 0;
			recall = 0;
			String actual = report.getComponent();
			// System.out.println(report.getId() + ": " + actual);
			List<Classification> predictions = classifier.classify(report);
			for (int numRecommendations = 1; numRecommendations <= MAX_RECOMMENDATIONS; numRecommendations++) {
				precisionStats = precisions.get(numRecommendations-1);
				recallStats = recalls.get(numRecommendations-1);
				for (int index = 0; index < numRecommendations && index < predictions.size(); index++) {
					String predicted = predictions.get(index)
							.getClassification();
					// System.out.println("\t" + predicted);
					if (predicted.equals(actual)) {
						precision = 1.0 / numRecommendations;
						recall = 1;
					}
				}
				precisionStats.addValue(precision);
				recallStats.addValue(recall);
			}
		}

		for (int numRecommendations = 0; numRecommendations < MAX_RECOMMENDATIONS; numRecommendations++) {
			precision = precisions.get(numRecommendations).getMean();
			recall = recalls.get(numRecommendations).getMean();
			System.out.println((numRecommendations+1) +": " + precision + " [" + recall + "]");
		}
	}
}
