package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;

public class GccDataset {

	public static void main(String[] args) {

		int year = 2005;
		int startMonth = 9;
		int endMonth = 12;
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca",
				"BugTriage", Project.GCC);
		CreateDataset extrator = new CreateDataset(user);

		for (int month = startMonth; month <= endMonth; month++) {
			extrator.run(month, year);
		}

	}
}
