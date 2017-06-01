package ca.uleth.bugtriage.sibyl.servlet.webpage;

import java.text.ParseException;

import org.eclipse.mylar.internal.bugzilla.core.HtmlTag;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class LoginPage extends UBCWebpage {

	private static final String PAGE_TITLE = "The " + Sibyl.NAME + " Project";

	private static final String PAGE_FOOTER = "Version: July 12, 2006";

	private static final String SUBMIT_LABEL = "Login";

	private static final String FORM_NAME = "login";

	private static final String DIRECTORY_SERVLET = Environment.getServletUrl()
			+ "directory";

	public static final String PASSWORD_KEY = "password";

	private static final String SIBYL_FAQ_URL = "http://www.cs.ubc.ca/labs/spl/projects/bugTriage/assignment/faq.html";

	private static final String SIBYL_FAQ_LINK = "Sibyl F.A.Q.";
	

	@Override
	protected String content(User user) {
		StringBuffer page = new StringBuffer();
		page.append("<center>");
		page.append("<table><tr>");
		page.append("<td><center>" + Webpage.paragraph(existingUser()));
		page.append(Webpage.paragraph(newUser()) + "</center></td>");
		page.append("<td><img src=\"sibyl_cutout.jpg\" height=200></td>");
		page.append("</table>");
		page.append("</center>");
		page.append(Webpage.DIVIDER);
		
		page.append(Webpage.paragraph(requirements()));
		page.append(Webpage.DIVIDER);
		
		page.append(Webpage.paragraph(tutorials()));
		return page.toString();
	}

	@Override
	protected String getPageFooter() {
		return PAGE_FOOTER;
	}

	@Override
	protected String getPageTitle() {
		if (Environment.DEVELOPMENT_INSTANCE)
			return PAGE_TITLE + Webpage.paragraph("(Development Instance)");
		return PAGE_TITLE;
	}

	@Override
	protected String scripts() {
		return "\n\nfunction checkField(form){\n" + "if(form."
				+ SibylUser.USER_ID_KEY + ".value == \"\"){\n"
				+ "alert(\"Please provide a user id.\");\n" + "return false;\n"
				+ "}\n" + "return true;\n" + "}\n";
	}

	private String tutorials() {
		StringBuffer sb = new StringBuffer();

		sb.append(Webpage.heading("F.A.Q. and Flash Tutorials"));
		sb.append(Webpage.paragraph(Webpage.link(SIBYL_FAQ_URL, SIBYL_FAQ_LINK)));
		sb
				.append(Webpage
						.paragraph(Webpage
								.link(
										"http://www.cs.ubc.ca/labs/spl/projects/bugTriage/assignment/videos/setup/",
										"Setting up the Sibyl Triage Advisor system.")));
		sb
				.append(Webpage
						.paragraph(Webpage
								.link(
										"http://www.cs.ubc.ca/labs/spl/projects/bugTriage/assignment/videos/using/",
										"Using the Sibyl Triage Advisor.")));
		return sb.toString();

	}

	private String requirements() {
		StringBuffer sb = new StringBuffer();

		sb.append(Webpage.heading("Note"));
		sb.append("Using the " + Sibyl.NAME + " system requires:");
		sb.append(Webpage.startList());
		sb.append(Webpage.listItem("Installing the "
				+ Webpage.link("http://greasemonkey.mozdev.org/",
						"Grease Monkey extension")
				+ " in the Firefox web browser."));
		sb.append(Webpage.listItem("Using the "
				+ Webpage.link("http://www.mozilla.com/firefox/",
						"Firefox web browser") + " to view bug reports."));
		sb.append(Webpage.endList());
		return sb.toString();
	}

	private static String newUser() {
		return Webpage.link(Environment.getServletUrl() + "join",
				"I need an id.");
	}

	private static String existingUser() {
		StringBuffer sb = new StringBuffer();

		sb.append(Webpage.startForm(DIRECTORY_SERVLET, FORM_NAME,
				"return checkField(this)"));
		sb.append(userId());
		sb.append(Webpage.endForm(SUBMIT_LABEL, FORM_NAME));

		return sb.toString();
	}

	private static String userId() {
		StringBuffer sb = new StringBuffer();
		sb.append(Webpage.paragraph(id()));
		sb.append(Webpage.paragraph(password()));
		return sb.toString();
	}

	private static String id() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<strong>User Id: </strong>");
			HtmlTag tag = new HtmlTag("INPUT");
			tag.setAttribute("type", "text");
			tag.setAttribute("size", "3");
			tag.setAttribute("name", SibylUser.USER_ID_KEY);
			sb.append(tag.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}

	private static String password() {
		StringBuffer sb = new StringBuffer();
		try {
			sb.append("<strong>Password: </strong>");
			HtmlTag tag = new HtmlTag("INPUT");
			tag.setAttribute("type", "password");
			tag.setAttribute("size", "10");
			tag.setAttribute("name", PASSWORD_KEY);
			sb.append(tag.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
}
