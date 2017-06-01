package ca.uleth.bugtriage.sibyl.servlet.util;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class Links {

	private static final String VIEW_LOG_SERVLET = Environment.getServletUrl()
			+ "viewLog?";

	private static final String VIEW_LOG_LINK = "View Activity Log";

	private static final String SUBMIT_LOG_LINK = "Submit Activity Log";

	private static final String SIBYL_LOGIN_LINK = "Sibyl Login";

	private static final String LOG_ICON = "log-icon.gif";

	private static final String SIBYL_BUG_SERVER = "http://ws.cs.ubc.ca/~janvik/bugs/index.cgi";

	private static final String SIBYL_LOGIN = Environment.getServletUrl() + "login";

	private static final String REPORT_BUG_LINK = "Report "
			+ ca.uleth.bugtriage.sibyl.sibyl.Sibyl.NAME + " Bug";

	private static final String REPORT_BUG_ICON = "bug-icon.png";

	private static final String SUBMIT_LOG_URL = Environment.getServletUrl()
			+ "submitLog?";

	private static final String SIBYL_ICON = "sibyl_cutout.jpg";

	public static String viewLog(String userId, boolean useIcon) {
		if (useIcon) {
			return Webpage.link(VIEW_LOG_SERVLET + SibylUser.USER_ID_KEY + "="
					+ userId, VIEW_LOG_LINK + " ", LOG_ICON, true);
		}
		return Webpage.link(VIEW_LOG_SERVLET + SibylUser.USER_ID_KEY + "="
				+ userId, VIEW_LOG_LINK + " ", null, true);

	}

	public static String bugReport(boolean useIcon) {
		if (useIcon) {
			return Webpage.link(SIBYL_BUG_SERVER, REPORT_BUG_LINK + " ",
					REPORT_BUG_ICON, true);
		}

		return Webpage
				.link(SIBYL_BUG_SERVER, REPORT_BUG_LINK + " ", null, true);
	}

	public static String sibylLogin(boolean useIcon) {
		if (useIcon) {
			return Webpage.link(SIBYL_LOGIN, SIBYL_LOGIN_LINK + " ",
					SIBYL_ICON, true);
		}

		return Webpage.link(SIBYL_LOGIN, SIBYL_LOGIN_LINK + " ", null, true);
	}

	public static String generateScript(String userId) {
		return generateScript(userId, "Redirection for Recommendations Script",
				true);
	}

	public static String generateScript(String userId, String text, boolean bold) {
		return Webpage.link(Environment.getServletUrl() + "/redirectScript?"
				+ SibylUser.USER_ID_KEY + "=" + userId, text, null, bold);
	}

	public static String submitLog(String userId, boolean useIcon) {
		if (useIcon) {
			return Webpage.link(SUBMIT_LOG_URL + SibylUser.USER_ID_KEY + "="
					+ userId, SUBMIT_LOG_LINK, LOG_ICON, true);
		}
		return Webpage.link(SUBMIT_LOG_URL + SibylUser.USER_ID_KEY + "="
				+ userId, SUBMIT_LOG_LINK);
	}

	public static String projectRepository(Project project) {
		return Webpage.link(project.getUrl(), project.getProduct()
				+ " Repository");
	}

	public static String contact() {
		return Webpage.link("mailto:janvik@cs.ubc.ca", "Contact "
				+ ca.uleth.bugtriage.sibyl.sibyl.Sibyl.NAME + " Project");
	}

	public static String changeAccount(String userId) {
		return Webpage.link(Environment.getServletUrl() + "/accountChange?"
				+ SibylUser.USER_ID_KEY + "=" + userId,
				"Change Account Information");
	}
}
