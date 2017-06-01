package ca.uleth.bugtriage.sibyl.servlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.webpage.RecommenderForm;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.EmailNotification;

public class SubmitLogServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 8855813112178595940L;

	@Override
	protected String createPage() {
		Recorder.recordRecommenderSurveyGiven(this.user, this.messages);

		this.submitLog();
		return RecommenderForm.get(this.user.getId());
	}

	private void submitLog() {
		File submittedDir = Sibyl.createSubmittedDir(this.user, this.messages);

		File logFile = new File(this.user.getUserDataDir() + Sibyl.LOG_FILE);

		String date = Sibyl.DMY_formatter.format(new Date(System.currentTimeMillis()));
		File submittedLog = new File(submittedDir + "/" + date + ".log");

		String log = this.getLogContents(logFile);

		if (submittedLog.exists()) {
			writeLog(submittedLog, log);
			if (logFile.delete() == false) {
				this.messages.add("Problem deleting " + logFile.getName());
			}
		} else {
			try {
				System.out.println("Renamining to "
						+ submittedLog.getCanonicalPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (logFile.renameTo(submittedLog) == false) {
				// Copy the file the long way
				writeLog(submittedLog, log);
				if (logFile.delete() == false) {
					this.messages.add("Problem deleting " + logFile.getName());
				}
			}
		}
		
		EmailNotification.send("User #" + this.user.getId() + " submitted log.", log);
	}
	
	private void writeLog(File submittedLog, String log) {
		FileWriter writer = null;
		try {
			writer = new FileWriter(submittedLog, true);
			writer.append(log);
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

	private String getLogContents(File logFile) {
		BufferedReader reader = null;
		StringBuffer log = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(logFile));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				log.append(line + "\n");
			}
		} catch (IOException e) {
			this.messages.add("Error reading " + logFile);
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
