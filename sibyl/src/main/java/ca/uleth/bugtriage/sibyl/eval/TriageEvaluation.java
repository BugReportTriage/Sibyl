package ca.uleth.bugtriage.sibyl.eval;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class TriageEvaluation {

	private final Set<BugReport> reports;

	private final int topX;

	private final TriageClassifier triageClassifier;

	private final Map<Integer, Set<String>> developerInfo;

	private final DescriptiveStatistics precisionStats, recallStats,
			developerStats;

	public Set<String> notMapped = new HashSet<String>();

	public TriageEvaluation(TriageClassifier triageClassifier,
			Map<Integer, Set<String>> developerInfo,
			Set<BugReport> reports, int topX) {
		this.reports = new HashSet<BugReport>(reports);
		this.topX = topX;
		this.developerInfo = developerInfo;
		this.triageClassifier = triageClassifier;
		this.precisionStats = new DescriptiveStatistics();
		this.recallStats = new DescriptiveStatistics();
		this.developerStats = new DescriptiveStatistics();
	}

	public void run() {

		int correct, precision, recall, testingInstances = 0;

		System.out.println("----------------------------------------------");
		System.out.println("Top X: " + this.topX);
		for (BugReport report : this.reports) {

			if (this.triageClassifier.inTrainingSet(report)) {
				System.out.println(report.getId() + " in training set");
				continue;
			}

			Set<String> possibleDevelopers = this.developerInfo
					.get(new Integer(report.getId()));
			if (possibleDevelopers == null) {
				// System.out.println("No developer info for " +
				// report.getId());
				continue;
			}

			testingInstances++;
			this.developerStats.addValue(possibleDevelopers.size());

			// System.out.println("\nBug " + report.getId() + " - " +
			// report.getAttribute("Component").getValue());

			correct = this.numCorrect(report, this.triageClassifier,
					possibleDevelopers);

			precision = (correct * 100) / this.topX;
			recall = (correct * 100) / possibleDevelopers.size();

			// System.out.println("Precision: " + precision);
			// System.out.println("Recall: " + recall);

			this.precisionStats.addValue(precision);
			this.recallStats.addValue(recall);
		}

		System.out.println("Testing Instances: " + testingInstances);
		this.printStats();
	}

	public double getPrecision() {
		return this.precisionStats.getMean();
	}

	public double getRecall() {
		return this.recallStats.getMean();
	}

	private void printStats() {
		System.out.println("Min Precision: " + this.precisionStats.getMin());
		System.out.println("Mean Precision: " + this.precisionStats.getMean());
		System.out.println("Max Precision: " + this.precisionStats.getMax());
		System.out.println();
		System.out.println("Min Recall: " + this.recallStats.getMin());
		System.out.println("Mean Recall: " + this.recallStats.getMean());
		System.out.println("Max Recall: " + this.recallStats.getMax());
		System.out.println();
		System.out.println("Min Group Size: " + this.developerStats.getMin());
		System.out.println("Mean Group Size: " + this.developerStats.getMean());
		System.out.println("Max Group Size: " + this.developerStats.getMax());
	}

	private int numCorrect(BugReport report, TriageClassifier classifier,
			Set<String> possibleDevelopers) {
		Classification prediction;
		List<Classification> classifications;
		int stopIndex;
		int correct = 0;

		//System.out.println("Bug #: " + report.getId());
		//System.out.println("Developers: " + possibleDevelopers);

		classifications = classifier.classify(report);
		Collections.sort(classifications);

		stopIndex = classifications.size() - this.topX;
		for (int index = classifications.size() - 1; index >= stopIndex
				&& index >= 0; index--) {
			prediction = classifications.get(index);

			// System.out.println(prediction.getClassification());

			if (possibleDevelopers.contains(prediction.getClassification())) {
				correct++;
			} else {
				boolean found = false;
				for (Set<String> developerList : this.developerInfo.values()) {
					if (developerList.contains(prediction.getClassification())) {
						found = true;
						continue;
					}
				}
				if (found == false) {
					// System.out.println(prediction.getClassification()
					// + " not mapped");
					this.notMapped.add(prediction.getClassification());
				}
			}
			//System.out.println((this.topX - (index - stopIndex)) + ": "
				//	+ prediction.getClassification());
		}

		 //System.out.println("Correct: " + correct);
		return correct;
	}
}
