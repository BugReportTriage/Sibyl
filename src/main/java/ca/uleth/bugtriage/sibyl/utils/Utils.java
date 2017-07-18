package ca.uleth.bugtriage.sibyl.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	private static final long DAY_IN_MILLISEC = 24 * 60 * 60 * 1000;

	private static final int MOST_RECENT_YEAR = 2006;	
	
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

	public static <K, V extends Comparable<? super V>> SortedSet<Map.Entry<K, V>> entriesSortedByValues(Map<K, V> map) {
		SortedSet<Map.Entry<K, V>> sortedEntries = new TreeSet<Map.Entry<K, V>>(new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> e1, Map.Entry<K, V> e2) {
				int res = e2.getValue().compareTo(e1.getValue());
				return res != 0 ? res : 1;
			}
		});
		sortedEntries.addAll(map.entrySet());
		return sortedEntries;
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
