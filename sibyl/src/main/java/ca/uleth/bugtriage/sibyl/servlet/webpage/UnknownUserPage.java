package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class UnknownUserPage extends UBCWebpage {

	private static final String PAGE_TITLE = Sibyl.NAME
			+ " System";

	private static final String PAGE_FOOTER = "";

	@Override
	protected String content(User user) {
		StringBuffer content = new StringBuffer();

		content.append(Webpage.paragraph(Webpage.banner("We're Sorry!"), true));
		content.append(Webpage.paragraph(Webpage
				.heading("The account information for user #" + user.getId()
						+ " could not be found.")));
		content.append(Webpage.paragraph(Webpage.heading("Do you need to "
				+ Webpage.link(Environment.getServletUrl() + "join",
						"create a new account") + "?")));

		return content.toString();
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
