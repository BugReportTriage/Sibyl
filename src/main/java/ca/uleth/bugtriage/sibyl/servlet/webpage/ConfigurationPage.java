package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Links;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class ConfigurationPage {

	private static final String PAGE_TITLE = "Environment Configuration for Using " + Sibyl.NAME;

	private static final String PAGE_FOOTER = "";

	public static String getPage(User user) {
		StringBuffer page = new StringBuffer();
		page.append(Webpage.startPage(PAGE_TITLE) + "\n");
		page.append(studyId(user.getId()));
		page.append(Webpage.startList());
		page.append(Webpage.listItem(getRedirectScriptText(user.getId())));
		page.append(Webpage.listItem(getTestLinkText(user.getRepository())));
		page.append(Webpage.endList());
		page.append(Webpage.endPage(PAGE_FOOTER) + "\n");

		return page.toString();
	}

	private static String getTestLinkText(String repository){
		StringBuffer sb = new StringBuffer();
		sb.append("Test your setup by viewing a bug report from ");
		sb.append("<a target=\"_blank\" href=\"" + repository + "\">your project bug repository</a>.<br>");
		sb.append(Webpage.startList());
		sb.append(Webpage.listItem("Clicking on the above link will open a new window for the project bug repository."));
		sb.append(Webpage.listItem("Choose any submitted bug report to view."));
		sb.append(Webpage.listItem("The report page will be redirected."));
		sb.append(Webpage.listItem("The new report page url will start with <strong>" + Environment.getServletUrl() + "</strong>"));
		sb.append(Webpage.endList());
		return sb.toString();
	}
	
	private static String getRedirectScriptText(String userId) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("Install the " + Links.generateScript(userId, "bug report redirection script.", false));
		sb.append(Webpage.startList());
		sb.append(Webpage.listItem("Make sure that the Grease Monkey extension is enabled."));
		sb.append(Webpage.listItem("Clicking on the above link will open a new window containing the script."));
		sb.append(Webpage.listItem("Click on the \"Install\" button in the top right corner."));
		sb.append(Webpage.listItem("A message will be displayed stating that the script is now installed."));
		sb.append(Webpage.listItem("Close the window containing the script."));
		sb.append(Webpage.endList());
		return  sb.toString();

	}

	private static String studyId(String studyId) {
		return Webpage.heading("Your login/user id for the study is: " + studyId + ".");
	}
}
