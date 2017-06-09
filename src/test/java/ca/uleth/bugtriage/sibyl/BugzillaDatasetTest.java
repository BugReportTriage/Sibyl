package ca.uleth.bugtriage.sibyl;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

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
	public static void setup(){
		testFirefox = Project.FIREFOX;
		testFirefox.startDate = "2010-06-01";
		testFirefox.endDate = "2010-06-02";	
	}
	
	@Ignore
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
	public void testGetReportHistoryBugzilla() throws JsonParseException, JsonMappingException, IOException {

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

	@Ignore
	@Test
	public void testGetReportCommentsBugzilla() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getComments(Project.FIREFOX, "569360");

		ObjectMapper mapper = new ObjectMapper();
		Report report = mapper.readValue(data, Report.class);
		List<Comment> comments = report.getComments();

		Assert.assertEquals(4, comments.size());
		Assert.assertEquals(4721961, comments.get(0).getId().intValue());

	}
	@Ignore
	@Test
	public void testBugReportBugzilla() {
		
		List<BugReport> reports = BugzillaDataset.getData(testFirefox);
		
		Assert.assertEquals(2, reports);
	}
}
