package ca.uleth.bugtriage.sibyl;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class XMLError {

	public static void main(String[] args) {
		int bugId = 329008;
		User john = new User("-1", "janvik@cs.ubc.ca", "BugTriage",
				Project.FIREFOX);
		Utils.getReport(john.getRepository(), john.getName(), john.getPassword(), bugId);
	}
}
