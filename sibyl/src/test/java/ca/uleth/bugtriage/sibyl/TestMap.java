package ca.uleth.bugtriage.sibyl;

import java.util.*;

import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class TestMap {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		int bugId = 114001;
		Set<ca.uleth.bugtriage.sibyl.report.BugReport> set = new HashSet<ca.uleth.bugtriage.sibyl.report.BugReport>();
		ca.uleth.bugtriage.sibyl.report.BugReport instance1 = Utils.getReport(Project.PLATFORM.getUrl(), Login.USER, Login.PASSWORD, bugId);
		ca.uleth.bugtriage.sibyl.report.BugReport instance2 = Utils.getReport(Project.PLATFORM.getUrl(), Login.USER, Login.PASSWORD, bugId);
		set.add(instance1);
		set.add(instance2);

	}

}
