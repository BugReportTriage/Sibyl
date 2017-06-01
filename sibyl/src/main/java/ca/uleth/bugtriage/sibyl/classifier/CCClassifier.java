package ca.uleth.bugtriage.sibyl.classifier;

import java.util.Set;

import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;

public class CCClassifier extends MLClassifier {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 7181134617657185982L;

	private static final int CC_THRESHOLD = 15;

	private final FastVector classNames = new FastVector();

	public CCClassifier(MLAlgorithm algorithm) {
		super(algorithm);
	}

	@Override
	protected void addInstances(Set<BugReport> bugs, Heuristic heuristic,
			Instances dataset) {
		Instance trainingInstance, parentInstance;

		for (BugReport report : bugs) {

			parentInstance = this.makeTrainingInstance(report, dataset);
			for (String cc : report.getCC()) {
				if (this.classNames.contains(cc)) {
					trainingInstance = (Instance) parentInstance.copy();

					// Set class
					trainingInstance.setClassValue(cc);
					dataset.add(trainingInstance);
				}
			}
		}

		dataset.compactify();
	}

	protected FastVector getClassNames(Set<BugReport> reports,
			Heuristic heuristic) {

		System.out.println("CC Filtering...");
		FrequencyTable ccNames = new FrequencyTable();
		for (BugReport report : reports) {
			for (String cc : report.getCC()) {
				ccNames.add(cc);
			}
		}

		System.out.println("Num Classes (Before): " + ccNames.getKeys().size());

		for (String name : ccNames.getKeys()) {
			int freq = ccNames.getFrequency(name);
			if (freq >= CC_THRESHOLD) {
				if (this.classNames.contains(name) == false)
					this.classNames.addElement(name);

			}
		}
		System.out.println("Num Classes (After): " + this.classNames.size());
		// System.err.println("Removed: " + removed);
		return this.classNames;
	}
}
