package ca.uleth.bugtriage.sibyl.dataset;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;

public class EvolutionDataset {

	public static void main(String[] args) {

		int year = 2005;
		int startMonth = 10;
		int endMonth = 12;
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca",
				"BugTriage", Project.EVOLUTION);
		CreateDataset extrator = new CreateDataset(user);

		// extrator.run(0, 0);

		for (int month = startMonth; month <= endMonth; month++) {
			extrator.run(month, year);
		}

	}
}
