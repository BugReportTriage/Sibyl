package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;

public class InvalidPasswordPage extends UBCWebpage {

	private static final String PAGE_TITLE = "Login Trouble";
	private static final String PAGE_FOOTER = "";

	@Override
	protected String content(User user) {
		StringBuffer page = new StringBuffer();
		page.append(Webpage.paragraph(Webpage.heading("Sorry, user id and password don't match."), true));
		return page.toString();
	}

	@Override
	protected String getPageFooter() {
		return PAGE_FOOTER;
	}

	@Override
	protected String getPageTitle() {
		return PAGE_TITLE;
	}

	@Override
	protected String scripts() {
		return "";
	}

}
