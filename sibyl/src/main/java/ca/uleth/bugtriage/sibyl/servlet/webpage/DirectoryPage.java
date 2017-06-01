package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.ProcessBugServlet;
import ca.uleth.bugtriage.sibyl.servlet.util.Links;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;

public class DirectoryPage extends UBCWebpage {

	private static final String PAGE_TITLE = "The " + Sibyl.NAME + " Project";

	private static final String PAGE_FOOTER = "Version: July 12, 2006";
	
	@Override
	protected String content(User user) {
		StringBuffer content = new StringBuffer();
		
		content.append("<center>");
		content.append(Webpage.heading("Welcome user #" + user.getId()
				+ ".<br>(" + user.getName() + ")"));
		content.append(Webpage.paragraph(Links.projectRepository(user
				.getProject())));
		content.append(Webpage.paragraph(Links.viewLog(user.getId(), false)));
		content.append(Webpage.paragraph(Links.submitLog(user.getId(), false)));
		content.append(Webpage.paragraph(Links.bugReport(false)));
		content.append(Webpage.paragraph(Links.changeAccount(user.getId())));
		content.append(Webpage.paragraph(Links.generateScript(user.getId())));
		content.append(Webpage.paragraph(Links.contact()));
		content.append("</center>");
		content.append(Webpage.DIVIDER);
		content.append(Webpage.paragraph(notes()));
		
		return content.toString();
	}

	private String notes() {
		StringBuffer notes = new StringBuffer();

		notes.append(Webpage.heading("Notes"));
		notes.append(Webpage.startList());
		notes.append(Webpage.listItem("Please submit your activity log weekly."));
		notes.append(Webpage.listItem("Surveys are given: "));
		notes.append(Webpage.startList());
		notes.append(Webpage.listItem("Every " + Sibyl.SURVEY_FREQUENCY
				+ " bug report changes."));
		notes.append(Webpage.listItem("When you submit your activity log."));
		notes.append(Webpage.endList());
		notes.append(Webpage.listItem("Every " + Sibyl.CONTROL_COUNT + "th recommendation is a control case."));
		notes.append(Webpage.endList());
		return notes.toString();
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
