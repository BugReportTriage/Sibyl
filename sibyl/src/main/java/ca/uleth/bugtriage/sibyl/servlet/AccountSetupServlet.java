package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.webpage.ConfigurationPage;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.EmailNotification;

public class AccountSetupServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 9173483777132760640L;

	

	@Override
	protected String createPage() {
		String project = this.data.get(Project.PROJECT_ID_TAG);
		this.user = new SibylUser(this.user.getId(), User.UNKNOWN_USER_NAME, User.UNKNOWN_USER_PASSWORD, Project.getProject(project));
		Recorder.recordAccount(this.user, this.data, this.messages);
		EmailNotification.send("New user - User #" + this.user.getId());
		return ConfigurationPage.getPage(this.user);
	}
}
