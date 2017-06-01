package ca.uleth.bugtriage.sibyl.servlet.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import ca.uleth.bugtriage.sibyl.servlet.webpage.AccountChangeForm;
import ca.uleth.bugtriage.sibyl.servlet.webpage.AccountSetupForm;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Encryption;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class Recorder {

	public static final DateFormat FORMATTER = new SimpleDateFormat(
			"d MMM yyyy HH:mm:ss", Locale.CANADA);

	private static final String BACKGROUND_FORM_NAME = "background";

	public static void recordPageView(SibylUser user, String url,
			PerformanceData performance, Messages msgs) {
		logEvent(user, LogTags.VIEW + " " + url, msgs);
		// System.err.println("Performance: " + performance.toString());
		logEvent(user, LogTags.PERFORMANCE + " " + performance.toString(), msgs);
	}

	public static void recordAssignmentSurveyGiven(SibylUser user, Messages msgs) {
		logEvent(user, LogTags.ASSIGNMENT_SURVEY_GIVEN, msgs);
	}

	public static void recordRecommenderSurveyGiven(SibylUser user,
			Messages msgs) {
		logEvent(user, LogTags.RECOMMENDER_SURVEY_GIVEN, msgs);
	}

	public static void recordChange(SibylUser user, Map<String, String> data,
			Messages msgs) {
		StringBuffer message = new StringBuffer();
		message.append(LogTags.CHANGE + "\n");
		for (String key : data.keySet()) {
			message.append("\t" + key + ":" + data.get(key) + "\n");
		}
		logEvent(user, message.toString(), msgs);
	}

	public static void recordConsent(SibylUser user, String name, String email,
			String date, Messages msgs) {
		Date time = new Date(System.currentTimeMillis());
		String consentFile = user.getUserDataDir() + Sibyl.CONSENT_FILE;
		user.createDataDir();
		try {
			FileWriter out = new FileWriter(consentFile, true);
			out.append("[" + user.getId() + "]\n");
			out.append("Consent given: (" + FORMATTER.format(time) + ")\n");
			out.append(name + "\n");
			out.append(email + "\n");
			out.append(date + "\n");
			out.close();
			// msgs.add("Consent Given");
		} catch (IOException e) {
			msgs.add("Error writing to " + consentFile);
		}

	}

	public static void recordAccount(SibylUser user, Map<String, String> data,
			Messages msgs) {
		System.out.println("Recording Account");
		Date time = new Date(System.currentTimeMillis());
		String accountFile = user.getUserDataDir() + Sibyl.ACCOUNT_FILE;
		user.createDataDir();
		try {
			FileWriter out = new FileWriter(accountFile, true);
			out.append("[" + user.getId() + "]\n");
			out.append("Repository Account Information ("
					+ FORMATTER.format(time) + ")\n");
			for (String key : data.keySet()) {
				if (key.equals(AccountSetupForm.FORM_NAME)
						|| key.contains(AccountSetupForm.PASSWORD_CHECK)
						|| key.contains(AccountSetupForm.SIBYL_PASSWORD_CHECK)
						|| key.equals(AccountChangeForm.FORM_NAME)) {
					continue;
				} else if (key.equals(AccountSetupForm.PASSWORD)) {
					String password = data.get(key);
					if (password.equals(""))
						continue;
					recordPassword(password, user.getPasswordFile());
				} else if (key.equals(AccountSetupForm.SIBYL_PASSWORD)) {
					String password = data.get(key);
					if (password.equals(""))
						continue;
					recordPassword(data.get(key), user.getSibylPasswordFile());
				} else {
					out.append(key + ":" + data.get(key) + "\n");
				}

			}
			out.append("-----------------------------------\n");
			out.close();
			// msgs.add("Account Information Recorded");
		} catch (IOException e) {
			msgs.add("Error writing to " + accountFile);
		}

	}

	private static void recordPassword(String password, String file) {
		try {
			byte[] encryptedPassword = Encryption.encrypt(password);
			FileOutputStream out = new FileOutputStream(file);
			out.write(encryptedPassword);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void recordBackground(SibylUser user,
			Map<String, String> data, Messages msgs) {
		Date time = new Date(System.currentTimeMillis());
		String backgroundFile = user.getUserDataDir() + Sibyl.BACKGROUND_FILE;
		user.createDataDir();
		try {
			FileWriter out = new FileWriter(backgroundFile, true);
			out.append("[" + user.getId() + "]\n");
			out.append("Background Survey (" + FORMATTER.format(time) + ")\n");
			for (String key : data.keySet()) {
				if (key.equals(BACKGROUND_FORM_NAME))
					continue;
				out.append(key + ": " + data.get(key) + "\n");
			}
			out.close();
			// msgs.add("Background Recorded");
		} catch (IOException e) {
			msgs.add("Error writing to " + backgroundFile);
		}
	}

	public static void main(String[] args) {
		DateFormat formatter = // DateFormat.getDateInstance(DateFormat.SHORT);
		new SimpleDateFormat("d MMM yyyy HH:mm:ss", Locale.CANADA);
		System.out.println(formatter
				.format(new Date(System.currentTimeMillis())));
	}

	private static void logEvent(SibylUser user, String event, Messages msgs) {
		Date time = new Date(System.currentTimeMillis());
		String logFile = user.getUserDataDir() + Sibyl.LOG_FILE;
		FileWriter out = null;
		try {
			out = new FileWriter(logFile, true);
			// msgs.add(formatter.format(time) + " " + event + "\n");
			out.append(FORMATTER.format(time) + " " + event + "\n");
		} catch (IOException e) {
			msgs.add("Error writing to " + logFile);
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					msgs.add(e.getMessage());
				}
			}
		}

	}

	public static void recordAssignmentSurvey(SibylUser user,
			Map<String, String> data, Messages msgs) {
		recordSurvey(LogTags.ASSIGNMENT_SURVEY_SUBMITTED, "assignmentSurvey", user, data, msgs);
	}

	public static void recordRecommenderSurvey(SibylUser user,
			Map<String, String> data, Messages msgs) {
		recordSurvey(LogTags.RECOMMENDER_SURVEY_SUBMITTED, "recommenderSurvey", user, data, msgs);
	}

	private static void recordSurvey(String logMsg, String surveyKey, SibylUser user, Map<String, String> data, Messages msgs) {
		StringBuffer message = new StringBuffer();
		message.append(logMsg + "\n");
		for (String key : data.keySet()) {
			if (data.get(key) == "" || key.equals(surveyKey))
				continue;
			message.append("\t" + key + ":" + data.get(key) + "\n");
		}
		logEvent(user, message.toString(), msgs);
	}

	
	
}
