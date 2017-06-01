package ca.uleth.bugtriage.sibyl;


import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class TestTriageReport {

	private static BugReport report;
	
	@BeforeClass
	public static void init() throws Exception {
		int bugId = 120153;
		Project project = Project.PLATFORM;
		User user = new User(User.UNKNOWN_USER_ID, Login.USER, Login.PASSWORD,
				project);
		report = Utils.getReport(project.getUrl(), user
				.getName(), user.getPassword(), bugId);
	}

	/**
	 * Tests
	 */
	@Test
	public void testDupId(){
		Assert.assertEquals(163119, report.getDupId());
	}
}
