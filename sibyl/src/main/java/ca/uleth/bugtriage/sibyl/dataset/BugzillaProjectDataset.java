package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;

public class BugzillaProjectDataset {

	public static void main(String[] args) {

		int year = 2006;
		int startMonth = 1;
		int endMonth = 9;
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca",
				"BugTriage", Project.BUGZILLA);
		CreateDataset extrator = new CreateDataset(user);

		for (int month = startMonth; month <= endMonth; month++) {
			extrator.run(month, year);
		}

	}
}
