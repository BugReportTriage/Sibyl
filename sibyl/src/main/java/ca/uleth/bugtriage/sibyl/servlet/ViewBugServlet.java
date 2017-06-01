package ca.uleth.bugtriage.sibyl.servlet;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.exceptions.UserNotFoundException;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.servlet.util.PerformanceData;
import ca.uleth.bugtriage.sibyl.servlet.util.Recommendations;
import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.util.Utils;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.BugzillaPage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class ViewBugServlet extends AbstractRecommenderServlet {

	/**
	 * Id for serialization
	 */
	private static final long serialVersionUID = 4861766485966485932L;

	public static final String SERVLET_NAME = "viewBug";

	private PerformanceData performance;

	protected String createPage() {

		this.performance = new PerformanceData();
		String page = modifyPage(this.url);
		Recorder
				.recordPageView(this.user, this.url, this.performance, this.messages);
		return page;
	}

	private String modifyPage(String url) {

		// in case of offline testing
		if (this.performance == null) {
			this.performance = new PerformanceData();
		}

		// System.out.println("Getting the page.");

		List<Classification> developerRecommendations = new ArrayList<Classification>();
		List<Classification> componentRecommendations = new ArrayList<Classification>();
		List<Classification> subcomponentRecommendations = new ArrayList<Classification>();
		List<Classification> ccRecommendations = new ArrayList<Classification>();

		// this.messages.add("user: " + this.user.getName());
		// this.messages.add("password: " + this.user.getPassword());
		String credentialUrl = Utils.addCredentials(url, this.user.getName(),
				this.user.getPassword(), this.messages);

		java.util.regex.Pattern idPattern = java.util.regex.Pattern
				.compile("\\?id=(\\d+)");
		java.util.regex.Matcher matcher = idPattern.matcher(credentialUrl);
		String bugId = "";
		if (matcher.find()) {
			bugId = matcher.group(1);
		}

		String decodedQuery = this.query;
		try {
			decodedQuery = URLDecoder.decode(this.query, Webpage.ENCODE_FORMAT);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Pattern componentPattern = Pattern.compile("&"
				+ ca.uleth.bugtriage.sibyl.utils.BugzillaPage.COMPONENT_KEY
				// + "=((\\w+\\s*[\\/-]*\\s*)*)&http");
				+ "=(.*)&http");
		matcher = componentPattern.matcher(decodedQuery);
		String component = null;
		if (matcher.find())
			component = matcher.group(1);
		// this.messages.add("Component: " + component);

		/* Recommendations */
		long start, end;
		double elapsed;
		Project project = this.user.getProject();
		int changeCount = this.user.getChangeCount();
		if (changeCount % Sibyl.CONTROL_COUNT == 0) {
			developerRecommendations.add(Sibyl.CONTROL_CLASSIFICATION);
			componentRecommendations.add(Sibyl.CONTROL_CLASSIFICATION);
			ccRecommendations.add(Sibyl.CONTROL_CLASSIFICATION);
		} else {

			start = System.currentTimeMillis();
			BugReport report = ca.uleth.bugtriage.sibyl.utils.Utils.getReport(project
					.getUrl(), this.user.getName(), this.user.getPassword(),
					Integer.parseInt(bugId));
			end = System.currentTimeMillis();
			elapsed = (end - start) / 1000.0;
			this.performance.setReportTime(elapsed);
			// this.messages.add("Get page: " + elapsed + " sec.");

			if (report != null) {
				start = System.currentTimeMillis();
				Recommendations recommendations = new Recommendations(report,
						project);
				try {
					developerRecommendations = recommendations
							.getDeveloperRecommendations(component);
				} catch (ClassifierNotFoundException e) {
					this.messages.add(e.getMessage());
				}
				try {
					componentRecommendations = recommendations
							.getComponentRecommendations();
				} catch (ClassifierNotFoundException e) {
					this.messages.add(e.getMessage());
				}
				try {
					subcomponentRecommendations = recommendations
							.getSubcomponentRecommendations();
				} catch (ClassifierNotFoundException e) {
					// this.messages.add(e.getMessage());
				}
				try {
					ccRecommendations = recommendations
							.getCCRecommendations();
				} catch (ClassifierNotFoundException e) {
					// this.messages.add(e.getMessage());
				}

				end = System.currentTimeMillis();
				elapsed = (end - start) / 1000.0;
				// this.messages.add("Get recommendations: " + elapsed + "
				// sec.");
				this.performance.setRecommendationsTime(elapsed);
			} else {
				developerRecommendations.add(Sibyl.CLASSIFICATION_UNAVAILABLE);
				componentRecommendations.add(Sibyl.CLASSIFICATION_UNAVAILABLE);
				subcomponentRecommendations
						.add(Sibyl.CLASSIFICATION_UNAVAILABLE);
				this.messages
						.add("Sibyl was unable to get the information to make the recommendations.");
				this.messages
						.add("Please check the username and password given to Sibyl for accessing the repository are correct.");
			}
		}

		BugzillaPage page = new BugzillaPage();
		try {
			start = System.currentTimeMillis();

			URL bugReportURL;
			// if (Environment.DEVELOPMENT_INSTANCE && project ==
			// Project.PLATFORM)
			// bugReportURL = new URL(url); // <-- Exploit of Eclipse
			// else
			bugReportURL = new URL(credentialUrl);
			System.out.println(credentialUrl);

			// Bugzilla bug to show reassignment field
			HttpURLConnection connection = ca.uleth.bugtriage.sibyl.utils.Utils
					.connectToServer(bugReportURL, this.messages);

			// Add BUGLIST cookie
			String cookie = user.getBuglist();
			connection.setRequestProperty("Cookie", cookie);

			InputStream stream = connection.getInputStream();

			page.get(stream, this.messages);

			if (false){
				System.out.println(page);
				return "";
			}

			page.update(this.user, component, developerRecommendations,
					componentRecommendations, subcomponentRecommendations,
					ccRecommendations, this.performance);

			System.out.println(page);
			end = System.currentTimeMillis();
			elapsed = (end - start) / 1000.0;
			this.performance.setBuildPageTime(elapsed);
			// this.messages.add("Rebuild page: " + elapsed + " sec.");
		} catch (MalformedURLException e) {
			this.messages.add("Sorry, the URL " + url + " is malformed");
		} catch (FileNotFoundException e) {
			this.messages.add("Sorry, the URL " + url
					+ " could not be accessed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return page.toString();
	}

	

	public static void main(String[] args) {
		ViewBugServlet servlet = new ViewBugServlet();
		Messages msgs = new Messages();

		try {
			if (Environment.DEVELOPMENT_INSTANCE == false)
				throw new RuntimeException(
						"Not using the development instance.");
			servlet.user = SibylUser.getUser("1", msgs);
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// servlet.query =
		// "https://stavanger.cs.ubc.ca:8443/sibyl/viewBug?userId=4&component=Developer%20Assignment&http://ws.cs.ubc.ca/~janvik/bugs/show_bug.cgi?id=22";
		servlet.query = "https://stavanger.cs.ubc.ca:8443/sibyl/viewBug?userId=4&http://ws.cs.ubc.ca/~janvik/bugs/show_bug.cgi?id=20";
		System.out.println(servlet.createPage());

	}
}
