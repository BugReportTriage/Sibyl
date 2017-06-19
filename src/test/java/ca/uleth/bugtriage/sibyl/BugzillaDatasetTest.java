package ca.uleth.bugtriage.sibyl;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.BugzillaFlag;
import ca.uleth.bugtriage.sibyl.activity.events.AttachmentFlagState;
import ca.uleth.bugtriage.sibyl.activity.events.AttachmentFlagStatus;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.activity.events.FlagEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReportsBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.ReportBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Comment;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Report;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugHistory;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugReportHistoryBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.ChangeBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.HistoryBugzilla;

public class BugzillaDatasetTest {

	private static Project testFirefox;

	@BeforeClass
	public static void setup() {
		testFirefox = Project.FIREFOX;
		testFirefox.startDate = "2010-06-01";
		testFirefox.endDate = "2010-06-02";
	}

	@Test
	public void testGetDataBugzilla() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getReports(testFirefox);

		ObjectMapper mapper = new ObjectMapper();
		BugReportsBugzilla bugs = mapper.readValue(data, BugReportsBugzilla.class);
		List<ReportBugzilla> reports = bugs.getBugs();

		Assert.assertEquals(2, reports.size());
		Assert.assertEquals(569360, reports.get(0).getId().intValue());
		Assert.assertEquals(569459, reports.get(1).getId().intValue());
	}
	
	@Test
	public void testGetReportHistoryBugzillaNormal() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getHistory(Project.FIREFOX, "569360");

		ObjectMapper mapper = new ObjectMapper();
		BugReportHistoryBugzilla history = mapper.readValue(data, BugReportHistoryBugzilla.class);
		List<BugHistory> reports = history.getBugs();

		Assert.assertEquals(1, reports.size());
		Assert.assertEquals(569360, reports.get(0).getId().intValue());

		List<HistoryBugzilla> changes = reports.get(0).getHistory();
		Assert.assertEquals(8, changes.size());

		HistoryBugzilla change = changes.get(0);
		Assert.assertEquals("mounir@lamouri.fr", change.getWho());
		Assert.assertEquals("2010-06-01T15:43:20Z", change.getWhen());

		ChangeBugzilla details = change.getChanges().get(0);
		Assert.assertEquals("review?(gavin.sharp@gmail.com)", details.getAdded());
		Assert.assertEquals("448527", details.getAttachmentId());
		Assert.assertEquals("flagtypes.name", details.getFieldName());
		Assert.assertEquals("", details.getRemoved());
	}

	@Test
	public void testGetReportHistoryBugzillaNeedInfo() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getHistory(Project.FIREFOX, "1236213");

		ObjectMapper mapper = new ObjectMapper();
		BugReportHistoryBugzilla history = mapper.readValue(data, BugReportHistoryBugzilla.class);
		List<BugHistory> reports = history.getBugs();

		Assert.assertEquals(1, reports.size());
		Assert.assertEquals(1236213, reports.get(0).getId().intValue());

		List<HistoryBugzilla> changes = reports.get(0).getHistory();
		Assert.assertEquals(8, changes.size());

		HistoryBugzilla change = changes.get(2);
		
		// Second change made at this time		 
		ChangeBugzilla details = change.getChanges()
				.get(1); 
		Assert.assertEquals("needinfo?(shorlander@mozilla.com)", details.getAdded());
		Assert.assertNull(details.getAttachmentId());
		
		BugActivityEvent event = BugActivityEvent.createEvent(details.getFieldName(), details.getAdded());
		Assert.assertTrue(event instanceof FlagEvent);
		List<BugzillaFlag> flags = ((FlagEvent) event).getFlags();
		Assert.assertEquals(1, flags.size());
		Assert.assertEquals(AttachmentFlagStatus.NEEDINFO, flags.get(0).getStatus());
		Assert.assertEquals(AttachmentFlagState.REQUESTED, flags.get(0).getState());
	}

	@Test
	public void testGetReportHistoryBugzillaQEVerify() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getHistory(Project.FIREFOX, "1236229");

		ObjectMapper mapper = new ObjectMapper();
		BugReportHistoryBugzilla history = mapper.readValue(data, BugReportHistoryBugzilla.class);
		List<BugHistory> reports = history.getBugs();

		Assert.assertEquals(1, reports.size());
		Assert.assertEquals(1236229, reports.get(0).getId().intValue());

		List<HistoryBugzilla> changes = reports.get(0).getHistory();
		Assert.assertEquals(10, changes.size());

		HistoryBugzilla change = changes.get(0);
		ChangeBugzilla details = change.getChanges().get(0);
		Assert.assertEquals("qe-verify-", details.getAdded());
		Assert.assertNull(details.getAttachmentId());
		
		BugActivityEvent event = BugActivityEvent.createEvent(details.getFieldName(), details.getAdded());
		Assert.assertTrue(event instanceof FlagEvent);
		List<BugzillaFlag> flags = ((FlagEvent) event).getFlags();
		Assert.assertEquals(1, flags.size());
		Assert.assertEquals(AttachmentFlagStatus.QE_VERIFY, flags.get(0).getStatus());
		Assert.assertEquals(AttachmentFlagState.DENIED, flags.get(0).getState());
	}
	
	@Test
	public void testGetReportHistoryBugzillaBacklog() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getHistory(Project.FIREFOX, "1239845");

		ObjectMapper mapper = new ObjectMapper();
		BugReportHistoryBugzilla history = mapper.readValue(data, BugReportHistoryBugzilla.class);
		List<BugHistory> reports = history.getBugs();

		Assert.assertEquals(1, reports.size());
		Assert.assertEquals(1239845, reports.get(0).getId().intValue());

		List<HistoryBugzilla> changes = reports.get(0).getHistory();
		Assert.assertEquals(22, changes.size());

		HistoryBugzilla change = changes.get(0);
		ChangeBugzilla details = change.getChanges().get(2);
		Assert.assertEquals("firefox-backlog+", details.getAdded());
		Assert.assertNull(details.getAttachmentId());
		
		BugActivityEvent event = BugActivityEvent.createEvent(details.getFieldName(), details.getAdded());
		Assert.assertTrue(event instanceof FlagEvent);
		List<BugzillaFlag> flags = ((FlagEvent) event).getFlags();
		Assert.assertEquals(1, flags.size());
		Assert.assertEquals(AttachmentFlagStatus.BACKLOG, flags.get(0).getStatus());
		Assert.assertEquals(AttachmentFlagState.GRANTED, flags.get(0).getState());
	}
	
	@Test
	public void testGetReportHistoryBugzillaInTestSuite() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getHistory(Project.FIREFOX, "1239671");

		ObjectMapper mapper = new ObjectMapper();
		BugReportHistoryBugzilla history = mapper.readValue(data, BugReportHistoryBugzilla.class);
		List<BugHistory> reports = history.getBugs();

		Assert.assertEquals(1, reports.size());
		Assert.assertEquals(1239671, reports.get(0).getId().intValue());

		List<HistoryBugzilla> changes = reports.get(0).getHistory();
		Assert.assertEquals(27, changes.size());

		HistoryBugzilla change = changes.get(19);
		ChangeBugzilla details = change.getChanges().get(0);
		Assert.assertEquals("in-testsuite?", details.getAdded());
		Assert.assertNull(details.getAttachmentId());
		
		BugActivityEvent event = BugActivityEvent.createEvent(details.getFieldName(), details.getAdded());
		Assert.assertTrue(event instanceof FlagEvent);
		List<BugzillaFlag> flags = ((FlagEvent) event).getFlags();
		Assert.assertEquals(1, flags.size());
		Assert.assertEquals(AttachmentFlagStatus.TEST_SUITE, flags.get(0).getStatus());
		Assert.assertEquals(AttachmentFlagState.REQUESTED, flags.get(0).getState());
	}
	
	@Test
	public void testGetReportCommentsBugzilla() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getComments(Project.FIREFOX, "569360");

		ObjectMapper mapper = new ObjectMapper();
		Report report = mapper.readValue(data, Report.class);
		List<Comment> comments = report.getComments();

		Assert.assertEquals(4, comments.size());
		Assert.assertEquals(4721961, comments.get(0).getId().intValue());

	}
	
	@Test
	public void testBugReportBugzilla() {

		List<BugReport> reports = BugzillaDataset.getData(testFirefox);

		Assert.assertEquals(2, reports.size());

		/* Validate report #1 */
		BugReport report = reports.get(0);
		Assert.assertEquals(569360, report.getId());
		Assert.assertNull(report.getDuplicateOf());
		Assert.assertEquals("mounir@lamouri.fr", report.getAssigned());
		Assert.assertEquals(StatusType.RESOLVED, report.getStatus());
		Assert.assertEquals(ResolutionType.FIXED, report.getResolution());

		BugActivity activity = report.getActivity();
		Assert.assertEquals(0, activity.getAssignmentEvents().size());
		Assert.assertEquals("mak77@bonardo.net", activity.resolution().resolvedBy());
		Assert.assertEquals(1, activity.getResolvers().size());
		Assert.assertEquals("mak77@bonardo.net", activity.whoSetStatus());
		Assert.assertEquals(1, activity.getApprovedAttachments().size());
		Assert.assertEquals(1, activity.getAttachmentSubmitters(activity.getApprovedAttachments()).size());
		Assert.assertEquals("mounir@lamouri.fr", activity.mostFrequentAttachmentSubmitter());
		Assert.assertEquals(1, activity.getFixers().size());
		Assert.assertEquals("mak77@bonardo.net", activity.getFixers().get(0));
		Assert.assertEquals(0, activity.getComponentChanges().size());
		Assert.assertEquals(3, activity.getCCAdded().size());

		/* Validate report #2 */
		report = reports.get(1);
		Assert.assertEquals(569459, report.getId());
		Assert.assertNull(report.getDuplicateOf());
		Assert.assertEquals("nobody@mozilla.org", report.getAssigned());
		Assert.assertEquals(StatusType.VERIFIED, report.getStatus());
		Assert.assertEquals(ResolutionType.FIXED, report.getResolution());

		activity = report.getActivity();
		Assert.assertEquals(0, activity.getAssignmentEvents().size());
		Assert.assertEquals("gavin.sharp@gmail.com", activity.resolution().resolvedBy());
		Assert.assertEquals(2, activity.getResolvers().size());
		Assert.assertEquals("hskupin@gmail.com", activity.whoSetStatus());
		Assert.assertEquals(0, activity.getApprovedAttachments().size());
		Assert.assertEquals(0, activity.getAttachmentSubmitters(activity.getApprovedAttachments()).size());
		Assert.assertEquals("", activity.mostFrequentAttachmentSubmitter());
		Assert.assertEquals(1, activity.getFixers().size());
		Assert.assertEquals("gavin.sharp@gmail.com", activity.getFixers().get(0));
		Assert.assertEquals(0, activity.getComponentChanges().size());
		Assert.assertEquals(7, activity.getCCAdded().size());
	}
	
	@Test
	public void testBugReportBugzillaExport() {

		List<BugReport> reports = BugzillaDataset.getData(testFirefox);
		Assert.assertEquals(2, reports.size());

		BugzillaDataset.exportReports(testFirefox, reports);
	}

	@Test
	public void testBugReportBugzillaImport() throws JsonProcessingException, IOException {

		List<BugReport> reports = BugzillaDataset.getData(testFirefox);
		Assert.assertEquals(2, reports.size());

		File file = BugzillaDataset.exportReports(testFirefox, reports);
		reports = null; // clear reference

		reports = BugzillaDataset.importReports(file);
		Assert.assertEquals(2, reports.size());
	}
}
