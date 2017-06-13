package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.naming.OperationNotSupportedException;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;

import weka.core.Instance;
import weka.core.Instances;

public class NaiveTriageClassifier extends TriageClassifier implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8522434037821716464L;

	private final List<String> classes;

	private final List<Classification> predictionList;

	public NaiveTriageClassifier() {
		this.predictionList = new ArrayList<Classification>();
		this.classes = new ArrayList<String>();
	}

	@Override
	public String getName() {
		return "NaiveTriageClassifier";
	}

	@Override
	protected void build(Instances dataset) throws Exception {
		throw new OperationNotSupportedException();
	}

	@Override
	protected List<Classification> classify(Instance instance) throws Exception {
		throw new OperationNotSupportedException();
	}

	@Override
	public void train(Set<BugReport> reports, Set<BugReport> testingBugs, Heuristic heuristic) throws Exception {

		System.out.println("Training " + this.getName());
		heuristic.getClassifier().useDuplicateResolver(true);

		// Filter reports
		System.out.println("Training Set before removing test reports: " + reports.size());
		reports.removeAll(testingBugs);
		System.out.println("Training Set after removing test reports: " + reports.size());
		reports = getTrainingBugs(reports, heuristic);

		FrequencyTable frequencyTable = new FrequencyTable();
		for (BugReport report : reports) {
			String classification = heuristic.getClassifier().classify(report).getClassification();
			if (classification.equals(HeuristicClassifier.CANNOT_CLASSIFY) == false
					&& classification.equals(HeuristicClassifier.HEURISTIC_FAILURE) == false) {
				frequencyTable.add(classification);
			}
		}

		System.out.println(frequencyTable);

		System.out.println("Creating prediction list");
		double totalReports = frequencyTable.getStatistics().getSum();
		for (String developer : frequencyTable.getKeys()) {
			this.predictionList.add(new Classification(developer, "Number Resolved",
					frequencyTable.getFrequency(developer) / totalReports));
			this.classes.add(developer);
		}
	}

	@Override
	public List<Classification> classify(BugReport report) {
		return this.predictionList;
	}

	public static NaiveTriageClassifier load(File file) {
		try {
			System.out.println("Reading in classifier");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
			NaiveTriageClassifier ntc = (NaiveTriageClassifier) in.readObject();
			in.close();
			return ntc;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		// Should never happen
		return null;
	}

	@Override
	public int numClasses() {
		return this.predictionList.size();
	}

	@Override
	public List<String> getClasses() {
		return this.classes;
	}

}
