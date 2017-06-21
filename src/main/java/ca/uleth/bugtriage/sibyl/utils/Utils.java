package ca.uleth.bugtriage.sibyl.utils;

import java.io.File;
import java.util.Date;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.report.BugReport;



public class Utils {

	private static final long DAY_IN_MILLISEC = 24 * 60 * 60 * 1000;
	
	public static long numDays(Date first, Date second) {
		long timeDifference;
		if (first.before(second)) {
			timeDifference = second.getTime() - first.getTime();
		} else {
			timeDifference = first.getTime() - second.getTime();
		}
		return timeDifference / DAY_IN_MILLISEC;
	}

	public static String[] getTrainingSet(String firefoxDir, int numMonths, int lastTrainingMonth) {
		// TODO Auto-generated method stub
		return null;
	}

	public static Set<BugReport> dataFiles(File file) {
		// TODO Auto-generated method stub
		return null;
	}

	public static String[] getDataset(Set<BugReport> dataFiles, int numProfileDays) {
		// TODO Auto-generated method stub
		return null;
	}
}
