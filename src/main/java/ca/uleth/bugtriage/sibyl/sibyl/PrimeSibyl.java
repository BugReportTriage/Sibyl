package ca.uleth.bugtriage.sibyl.sibyl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.servlet.ViewBugServlet;
import ca.uleth.bugtriage.sibyl.utils.Environment;

/*
 * This 'script' requests a page from Sibyl to prime the system
 * 
 */
public class PrimeSibyl {

	public static void main(String[] args) {
		/* Since the only one using Sibyl is Eclipse, we just prime that one */

		if (Environment.DEVELOPMENT_INSTANCE) {
			throw new RuntimeException(
					"Using development instance: Configured for production");
		}

		int reportId = 162005;
		int userId = 6;
		Project project = Project.PLATFORM;
		String query = Environment.getServletUrl()
				+ ViewBugServlet.SERVLET_NAME + "?" + SibylUser.USER_ID_KEY
				+ "=" + userId + "&" + project.getUrl() + "show_bug.cgi?id="
				+ reportId;

		// servlet.query =
		// "https://stavanger.cs.ubc.ca:8443/sibyl/viewBug?userId=4&http://ws.cs.ubc.ca/~janvik/bugs/show_bug.cgi?id=20";
		System.out.println(query);
		long start = System.currentTimeMillis();
		try {
			URL url = new URL(query);
			HttpURLConnection connection = ca.uleth.bugtriage.sibyl.utils.Utils
					.connectToServer(url, null);
			connection.connect();
			 connection.getContent();
			/*
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				System.out.println(line);
			}
			*/
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long end = System.currentTimeMillis();
		long seconds = (end - start) / 1000;
		System.out.println("Time to prime: " + seconds);
	}
}
