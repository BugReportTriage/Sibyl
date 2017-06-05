package ca.uleth.bugtriage.sibyl.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import ca.uleth.bugtriage.sibyl.activity.ActivityParser;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class Utils {

	private static final ActivityParser ACTIVITY_PARSER = new ActivityParser();

	private static final int RETRY_LIMIT = 10;

	private static final int CONNECTION_RETRY_DELAY = 30; // in seconds

	private static final long DAY_IN_MILLISEC = 24 * 60 * 60 * 1000;

	private static final int MOST_RECENT_YEAR = 2006;
	
	public static Set<BugReport> getReports(String[] reportFiles) {
		
		if(true)
			throw new UnsupportedOperationException();
		
		Map<String, BugReport> bugs = new HashMap<String, BugReport>();

		for (String filename : reportFiles) {
			 //System.out.println("Getting files from : " + filename);
			if (filename == null) {
				System.err.println("Null filename!");
				continue;
			}
			Dataset data = null; //new FileDataset(new File(filename));
			List<BugReport> reports = data.getData();
			for (BugReport report : reports) {
				try {
					String bugId = String.valueOf(report.getId());

					if (bugs.containsKey(bugId) == false) {
						bugs.put(bugId, report);
					} else {
						// System.out.println("Duplicate " + bugId);
					}
				} catch (Exception ex) {
					if (report == null) {
						System.err.println("Null Report in Dataset");
					} else {
						System.err.println("Problem with: " + report.getId());
					}
				}

			}
		}

		Set<BugReport> bugSet = new HashSet<BugReport>(bugs
				.values());
		return bugSet;
	}

	public static Set<BugReport> getReports(String reportFile) {
		String[] array = new String[1];
		array[0] = reportFile;
		return getReports(array);
	}
	
	public static void writeDataset(String filename, List data) {
		try {

			if (data.isEmpty()) {
				System.err.println("No data to write to " + filename);
				return;
			}

			File dataFile = new File(filename);
			File parent = new File(dataFile.getParent());
			if (parent.exists() == false) {
				parent.mkdir();
			}

			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(filename));
			System.out.println("Writing to: " + filename);
			out.writeObject(data);
			out.close();
			System.out.println("Bug list written out: " + filename);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	@Deprecated
	public static String[] getMonths(String[] dataSets, int numMonths) {
		String[] months = new String[numMonths];
		System.arraycopy(dataSets, 0, months, 0, numMonths);
		return months;
	}

	public static long numDays(Date first, Date second) {
		long timeDifference;
		if (first.before(second)) {
			timeDifference = second.getTime() - first.getTime();
		} else {
			timeDifference = first.getTime() - second.getTime();
		}
		return timeDifference / DAY_IN_MILLISEC;
	}
	
	public static String[] dataFiles(File dataDir) {
		List<String> dataFilenames = new ArrayList<String>();
		FileFilter filter = new FileFilter() {
			public boolean accept(File pathname) {
				return pathname.getName().endsWith("1days.bugs");
			}
		};

		if (dataDir.isDirectory()) {
			File[] files = dataDir.listFiles(filter);
			for (File file : files) {
				String name = file.getAbsolutePath();
				dataFilenames.add(name);
			}
		}
		Collections.sort(dataFilenames, Collections.reverseOrder());
		return dataFilenames.toArray(new String[dataFilenames.size()]);
	}

	/*
	 * Assumes that each data file represents a day and parameter array is in
	 * reverse chronological order
	 */
	public static String[] getDataset(String[] dataFiles, int days) {
		int arraySize = (dataFiles.length >= days) ? days : dataFiles.length;

		String[] trainingData = new String[arraySize];
		System.arraycopy(dataFiles, 0, trainingData, 0, arraySize);
		return trainingData;
	}

	public static String[] createFileset(File fileDirectory, Pattern regex) {

		Matcher matcher;
		List<String> filteredFilenames = new ArrayList<String>();
		String[] filenames = fileDirectory.list();
		for (String filename : filenames) {
			matcher = regex.matcher(filename);
			if (matcher.find()) {
				filteredFilenames.add(fileDirectory + "/" + filename);
				//System.out.println(filename);
			}
		}
		String[] files = filteredFilenames.toArray(new String[0]);
		Arrays.sort(files, Collections.reverseOrder());
		return files;
	}

	/**
	 * Assumes that final month of training set is June 2006
	 * 
	 * @param fileDir
	 * @param numMonths
	 * @return
	 */
	public static String[] getTrainingSet(String fileDir, int numMonths,
			int lastMonth) {

		
		
		String regex;
		if (numMonths <= lastMonth) {
			int startMonth = lastMonth - numMonths + 1;
			String months = new String();
			for(int i = startMonth; i <= lastMonth; i++){
				if(i < 10){
					months += "0" + i + "|";
				}else{
					months += i + "|";	
				}
			}
			months = months.substring(0, months.length()-1);
			regex = MOST_RECENT_YEAR + "_(" + months
					+ ")_\\d+";
			//System.out.println("Regex: " + regex);
		} else {
			regex = MOST_RECENT_YEAR + "_0[1-" + lastMonth + "]_\\d+";
			int remaining = numMonths - lastMonth;
			if (remaining < 3) {
				regex += "|" + (MOST_RECENT_YEAR - 1) + "_1[" + (3 - remaining) + "-2]_\\d+";
			} else {
				regex += "|" + (MOST_RECENT_YEAR - 1) + "_1[0-2]_\\d+";
				regex += "|" + (MOST_RECENT_YEAR - 1) + "_0[" + (12 - remaining) + "-9]_\\d+";
			}
		}
		Pattern pattern = Pattern.compile(regex);
		String[] testingSet = createFileset(new File(fileDir), pattern);
		return testingSet;
	}

	public static String[] getTestingSet(String fileDir, int month) {
		String regex;
		if (month < 10) {
			regex = MOST_RECENT_YEAR + "_0" + month + "_\\d+";
		} else {
			regex = MOST_RECENT_YEAR + "_" + month + "_\\d+";
		}

		Pattern pattern = Pattern.compile(regex);
		String[] testingSet = createFileset(new File(fileDir), pattern);
		return testingSet;
	}

	/*
	public static String duplicateURL(String repository, int id) {
		String[] excludedFields = { "cc", "short_desc", "bug_id",
				"creation_ts", "delta_ts", "reporter_accessible",
				"cclist_accessible", "classification_id", "classification",
				"product", "component", "version", "rep_platform", "op_sys",
				"bug_status", "resolution", "priority", "bug_severity",
				"target_milestone", "everconfirmed", "reporter", "assigned_to" };
		StringBuffer url = new StringBuffer(repository + "/show_bug.cgi?id="
				+ id + "&ctype=xml");
		for (String field : excludedFields) {
			url.append("&excludefield=" + field);
		}
		return url.toString();
	}
	*/
}
