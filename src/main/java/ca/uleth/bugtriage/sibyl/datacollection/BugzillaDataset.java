package ca.uleth.bugtriage.sibyl.datacollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReportsBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.ReportBugzilla;

public class BugzillaDataset {

		private static final String CLOSED_FIXED = "&resolution=FIXED&status=RESOLVED&status=VERIFIED&status=CLOSED";

	private static final String DATA_FIELDS = "id," + "cc," + "component," + "summary," + "creator," + "assigned_to,"
			+ "resolution," + "creation_time," + "op_sys," + "platform," + "last_change_time," + "priority,"
			+ "dupe_of," + "severity," + "status";

	/**
	 * Get bug report data for a project.
	 * 
	 * @param project
	 *            The project to get the reports from.
	 * @return The report data in JSON format.
	 * 
	 *         Example:
	 *         https://bugzilla.mozilla.org//rest/bug?product=Firefox&resolution
	 *         =FIXED&status=RESOLVED&status=VERIFIED&status=CLOSED&
	 *         include_fields=id&quicksearch=creation_ts%3E=2010-06-01%
	 *         20creation_ts%3C2010-06-02
	 */

	public static String getReports(Project project) {

		String url = project.url + "/rest/bug?product=" + project.product + CLOSED_FIXED + "&include_fields=" + DATA_FIELDS + "&quicksearch=creation_ts%3E=" + project.startDate
				+ "%20creation_ts%3C" + project.endDate;
		return getJsonData(url);
	}

	/**
	 * Get the history of a bug report
	 * @param project The project to access.
	 * @param reportId The id of the report.
	 * @return Activity history of the report in JSON format.
	 */
	public static String getHistory(Project project, String reportId) {

		/* GET /rest/bug/(id)/history */
		String url = project.url + "/rest/bug/" + reportId + "/history";
		return getJsonData(url);
	}

	/**
	 * Get the comments of a bug report
	 * @param project The project to access.
	 * @param reportId The id of the report.
	 * @return Comments of the report in JSON format.
	 */
	public static String getComments(Project project, String reportId) {

		/* GET /rest/bug/(id)/comment */
		String url = project.url + "/rest/bug/" + reportId + "/comment";
		return getJsonData(url);
	}

	
	private static String getJsonData(String url) {
		InputStream is = null;
		try {
			try {
				URLConnection c = new URL(url).openConnection();
				c.addRequestProperty("Accept", "application/json");
				c.addRequestProperty("Content-Type", "application/json");

				c.connect();
				is = c.getInputStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();
				int cp;
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
				return sb.toString();

			} finally {
				if (is != null)
					is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void writeToFile(Project project, String data) {
		try {

			File dataFile = new File(project.dataDir + "/" + project.name + "_" + project.startDate + ".json");
			File parent = new File(dataFile.getParent());
			if (parent.exists() == false) {
				parent.mkdir();
			}

			FileWriter out = new FileWriter(dataFile);
			System.out.println("Writing to: " + dataFile.getName());
			out.write(data);
			out.close();
			System.out.println("Bugs written out: " + dataFile.getName());
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
