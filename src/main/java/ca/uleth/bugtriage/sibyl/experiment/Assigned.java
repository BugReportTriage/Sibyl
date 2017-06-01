package ca.uleth.bugtriage.sibyl.experiment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.AssignmentEvent;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class Assigned {

	private static final DateFormat formatter = new SimpleDateFormat(
	"yyyy-MM-dd");
	
	public static void main(String[] args) {
		int numMonths = 8;

		String[] reportFiles = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR,
				numMonths, EclipseData.LAST_TRAINING_MONTH);
		/*
		 * String[] reportFiles = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
		 * numMonths , FirefoxData.LAST_TRAINING_MONTH);
		 * 
		 * String[] reportFiles = Utils.getTrainingSet(GccData.GCC_DIR,
		 * numMonths , GccData.LAST_TRAINING_MONTH);
		 * 
		 * String[] reportFiles = Utils.getTrainingSet(MylarData.MYLAR_DIR,
		 * numMonths , MylarData.LAST_TRAINING_MONTH);
		 */
		/*
		 * String[] reportFiles =
		 * Utils.getTrainingSet(BugzillaData.BUGZILLA_DIR, numMonths ,
		 * BugzillaData.LAST_TRAINING_MONTH);
		 */

		
		FrequencyTable freq = new FrequencyTable();
		List<String> outliers = new ArrayList<String>();
		for (String dataFile : reportFiles) {
			Set<BugReport> reports = Utils.getReports(dataFile);
			for (BugReport report : reports) {
				BugActivity assignedTo = report.getActivity();
				for (BugActivityEvent bugActivityEvent : assignedTo) {
					if (bugActivityEvent instanceof AssignmentEvent) {
						String date = bugActivityEvent.getDate().substring(0, 10);
						freq.add(date);
						if(date.equals("2006-04-12")){
							if(bugActivityEvent.getName().equals("michaelvanmeekeren@yahoo.ca") && bugActivityEvent.getAdded().equals("Tod_Creasey@ca.ibm.com")){
							outliers.add(report.getId() + " " + report.getResolution()+ ": " + bugActivityEvent);
							}
						}
					}
				}

			}
		}
		System.out.println(freq);
		
		
		//for(String line : outliers)
		//System.out.println(line);
		System.out.println("Size: " + outliers.size());
	}

}
