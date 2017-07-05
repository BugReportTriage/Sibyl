package ca.uleth.bugtriage.sibyl.experiment.newdeveloper.firefox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.eval.Triage;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import weka.classifiers.Evaluation;
import weka.core.Instance;

public class FirefoxSVM {

	public static void main(String[] args) {
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		Set<BugReport> reports = new TreeSet<BugReport>(dataset.importReports());
		System.out.println("Dataset size: " + reports.size());

		ClassifierType classifierType = ClassifierType.SVM;
		Heuristic heuristic = Heuristic.MOZILLA;
		Profiles profiles = Classifier.createDeveloperProfiles(dataset);

		// Partition data by developer
		Map<String, Set<BugReport>> byDeveloper = new HashMap<String, Set<BugReport>>();
		Set<BugReport> devReports;
		HeuristicClassifier hClassifier = heuristic.getClassifier();
		for (BugReport report : reports) {
			String fixedBy = hClassifier.classify(report).getClassification();
			if (profiles.contains(fixedBy)) {
				devReports = byDeveloper.get(fixedBy);
				if (devReports == null) {
					devReports = new TreeSet<BugReport>();
					byDeveloper.put(fixedBy, devReports);
				}
				devReports.add(report);
			}
		}

		/*
		 * for(String dev : byDeveloper.keySet()){ System.out.println(dev + ": "
		 * + byDeveloper.get(dev)); }
		 */

		for (String dev : byDeveloper.keySet()) {			
			System.out.println("========== " + dev + " ==========");
			List<BugReport> testingList = new ArrayList<BugReport>(byDeveloper.get(dev));
			Set<BugReport> trainingSet = new TreeSet<BugReport>();
			for (Entry<String, Set<BugReport>> entry : byDeveloper.entrySet()) {
				if (entry.getKey().equals(dev) == false)
					trainingSet.addAll(entry.getValue());
			}
			while (testingList.isEmpty() == false) {
				System.out.println("Training: " + trainingSet.size() + " Testing: " + testingList.size());
				TriageClassifier classifier = Classifier.create(classifierType, trainingSet, new TreeSet<BugReport>(testingList), null,
						heuristic, profiles);

				try {
					Evaluation eval = new Evaluation(classifier.getTestingInstances());
					MLClassifier mlc = (MLClassifier) classifier;

					for (Instance i : classifier.getTestingInstances()) {
						eval.evaluateModelOnceAndRecordPrediction(mlc.getClassifier(), i);
					}

					// eval.evaluateModel(mlc.getClassifier(),
					// classifier.getTestingInstances());
					// System.out.println(eval.toSummaryString() +
					// eval.toMatrixString());
					//System.out.println("=====> Correct: " + eval.correct() + " Incorrect: " + eval.incorrect() + " <=====");
					System.out.println("=====> New dev classified: " + (eval.incorrect() - testingList.size())+ " <=====");

				} catch (Exception e) { // TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				trainingSet.add(testingList.remove(0));
			}
			System.out.println("JAIL BREAK!!!");
			break;
		}
		/*
		 * for (BugReport testReport : dataset.getTestingReports()) {
		 * System.out.println("Classifying " + testReport.getId());
		 * List<Classification> predictions = classifier.classify(testReport);
		 * System.out.println(predictions); }
		 */
	}
}
