package ca.uleth.bugtriage.sibyl.misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.AssignmentEvent;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class Assigners {

	public static void main(String[] args) {
		int NUM_MONTHS = 1;
		Set<BugReport> reports = Utils.getReports(Utils.getTrainingSet(EclipseData.ECLIPSE_DIR, NUM_MONTHS, EclipseData.LAST_TRAINING_MONTH));
		
		Map<String, FrequencyTable> assigners = new HashMap<String, FrequencyTable>();
		BugActivityEvent bugEvent;
		for (BugReport report : reports) {
			BugActivity reportActivity = report.getActivity();
			List<AssignmentEvent> events = reportActivity.getAssignmentEvents();
			if (events.isEmpty()) {
				//System.out.println("No assignments: #" + report.getId());
				// Check if status was ever changed to ASSIGNED
				for (Object event : reportActivity) {
					bugEvent = (BugActivityEvent)event;
					if (bugEvent.getWhat().equals("ASSIGNED")) {
						String name = bugEvent.getName();
						System.out.println("\tAssigners: " + name);
						if(assigners.containsKey(name) == false){
							assigners.put(name, new FrequencyTable());
						}
						FrequencyTable assignedTo = assigners.get(name);
						assignedTo.add(bugEvent.getAdded());
					}
				}
			} else {
				for (AssignmentEvent event : events) {
					String name = event.getName();
					if(assigners.containsKey(name) == false){
						assigners.put(name, new FrequencyTable());
					}
					FrequencyTable assignedTo = assigners.get(name);
					assignedTo.add(event.getAdded());
				}
			}
		}
		
		for (String assigner : assigners.keySet()) {
			System.out.println(assigner + ": ");
			FrequencyTable assignedTo = assigners.get(assigner);
			System.out.println(assignedTo);
		}
		//System.out.println(assigners);
	}

}
