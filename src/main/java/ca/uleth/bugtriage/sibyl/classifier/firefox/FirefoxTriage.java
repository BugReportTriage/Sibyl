package ca.uleth.bugtriage.sibyl.classifier.firefox;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import weka.classifiers.Evaluation;

public class FirefoxTriage {
	
	public static void run(TriageClassifier classifier, Set<BugReport> testingReports) {	    
		new Triage().evaluate(classifier, testingReports, FirefoxData.DEVELOPER_INFO);
	}
	
	public static void main(String[] args) {
		Dataset dataset = new BugzillaDataset(Project.FIREFOX);
		List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports());
		System.out.println("Dataset size: " + reports.size());
		
		ClassifierType classifierType = ClassifierType.SVM;
		Heuristic heuristic = Heuristic.MOZILLA;						
		Profiles profiles = Classifier.createDeveloperProfiles(dataset);
		
		TriageClassifier classifier = Classifier.create(classifierType, dataset.getTrainingReports(),
				dataset.getTestingReports(), null, heuristic, profiles);
		
		try {
			Evaluation eval = new Evaluation(classifier.getTrainingInstances());
			MLClassifier mlc = (MLClassifier)classifier;
			 eval.evaluateModel(mlc.getClassifier(), classifier.getTestingInstances());
			 System.out.println(eval.toSummaryString()+eval.toMatrixString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	
		for (BugReport testReport : dataset.getTestingReports()) {
			System.out.println("Classifying " + testReport.getId());
			List<Classification> predictions = classifier.classify(testReport);			
			System.out.println(predictions);
		}
		*/
	}
}
