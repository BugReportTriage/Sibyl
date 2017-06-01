package ca.uleth.bugtriage.sibyl.servlet;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Utils;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class GenerateScriptServletOld extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 9148874368493178468L;

	private static final String SCRIPT_FILE = Environment.getWebPageDir()
			+ "recommender.user.js";


	@Override
	protected String createPage() {
		String script = generateScript(this.user.getId(), this.messages);
		int splitIndex = SCRIPT_FILE.lastIndexOf("/");

		String scriptFile = SCRIPT_FILE.substring(splitIndex + 1);
		String scriptPath = this.user.getUserDataDir() + scriptFile;
		// Utils.messages.add("File: " + scriptPath);
		Utils.write2File(script, scriptPath);

		String url = Environment.getServletUrl() + "/data/" + this.user.getId() + "/"
				+ scriptFile;
		// Utils.messages.add(url);
		if (this.messages.size() != 0) {
			return "";
		}
		return Utils.redirectPage(url);
	}

	private static String generateScript(String userId, Messages msgs) {
		StringBuffer page = new StringBuffer();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					SCRIPT_FILE));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {

				if (line.contains("userId = -1")) {
					page.append("var userId = " + userId + ";\n");
				} else {
					page.append(line + "\n");
				}
			}
		} catch (FileNotFoundException e) {
			msgs.add(e.getMessage());
		} catch (IOException e) {
			msgs.add(e.getMessage());
		}

		return page.toString();

	}

	public static void main(String[] args) {
		Messages msgs = new Messages();
		System.out.println(generateScript("10", msgs));
		System.out.println(msgs);
	}
}
