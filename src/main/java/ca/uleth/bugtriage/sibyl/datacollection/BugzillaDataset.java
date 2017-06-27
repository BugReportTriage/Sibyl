package ca.uleth.bugtriage.sibyl.datacollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugReportsBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.BugzillaAdapter;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.ReportBugzilla;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Comment;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment.Report;
import ca.uleth.bugtriage.sibyl.report.bugzilla.json.history.BugReportHistoryBugzilla;

public class BugzillaDataset extends Dataset {

	private static final int BATCH_SIZE = 50;

	private static final String CLOSED_FIXED = "&resolution=FIXED&status=RESOLVED&status=VERIFIED&status=CLOSED";

	private static final String DATA_FIELDS = "id," + "cc," + "component," + "summary," + "creator," + "assigned_to,"
			+ "resolution," + "creation_time," + "op_sys," + "platform," + "last_change_time," + "priority,"
			+ "dupe_of," + "severity," + "status";

	public BugzillaDataset(Project p) {
		super(p);
	}

	/**
	 * Example:
	 * https://bugzilla.mozilla.org//rest/bug?product=Firefox&resolution
	 * =FIXED&status=RESOLVED&status=VERIFIED&status=CLOSED&
	 * include_fields=id&quicksearch=creation_ts%3E=2010-06-01%
	 * 20creation_ts%3C2010-06-02
	 */
	@Override
	public String getReports() {

		String url = project.url + "/rest/bug?product=" + project.product + CLOSED_FIXED + "&include_fields="
				+ DATA_FIELDS + "&quicksearch=creation_ts%3E=" + project.startDate + "%20creation_ts%3C"
				+ project.endDate;
		return getJsonData(url);
	}

	/**
	 * Get the history of a bug report
	 * 
	 * @param project
	 *            The project to access.
	 * @param reportId
	 *            The id of the report.
	 * @return Activity history of the report in JSON format.
	 */
	public String getHistory(String reportId) {

		/* GET /rest/bug/(id)/history */
		String url = project.url + "/rest/bug/" + reportId + "/history";
		return getJsonData(url);
	}

	/**
	 * Get the comments of a bug report
	 * 
	 * @param project
	 *            The project to access.
	 * @param reportId
	 *            The id of the report.
	 * @return Comments of the report in JSON format.
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public String getComments(String reportId) throws JsonParseException, JsonMappingException, IOException {

		/* GET /rest/bug/(id)/comment */
		String url = project.url + "/rest/bug/" + reportId + "/comment";

		String data = getJsonData(url);

		ObjectMapper mapper = new ObjectMapper();
		Map<String, JsonNode> commentsBugzilla = mapper.readValue(data, new TypeReference<Map<String, JsonNode>>() {
		});
		/* Should only be 1 item */
		JsonNode bugsChild = new ArrayList<JsonNode>(commentsBugzilla.values()).get(0);

		JsonNode commentsJson = bugsChild.iterator().next();
		return commentsJson.toString();
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

	@Override
	public Set<BugReport> getData() {
		return getData(0);
	}

	public Set<BugReport> getData(int startingReportNum) {
		ObjectMapper mapper = new ObjectMapper();

		try {
			String reportsJson = this.getReports();
			BugReportsBugzilla bugs = mapper.readValue(reportsJson, BugReportsBugzilla.class);
			List<ReportBugzilla> reportsBugzilla = bugs.getBugs();

			for (int reportNum = startingReportNum; reportNum < reportsBugzilla.size(); reportNum++) {
				ReportBugzilla r = reportsBugzilla.get(reportNum);
				System.err.println("Getting report " + reportNum + " / " + reportsBugzilla.size());
				BugReport report = BugzillaAdapter.convertReport(r);

				// Add bug report history
				try {
					String historyJson = this.getHistory(r.getId().toString());
					BugReportHistoryBugzilla historyBugzilla = mapper.readValue(historyJson,
							BugReportHistoryBugzilla.class);
					BugActivity history = BugzillaAdapter.convertHistory(historyBugzilla);
					report.setActivity(history);
				} catch (JsonMappingException e) {
					System.err.println("Problem getting comments from report #" + r.getId());
					System.err.println(e.getMessage());
					continue;
				}

				try {
					// Add bug report comments
					String commentsJson = this.getComments(r.getId().toString());
					Report commentsBugzillaReport = mapper.readValue(commentsJson, Report.class);
					List<Comment> commentsBugzilla = commentsBugzillaReport.getComments();

					List<ca.uleth.bugtriage.sibyl.report.Comment> comments = BugzillaAdapter
							.convertComments(commentsBugzilla);
					report.setComments(comments);
					report.setDescription(comments.get(0).getText());
				} catch (JsonMappingException e) {
					System.err.println("Problem getting comments from report #" + r.getId());
					System.err.println(e.getMessage());
					continue;
				}
				reports.add(report);

				if (reportNum % BATCH_SIZE == 0) {
					saveCheckpoint(reportNum);
					reports.clear();
				}

				// Pause to not annoy
				Thread.sleep(5000);
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return reports;
	}

	private void saveCheckpoint(int count) {
		try {
			File dataFile = new File(getProject().getDatafile().getAbsolutePath() + "." + count / BATCH_SIZE);
			File parent = new File(dataFile.getParent());
			if (parent.exists() == false) {
				parent.mkdir();
			}

			System.out.println("Writing to: " + dataFile.getName());

			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, reports);

			System.out.println("Bugs written out: " + dataFile.getName());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
