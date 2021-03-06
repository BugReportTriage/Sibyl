package ca.uleth.bugtriage.sibyl.report.bugzilla.json;

import java.util.ArrayList;
import java.util.List;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Comment;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugReportHistoryBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.ChangeBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.HistoryBugzilla;

public class BugzillaAdapter {

	public static BugReport convertReport(ReportBugzilla r) {

		BugReport report = new BugReport();
		report.setId(r.getId());
		report.setAssigned(r.getAssigned_to());
		report.setCCList(r.getCc());
		report.setChanged(r.getLast_change_time());
		report.setComponent(r.getComponent());
		// report.setCreated(r.getCreation_time()); // TODO Need DataConverter
		report.setHardware(r.getPlatform());
		report.setOperatingSystem(r.getOp_sys());
		report.setReporter(r.getCreator());
		report.setResolution(r.getResolution());
		report.setSummary(r.getSummary());
		report.setPriority(r.getPriority());
		report.setSeverity(r.getSeverity());
		if (r.getDupe_of() != null)
			report.setDuplicateOf(r.getDupe_of().toString());
		report.setStatus(r.getStatus());
		// report.getSubcomponent(); // TODO: Only Eclipse?

		return report;
	}

	public static BugActivity convertHistory(BugReportHistoryBugzilla historyBugzilla) {
		BugActivity history = new BugActivity();
		List<HistoryBugzilla> events = historyBugzilla.getBugs().get(0).getHistory(); // only
																						// ever
																						// one
																						// 'bug'		
		for (HistoryBugzilla e : events) {
			for (ChangeBugzilla c : e.getChanges()) {
				String type = c.getFieldName();
				if(c.getAttachmentId() != null)
					type += ":" + c.getAttachmentId();
				String change = c.getAdded();
				BugActivityEvent event = BugActivityEvent.createEvent(type, change);
				event.setRemoved(c.getRemoved());
				event.setDate(e.getWhen());
				event.setName(e.getWho());

				history.addEvent(event);
			}
		}
		return history;
	}

	public static List<ca.uleth.bugtriage.sibyl.report.Comment> convertComments(List<Comment> commentsBugzilla) {
		List<ca.uleth.bugtriage.sibyl.report.Comment> comments = new ArrayList<ca.uleth.bugtriage.sibyl.report.Comment>();
		for (Comment c : commentsBugzilla) {
			ca.uleth.bugtriage.sibyl.report.Comment comment = new ca.uleth.bugtriage.sibyl.report.Comment();
			comment.setAuthour(c.getAuthor());
			comment.setCreated(c.getCreationTime());
			comment.setText(c.getText());

			comments.add(comment);
		}
		return comments;
	}

}
