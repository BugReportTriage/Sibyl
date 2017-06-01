package ca.uleth.bugtriage.sibyl.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Links;
import ca.uleth.bugtriage.sibyl.servlet.util.Utils;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class ViewLogServlet extends AbstractRecommenderServlet {
	/**
	 * Generated id
	 */
	private static final long serialVersionUID = -8056638363413488729L;

	private static final String LOG_TITLE = "Activity log from using Sibyl";

	private static final String LOG_NOT_FOUND = "It appears that a log of your triage activity has yet to be created.<br>"
			+ "Perhaps you have not triaged any reports since you last submitted a log.";



	@Override
	protected String createPage() {
		File logFile = new File(this.user.getUserDataDir() + Sibyl.LOG_FILE);
		BufferedReader reader = null;
		StringBuffer log = new StringBuffer();
		try {
			log.append(Webpage.startPage(LOG_TITLE));
			if (logFile.exists()) {
				log.append(Links.submitLog(this.user.getId(), true));

				log.append("<pre>");
				reader = new BufferedReader(new FileReader(logFile));
				for (String line = reader.readLine(); line != null; line = reader
						.readLine()) {
					log.append(line + "\n");
				}
				log.append("</pre>");
			} else {
				log.append(Webpage.message(LOG_NOT_FOUND));
			}

			log.append(Webpage.endPage(""));
		} catch (IOException e) {
			this.messages.add("Error reading " + logFile + "\n" + e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					this.messages.add(e.getMessage());
				}
			}
		}
		return log.toString();
	}
}
