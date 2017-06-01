package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UBCWebpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class MakeAccountChangeServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 6102660898908916861L;

	private static final int PAGE_DELAY = 5;

	private static final String PAGE_TITLE = Sibyl.NAME + " Account Updated";

	private static final String PAGE_FOOTER = "";

	private static final String ACCOUNT_CHANGED_MSG = Webpage.heading("Your " + Sibyl.NAME
			+ " account has been changed.");

	private static final String REDIRECTION_MSG = Webpage.message("(Redirecting back to the directory page in "
			+ PAGE_DELAY + " seconds)");

	protected String content() {
		return Webpage.paragraph(ACCOUNT_CHANGED_MSG, true)
				+ Webpage.paragraph(REDIRECTION_MSG, true);
	}

	protected String getPageFooter() {
		return PAGE_FOOTER;
	}

	protected String getPageTitle() {
		return PAGE_TITLE;
	}

	protected String scripts() {
		return "";
	}

	@Override
	protected String createPage() {
		Recorder.recordAccount(this.user, this.data, this.messages);
		StringBuffer page = new StringBuffer();

		page.append(Webpage.startPage(this.getPageTitle(), redirect(this.user
				.getId()), scripts()));
		page.append(content());
		page.append(Webpage.endPage(this.getPageFooter()));

		return page.toString();
	}

	private String redirect(String userId) {
		return "<meta http-equiv=\"REFRESH\" content=\"" + PAGE_DELAY
				+ "; URL=" + Environment.getServletUrl() + "directory" + "?"
				+ SibylUser.USER_ID_KEY + "=" + userId + "\">";
	}
}
