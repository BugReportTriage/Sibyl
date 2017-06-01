package ca.uleth.bugtriage.sibyl.eval;

import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class RecommenderEval {

	private static final int MAX_RECOMMENDATIONS = 5;

	private static final HeuristicClassifier HEURISTIC = Heuristic.ECLIPSE
			.getClassifier();

	public static void main(String[] args) {
		String[] dataFiles = dataFiles(new File(Project.PLATFORM.getDataDir()));
		String classifierFile = Environment.getClassifierDir() + "platform.classifier";
		RecommenderEval.run(
				classifierFile, getTestingFiles(dataFiles, 30));
	}

	public static void run(String classifierFile, String[] testSet) {
		TriageClassifier classifier = null;
		try {
			classifier = MLClassifier.load(new File(classifierFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Getting test reports [" + testSet.length + "]");
		Set<BugReport> testReports = Utils.getReports(testSet);

		double precision, recall, correct;
		DescriptiveStatistics precisionStats, recallStats;
		List<DescriptiveStatistics> precisions = new ArrayList<DescriptiveStatistics>();
		List<DescriptiveStatistics> recalls = new ArrayList<DescriptiveStatistics>();

		for (int numRecommendations = 1; numRecommendations <= MAX_RECOMMENDATIONS; numRecommendations++) {
			precisions.add(new DescriptiveStatistics());
			recalls.add(new DescriptiveStatistics());
		}

		for (BugReport report : testReports) {
			precision = 0;
			recall = 0;
			String actual = getActual(report);
			// System.out.println(report.getId() + ": " + actual);
			List<Classification> predictions = classifier.classify(report);
			for (int numRecommendations = 1; numRecommendations <= MAX_RECOMMENDATIONS; numRecommendations++) {
				precisionStats = precisions.get(numRecommendations - 1);
				recallStats = recalls.get(numRecommendations - 1);
				correct = 0;
				for (int index = 0; index < numRecommendations; index++) {
					String predicted = predictions.get(index)
							.getClassification();
					// System.out.println("\t" + predicted);
					if (predicted.equals(actual)) {
						correct++;

					}
				}

				precisionStats.addValue(correct / numRecommendations);
				recallStats.addValue(correct / 1);
			}
		}

		for (int numRecommendations = 0; numRecommendations < MAX_RECOMMENDATIONS; numRecommendations++) {
			precision = precisions.get(numRecommendations).getMean();
			recall = recalls.get(numRecommendations).getMean();
			System.out.println((numRecommendations + 1) + ": " + precision
					+ " [" + recall + "]");
		}
	}

	private static String getActual(BugReport report) {
		return HEURISTIC.classify(report).getClassification();
	}

	private static String[] dataFiles(File dataDir) {
		List<String> dataFilenames = new ArrayList<String>();
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith("1days.bugs");
			}
		};

		if (dataDir.isDirectory()) {
			File[] files = dataDir.listFiles(filter);
			for (File file : files) {
				String name = file.getAbsolutePath();
				dataFilenames.add(name);
			}
		}
		Collections.sort(dataFilenames, Collections.reverseOrder());
		return dataFilenames.toArray(new String[dataFilenames.size()]);
	}
	
	private static String[] getTestingFiles(String[] dataFiles, int days) {
		int arraySize = (dataFiles.length >= days) ? days
				: dataFiles.length;

		String[] trainingData = new String[arraySize];
		System.arraycopy(dataFiles, 0, trainingData, 0, arraySize);
		return trainingData;
	}
}
