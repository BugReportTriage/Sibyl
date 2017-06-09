package ca.uleth.bugtriage.sibyl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReportsBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.ReportBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.BugComment;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Bugs;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Comment;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Report;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugHistory;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugReportHistoryBugzilla;

public class BugzillaDatasetTest {

	@Ignore
	@Test
	public void testGetDataBugzilla() throws JsonParseException, JsonMappingException, IOException {

		Project testFirefox = Project.FIREFOX;
		testFirefox.startDate = "2010-06-01";
		testFirefox.endDate = "2010-06-02";

		String data = BugzillaDataset.getReports(testFirefox);

		ObjectMapper mapper = new ObjectMapper();
		BugReportsBugzilla bugs = mapper.readValue(data, BugReportsBugzilla.class);
		List<ReportBugzilla> reports = bugs.getBugs();

		Assert.assertEquals(2, reports.size());
		Assert.assertEquals(569360, reports.get(0).getId().intValue());
		Assert.assertEquals(569459, reports.get(1).getId().intValue());
	}

	@Ignore
	@Test
	public void testGetReportHistoryBugzilla() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getHistory(Project.FIREFOX, "569360");

		ObjectMapper mapper = new ObjectMapper();
		BugReportHistoryBugzilla history = mapper.readValue(data, BugReportHistoryBugzilla.class);
		List<BugHistory> reports = history.getBugs();

		Assert.assertEquals(1, reports.size());
		Assert.assertEquals(569360, reports.get(0).getId().intValue());
		Assert.assertEquals(8, reports.get(0).getHistory().size());
	}

	@Test
	public void testGetReportCommentsBugzilla() throws JsonParseException, JsonMappingException, IOException {

		String data = BugzillaDataset.getComments(Project.FIREFOX, "569360");

		ObjectMapper mapper = new ObjectMapper(); 
		Map<String, JsonNode> bugs = mapper.readValue(data, new TypeReference<Map<String, JsonNode>>() {
		});

		bugs.remove("comments");
		JsonNode bugsChild = new ArrayList<JsonNode>(bugs.values()).get(0);
		JsonNode commentsJson = bugsChild.iterator().next(); // Should only be 1 item
		
		Report report = mapper.readValue(commentsJson, Report.class);
		List<Comment> comments = report.getComments();
		Assert.assertEquals(4, comments.size());
		Assert.assertEquals(4721961, comments.get(0).getId().intValue());

	}
}
