package ca.uleth.bugtriage.sibyl.experiment;


import java.util.List;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.classifier.bugzilla.BugzillaData;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxData;
import ca.uleth.bugtriage.sibyl.classifier.gcc.GccData;
import ca.uleth.bugtriage.sibyl.classifier.mylar.MylarData;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class Reassigned {

	public static void main(String[] args) {

		int THRESHOLD = 1;

		
		int numMonths = 8;
		/*
		String[] reportFiles = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR,
				numMonths , EclipseData.LAST_TRAINING_MONTH);
		
		String[] reportFiles = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
				numMonths , FirefoxData.LAST_TRAINING_MONTH);
		
		String[] reportFiles = Utils.getTrainingSet(GccData.GCC_DIR,
				numMonths , GccData.LAST_TRAINING_MONTH);
				
		String[] reportFiles = Utils.getTrainingSet(MylarData.MYLAR_DIR,
				numMonths , MylarData.LAST_TRAINING_MONTH);
				*/
		String[] reportFiles = Utils.getTrainingSet(BugzillaData.BUGZILLA_DIR,
				numMonths , BugzillaData.LAST_TRAINING_MONTH);
		
		int errorCount = 0;
		int totalReports = 0;
		for (String dataFile : reportFiles) {
			Set<BugReport> reports = Utils.getReports(dataFile);
			totalReports += reports.size();
			for (BugReport report : reports) {
				List<String> assignedTo = report.getActivity()
						.getAllAssignedTo();
				if (assignedTo.size() > THRESHOLD) {
					//System.out.println(report.getId() + ": " + assignedTo.toString());
					// System.err.println("Assigned To Error: " +
					// report.getId());
					errorCount++;
				} else {
					// System.err.println("Assigned To Correct: " +
					// report.getId());
				}
			}
		}
		int errorRate = (int) (errorCount / (totalReports * 1.0) * 100);
		System.out.println("Error Rate: " + errorRate + "% (" + errorCount
				+ " / " + totalReports + ")");
		int correctRate = (int) ((totalReports - errorCount)
				/ (totalReports * 1.0) * 100);
		System.out.println("Correct Rate: " + correctRate + "% ("
				+ (totalReports - errorCount) + " / " + totalReports + ")");
	}

}
