package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.servlet.ViewBugServlet;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class RedirectPage {

	private static final String MESSAGE = "Please wait while the recommendations are retrieved.";

	public static String get(String query) {
		StringBuffer sb = new StringBuffer();
		String meta = "<meta http-equiv=\"REFRESH\" content=\"0; URL=" + Environment.getServletUrl() + ViewBugServlet.SERVLET_NAME + "?" + query + "\">";
		sb.append(Webpage.startPage("Redirecting to Sibyl Server", meta, ""));
		sb.append(MESSAGE);
		//sb.append(Environment.getServletUrl() + "viewBug?" + query);
		sb.append(Webpage.endPage(""));

		return sb.toString();
	}
}
