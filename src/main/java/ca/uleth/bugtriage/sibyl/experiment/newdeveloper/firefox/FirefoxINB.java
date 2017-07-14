package ca.uleth.bugtriage.sibyl.experiment.newdeveloper.firefox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import weka.classifiers.Evaluation;
import weka.classifiers.UpdateableClassifier;
import weka.core.Instance;
import weka.core.Instances;

public class FirefoxINB {

    public static void main(String[] args) {
	Dataset dataset = new BugzillaDataset(Project.FIREFOX);
	Set<BugReport> reports = new TreeSet<BugReport>(dataset.importReports());
	System.out.println("Dataset size: " + reports.size());

	ClassifierType classifierType = ClassifierType.INB;
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

	int stopAfter = 1;
	for (String dev : byDeveloper.keySet()) {
	    int atSign = dev.indexOf('@');
	    String devName = dev.substring(0, atSign);
	    File devFile = new File(Project.FIREFOX.dataDir + "/newDeveloper/inb/" + devName + ".ranks");
	    devFile.getParentFile().mkdirs();
	    try {
		PrintWriter output = new PrintWriter(devFile);
		output.println("Testing Reports,Avg. Rank");

		System.out.println("========== " + dev + " ==========");
		List<BugReport> testingList = new ArrayList<BugReport>(byDeveloper.get(dev));
		Set<BugReport> trainingSet = new TreeSet<BugReport>();
		for (Entry<String, Set<BugReport>> entry : byDeveloper.entrySet()) {
		    if (entry.getKey().equals(dev) == false)
			trainingSet.addAll(entry.getValue());
		}

		Classification newDeveloper = new Classification(dev, "Comparable", -1);
		List<Classification> classifications;

		TriageClassifier classifier = Classifier.create(classifierType, trainingSet,
			new TreeSet<BugReport>(testingList), null, heuristic, profiles);

		Instances testing = classifier.getTestingInstances();

		while (testing.isEmpty() == false) {
		    if (classifier.getClasses().contains(dev) == false) {
			System.err.println(dev + " not a class in classifier");
		    }

		    try {
			MLClassifier mlc = (MLClassifier) classifier;
			int totalRank = 0;

			for (Instance i : testing) {

			    classifications = mlc.classify(i);
			    Collections.sort(classifications, Collections.reverseOrder());
			    System.out.println(classifications.subList(0, 5));
			    int rank = classifications.indexOf(newDeveloper);
			    if (rank != -1) {
				totalRank += rank + 1;
				// System.out.println(dev + " found at " +
				// rank);
			    }
			}
			double avgRank = totalRank / testingList.size();
			System.out.println(
				"=====> Avg. Rank: " + avgRank + " / " + classifier.getClasses().size() + " <=====");
			output.println(testingList.size() + "," + avgRank);
			output.flush();

			Instance newDevInstance = testing.remove(0);
			System.err.println(newDevInstance.classValue());
			((UpdateableClassifier) mlc.getClassifier()).updateClassifier(newDevInstance);
		    } catch (Exception e) { // TODO Auto-generated catch block
			e.printStackTrace();
		    }

		}

		output.close();

		stopAfter--;
		if (stopAfter == 0) {
		    System.out.println("JAIL BREAK!!!");
		    break;
		}
		
	    } catch (FileNotFoundException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	}
	
    }
}
