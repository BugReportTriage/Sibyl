package ca.uleth.bugtriage.sibyl.servlet;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.util.Utils;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.AssignmentChoiceForm;
import ca.uleth.bugtriage.sibyl.servlet.webpage.RecommenderForm;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.BugzillaPage;

public class ProcessBugServlet extends AbstractRecommenderServlet {

	/**
	 * Generated serialization id
	 */
	private static final long serialVersionUID = 6497733659380974810L;

	private static final String REQUEST_PROPERTY_CONTENT_TYPE = "Content-Type";

	private static final String REQUEST_PROPERTY_CONTENT_LENGTH = "Content-Length";

	private static final String BUGZILLA_TRY_AGAIN = "try again.";

	private static final String BUGZILLA_COLLISION = "collision";

	/*
	 * Result of stealing repository connection code in forwardChange() Not sure
	 * if used
	 */
	private String charset;

	public String createPage() {

		// this.messages.add(user.getId());
		// this.messages.add(user.getName());
		// this.messages.add(user.getPassword());

		String url = this.data.get(BugzillaPage.REPOSITORY_URL_KEY);

		String requestContent = createContentString();
		HttpURLConnection connection = this.makeConnection(url);

		if (null == connection) {
			this.messages
					.add("Problem sending change through: Connection null");
			return "";
		}

		// Add BUGLIST cookie
		String cookie = user.getBuglist();
		connection.setRequestProperty("Cookie", cookie);

		this.sendChange(connection, requestContent);
		String response = this.getResponse(connection);
		if (responseOK(response)) {
			// this.messages.add("Change made");
			Recorder.recordChange(this.user, this.data, this.messages);
			int assignmentCount = this.user.getChangeCount();
			this.user.updateChangeCount();

			if (assignmentCount % Sibyl.SURVEY_FREQUENCY == 0) {
				if (assignmentCount % (Sibyl.SURVEY_FREQUENCY * 2) == 0) {
					response = this.doRecommenderSurvey(response);
					Recorder.recordRecommenderSurveyGiven(this.user,
							this.messages);
				} else {
					response = this.doAssignmentSurvey(response);
					Recorder.recordAssignmentSurveyGiven(this.user,
							this.messages);
				}
			}

		} else {
			this.messages.add("There was a problem with Bugzilla.");
			this.messages.add("No information was logged");
		}
		return response;
	}

	private String doAssignmentSurvey(String response) {
		String survey = AssignmentChoiceForm.get(this.user.getId());
		return survey + response;
	}

	private String doRecommenderSurvey(String response) {
		String survey = RecommenderForm.get(this.user.getId());
		return survey + response;
	}
	
	private boolean responseOK(String response) {
		if (response == null) {
			return false;
		}

		if (response.contains(BUGZILLA_TRY_AGAIN)
				|| response.contains(BUGZILLA_COLLISION)) {
			return false;
		}
		return true;
	}

	/*
	 * Example: delta_ts=20060505104639&longdesclength=1
	 */
	private String createContentString() {
		String parameterValue;
		StringBuffer contentString = new StringBuffer();

		try {
			parameterValue = this.data
					.get(BugzillaPage.ASSIGNMENT_RECOMMENDATION_KEY);

			if (parameterValue != null
					&& BugzillaPage.OTHER_DEVELOPER.equals(parameterValue) == false
					&& BugzillaPage.NO_CHOICE.equals(parameterValue) == false) {
				this.data.put(BugzillaPage.ASSIGN_TO, parameterValue);
			}

			parameterValue = this.data.get(BugzillaPage.CC_RECOMMENDATION_KEY);

			if (parameterValue != null) {
				this.data.put("newcc", parameterValue);
			}

			for (String parameter : this.data.keySet()) {
				parameterValue = this.data.get(parameter);

				contentString.append(URLEncoder.encode(parameter,
						Webpage.ENCODE_FORMAT)
						+ "="
						+ URLEncoder.encode(parameterValue,
								Webpage.ENCODE_FORMAT) + "&");
			}
		} catch (UnsupportedEncodingException e) {
			this.messages.add(e.getMessage());
		}
		contentString.deleteCharAt(contentString.length() - 1);
		return contentString.toString();
	}

	private HttpURLConnection makeConnection(String url) {
		// this.messages.add("URL: " + url);
		url += "/" + BugzillaPage.PROCESS_SCRIPT_BUGZILLA + "?";

		// this.messages.add("process url:" + url);

		HttpURLConnection serverConnection = null;
		try {
			serverConnection = ca.uleth.bugtriage.sibyl.utils.Utils.connectToServer(new URL(
					url), this.messages);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverConnection;

	}

	private String getResponse(HttpURLConnection connection) {
		BufferedReader reader = null;
		BugzillaPage page = new BugzillaPage();
		try {
			int responseCode = connection.getResponseCode();
			if (responseCode != HttpURLConnection.HTTP_OK
					&& responseCode != HttpURLConnection.HTTP_CREATED) {
				this.messages.add("Server returned HTTP error: " + responseCode
						+ " - " + connection.getResponseMessage());
				return "";
			}

			// this.messages.add("Checking response");

			page.get(connection.getInputStream(), this.messages);
			page.update(this.user);
		} catch (IOException ex) {
			this.messages.add(ex.getMessage());
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					this.messages.add(e.getMessage());
				}
			}
		}
		return page.toString();
	}

	private void sendChange(HttpURLConnection connection, String content) {

		BufferedOutputStream writer = null;
		try {
			content = Utils.addCredentials(content, this.user.getName(),
					this.user.getPassword(), this.messages);

			connection.setRequestMethod(Webpage.METHOD_POST);
			String contentTypeString = Webpage.URL_ENCODING;
			if (this.charset != null) {
				contentTypeString += ";charset=" + this.charset;
			}
			connection.setRequestProperty(REQUEST_PROPERTY_CONTENT_TYPE,
					contentTypeString);
			byte[] body = content.getBytes();
			connection.setRequestProperty(REQUEST_PROPERTY_CONTENT_LENGTH,
					String.valueOf(body.length));
			connection.setDoOutput(true);

			writer = new BufferedOutputStream(connection.getOutputStream());
			writer.write(body);

		} catch (ProtocolException e) {
			this.messages.add(e.getMessage());
		} catch (IOException e) {
			this.messages.add(e.getMessage());
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					this.messages.add(e.getMessage());
				}
			}
		}
	}

	/* Used for testing */
	public static void main(String[] args) {
		ProcessBugServlet servlet = new ProcessBugServlet();
		servlet.data = new HashMap<String, String>();
		servlet.data.put("quotedText", "This is \"quoted\"");
		String content = servlet.createContentString();
		System.out.println(content);
	}
}
