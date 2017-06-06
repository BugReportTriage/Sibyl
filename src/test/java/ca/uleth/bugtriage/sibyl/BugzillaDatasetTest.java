package ca.uleth.bugtriage.sibyl;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReportsBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.ReportBugzilla;

public class BugzillaDatasetTest {

	@Test
	public void testGetDataFirefox() throws JsonParseException, JsonMappingException, IOException {
		Project testFirefox = Project.FIREFOX;
		testFirefox.startDate = "2010-06-01";
		testFirefox.endDate = "2010-06-02";

		String data = BugzillaDataset.getData(testFirefox);
		
		ObjectMapper mapper = new ObjectMapper();
		BugReportsBugzilla bugs = mapper.readValue(data, BugReportsBugzilla.class);
		List<ReportBugzilla> reports = bugs.getBugs();
		
		Assert.assertEquals(2, reports.size());		
		Assert.assertEquals(569360, reports.get(0).getId().intValue());
		Assert.assertEquals(569459, reports.get(1).getId().intValue());
	}

}
