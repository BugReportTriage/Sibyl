package ca.uleth.bugtriage.sibyl.servlet.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.mylar.internal.bugzilla.core.BugzillaRepositoryUtil;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.exceptions.UserNotFoundException;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class Utils {

	/**
	 * Extract the parameters from a POST request
	 * 
	 * @param request
	 *            The HTTP request
	 * @return Map of parameter values
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, String> getData(HttpServletRequest request,
			Messages messages) {
		Map<String, String> parameters = new HashMap<String, String>();

		Enumeration<String> parameterNames = request.getParameterNames();
		String parameter;
		String[] parameterValues;
		while (parameterNames.hasMoreElements()) {
			parameter = parameterNames.nextElement();
			parameterValues = request.getParameterValues(parameter);
			if (parameterValues.length == 1) {
				parameters.put(parameter, parameterValues[0]);
			} else {
				String values = parameterValues[0];
				for (int i = 1; i < parameterValues.length; i++) {
					values += "," + parameterValues[i];
				}
				parameters.put(parameter, values);
			}
		}
		return parameters;
	}

	/**
	 * Send generated page back to client
	 * 
	 * @param response
	 *            Servelet response outlet
	 * @param messages
	 *            Diagnositic messages
	 * @param page
	 *            The webpage to send
	 * @throws IOException
	 */
	public static void sendPage(HttpServletResponse response, String page,
			Messages msg) {
		// Get page from server
		PrintWriter out = null;
		try {

			response.setContentType("text/html");
			out = response.getWriter();
			out.print(msg);
			out.print(page);
		} catch (IOException e) {
			msg.add(e.getMessage()); // Not sure this makes sense
		} finally {
			if (out != null)
				out.close();
		}
	}

	/**
	 * Get a static html page from a file
	 * 
	 * @param htmlFile
	 * @param messages
	 * @return
	 */
	public static String getStaticHTMLPage(String htmlFilename,
			Messages messages) {
		StringBuffer page = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					htmlFilename));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				page.append(line);
			}
		} catch (FileNotFoundException e) {
			messages.add(e.getMessage());
		} catch (IOException e) {
			messages.add(e.getMessage());
		}

		return page.toString();
	}

	public static void write2File(String content, String fileName) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static SibylUser getUser(String query, Messages msgs)
			throws UserNotFoundException, PasswordNotFoundException {
		// this.messages.add("Getting user information");
		Pattern idPattern = Pattern.compile(SibylUser.USER_ID_KEY + "=(\\d+)");
		Matcher matcher = idPattern.matcher(query);
		// this.messages.add("query: " + query);
		if (matcher.find()) {
			String id = matcher.group(1);
			return SibylUser.getUser(id, msgs);
		}
		throw new UserNotFoundException(User.UNKNOWN_USER_ID);
	}

	public static String redirectPage(String url) {
		StringBuffer page = new StringBuffer();

		page.append("<html>\n");
		page.append("<head>\n");
		page.append("<meta http-equiv=\"REFRESH\" content=\"0; URL=" + url
				+ "\">\n");
		page.append("</head>\n");
		page.append("</html>\n");

		return page.toString();
	}

	public static String addCredentials(String url, String username,
			String password, Messages msgs) {
		try {
			return BugzillaRepositoryUtil.addCredentials(url, username,
					password);
		} catch (UnsupportedEncodingException e) {
			msgs.add(e.getMessage());
			e.printStackTrace();
		}

		// Should never be reached
		return url;
	}
}
