package ca.uleth.bugtriage.sibyl.classifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.eval.DeveloperInfo;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import weka.classifiers.Classifier;
import weka.core.Instance;

public class ComponentClassifier extends MLClassifier {

	private static final long serialVersionUID = 4916033445607428881L;

	protected final static String COMPONENT = "Component";

	private Map<String, TriageClassifier> classifiers;

	public ComponentClassifier(MLAlgorithm algorithm) {
		super(algorithm);
		this.classifiers = new HashMap<String, TriageClassifier>();
	}

	@Override
	public String getName() {
		return "CompositeClassifier" + super.getName();

	}

	@Override
	protected List<Classification> classify(Instance instance) throws Exception {
		throw new NotImplementedException();
	}

	public List<Classification> classify(BugReport report,
			String component) {
		List<Classification> componentClassifications = new ArrayList<Classification>();
		TriageClassifier componentClassifier = this.classifiers.get(component);
		if (componentClassifier != null) {
			componentClassifications = componentClassifier.classify(report);
		} else {
			componentClassifications.add(new Classification(
					HeuristicClassifier.CANNOT_CLASSIFY, component
							+ " unknown by classifier", 1.0));
		}
		return componentClassifications;
	}

	@Override
	public List<Classification> classify(BugReport report) {
		String component = report.getComponent();
		return this.classify(report, component);
	}

	@Override
	public void train(String[] trainingSets, String[] testingSet,
			String developerInfoFile, Heuristic heuristic) {

		Set<BugReport> bugs = Utils.getReports(trainingSets);
		Set<BugReport> testingBugs;
		if (developerInfoFile != null) {
			DeveloperInfo developerInfo = new DeveloperInfo(developerInfoFile);
			testingBugs = developerInfo.getTestingSet(testingSet);
		} else if (testingSet != null) {
			testingBugs = Utils.getReports(testingSet);
		} else {
			testingBugs = new HashSet<BugReport>();
		}
		Map<String, Set<BugReport>> partitions = partition(bugs);
		createClassifiers(partitions, testingBugs, heuristic);
	}

	private Map<String, Set<BugReport>> partition(
			Set<BugReport> bugs) {
		Map<String, Set<BugReport>> partitions = new HashMap<String, Set<BugReport>>();

		String component;
		Set<BugReport> reports;
		for (BugReport report : bugs) {
			component = report.getComponent();
			if (partitions.containsKey(component) == false) {
				partitions.put(component, new HashSet<BugReport>());
			}
			reports = partitions.get(component);
			reports.add(report);
		}
		return partitions;
	}

	private void createClassifiers(
			Map<String, Set<BugReport>> partitions,
			Set<BugReport> testingReports, Heuristic heuristic) {

		Set<BugReport> trainingReports;
		TriageClassifier partitionClassifier = null;
		System.out.println("Components: " + partitions.keySet().size());
		for (String partitionName : partitions.keySet()) {
			trainingReports = partitions.get(partitionName);

			// printIds(trainingReports);

			try {
				partitionClassifier = new MLClassifier(Classifier.makeCopy(this.classifier));
				if (heuristic == Heuristic.SUBCOMPONENT) {
					Profiles profiles = ca.uleth.bugtriage.sibyl.classifier.Classifier.createSubcomponentProfiles(trainingReports, heuristic);
					partitionClassifier.setProfile(profiles);
				} else {
					partitionClassifier.setProfile(this.profile);
				}

				System.out.println("\n--> " + partitionName + " <--");

				partitionClassifier.train(trainingReports, testingReports,
						heuristic);

				this.classifiers.put(partitionName, partitionClassifier);
			} catch (Exception e) {
				// Classifier could not be created
				System.err.println("Classifier for " + partitionName
						+ " could not be created!");

			}
		}
	}

	private void printIds(Set<BugReport> trainingReports) {
		List<Integer> ids = new ArrayList<Integer>();
		for (BugReport report : trainingReports) {
			ids.add(new Integer(report.getId()));
		}
		Collections.sort(ids);
		System.out.println(ids);

	}

	@Override
	public List<String> getClasses() {
		List<String> classes = new ArrayList<String>();
		for (TriageClassifier componentClassifier : this.classifiers.values()) {
			classes.addAll(componentClassifier.getClasses());
		}
		return classes;
	}

	public List<String> getClasses(String component){
		//System.out.println("Getting classes for: " + component);
		TriageClassifier classifier = this.classifiers.get(component);
		if(classifier == null) {
			System.err.println("No classifier for " + component + "!");
			return Collections.EMPTY_LIST;
		}
		return classifier.getClasses();
	}
	
	@Override
	public boolean inTrainingSet(BugReport report) {
		String component = report.getComponent();
		TriageClassifier componentClassifier = this.classifiers.get(component);
		if (componentClassifier == null) {
			// Classifier doesn't have a classifier for this component
			return false;
		}
		return componentClassifier.inTrainingSet(report);

	}

	@Override
	public int numClasses() {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (TriageClassifier componentClassifier : this.classifiers.values()) {
			stats.addValue(componentClassifier.getClasses().size());
		}

		System.out.println("Min Classes: " + stats.getMin());
		System.out.println("Mean Classes: " + stats.getMean());
		System.out.println("Max Classes: " + stats.getMax());

		return (int) stats.getMean();
	}

}
