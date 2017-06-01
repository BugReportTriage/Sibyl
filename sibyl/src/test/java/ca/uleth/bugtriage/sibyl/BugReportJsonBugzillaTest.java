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

import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReports;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.Report;
import junit.framework.TestCase;

public class BugReportJsonBugzillaTest extends TestCase {

	private static final String TEST_BUG_FILENAME = "data/testBug.json";
	private Report report = null;

	@Override
	protected void setUp() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		BugReports bugs = mapper.readValue(new File(TEST_BUG_FILENAME), BugReports.class);

		assertEquals(bugs.getBugs().size(), 1);
		report = bugs.getBugs().get(0);
	}

	@Test
	public void testAttributes(){
		assertEquals("odvarko@gmail.com", report.getAssigned_to());
	}
}
