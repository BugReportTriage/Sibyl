package ca.uleth.bugtriage.sibyl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Set;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReportsBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.ReportBugzilla;
import junit.framework.TestCase;

public class BugReportJsonTest extends TestCase {

	private static final String TEST_BUGZILLA_BUG_FILENAME = "data/testBug.json";
	private ReportBugzilla report = null;	

	@Test
	public void testBugzillaJSON() throws JsonParseException, JsonMappingException, IOException {

		ObjectMapper mapper = new ObjectMapper();
		BugReportsBugzilla bugs = mapper.readValue(new File(TEST_BUGZILLA_BUG_FILENAME), BugReportsBugzilla.class);

		assertEquals(bugs.getBugs().size(), 1);

		report = bugs.getBugs().get(0);

		assertEquals("ttromey@mozilla.com", report.getAssigned_to());
		assertEquals("Developer Tools: Framework", report.getComponent());
		assertEquals("2017-05-15T09:28:10Z", report.getCreation_time());
		assertEquals("ttromey@mozilla.com", report.getCreator());
		assertEquals("2017-05-17T10:22:28Z", report.getLast_change_time());
		assertEquals("Unspecified", report.getOp_sys());
		assertEquals("Unspecified", report.getPlatform());
		assertEquals("P1", report.getPriority());
		assertEquals("FIXED", report.getResolution());
		assertEquals("normal", report.getSeverity());
		assertEquals("RESOLVED", report.getStatus());
		assertEquals("devtools-source-map: update to 0.5.0", report.getSummary());
	}
}
