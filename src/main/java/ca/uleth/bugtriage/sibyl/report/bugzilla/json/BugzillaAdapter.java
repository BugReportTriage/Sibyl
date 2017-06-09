package ca.uleth.bugtriage.sibyl.report.bugzilla.json;

import java.util.List;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugReportHistoryBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.HistoryBugzilla;

public class BugzillaAdapter {

	public static BugReport convertReport(ReportBugzilla r) {
		
		BugReport report = new BugReport(r.getId());
		report.setAssigned(r.getAssigned_to());
		report.setCcList(r.getCc());
		report.setChanged(r.getLast_change_time());
		report.setComponent(r.getComponent());
		//report.setCreated(r.getCreation_time()); // TODO Need DataConverter
		report.setHardware(r.getPlatform());
		report.setOperatingSystem(r.getOp_sys());
		report.setReporter(r.getCreator());
		report.setResolution(r.getResolution());
		report.setSummary(r.getSummary());
		report.setPriority(r.getPriority());
		report.setSeverity(r.getSeverity());
		report.setDuplicateOf(r.getDupe_of().toString());
		
		//report.getSubcomponent(); // TODO Only Eclipse?
				
		return report;
	}

	public static BugActivity convertHistory(BugReportHistoryBugzilla historyBugzilla) {
		BugActivity history = new BugActivity();
		List<HistoryBugzilla> events = historyBugzilla.getBugs().get(0).getHistory(); // only ever one 'bug'
		for(HistoryBugzilla e : events){
			String type = e.getChanges().get(0).getFieldName();
			String change = e.getChanges().get(0).getAdded();
			BugActivityEvent event = BugActivityEvent.createEvent(type, change);
			event.setRemoved(e.getChanges().get(0).getRemoved());
			event.setDate(e.getWhen());
			event.setName(e.getWho());
			
			history.addEvent(event);
		}
		return history;
	}

}
