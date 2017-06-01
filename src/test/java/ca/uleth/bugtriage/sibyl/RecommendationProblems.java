package ca.uleth.bugtriage.sibyl;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.servlet.util.Recommendations;

public class RecommendationProblems {

	public static void main(String[] args) {
		Project project = Project.FIREFOX;
		int bugId = 350612;
		
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca", "BugTriage", project);
		BugReport report = ca.uleth.bugtriage.sibyl.utils.Utils.getReport(project
				.getUrl(), user.getName(), user.getPassword(),
				bugId);
		Recommendations recommendations = new Recommendations(report,
				project);
		try {
			System.out.println(recommendations.getComponentRecommendations());
		} catch (ClassifierNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
