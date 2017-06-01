package ca.uleth.bugtriage.sibyl.classifier.kdeplasma;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class KDEData {
	public final static String KDE_PLASMA_DIR = Environment.getBugDataDir() + "kde-plasma/";

	public final static String KDE_PLASMA_CVS_DIR = Environment.getCVSDataDir()
	+ "kde-plasma/";
	
	public final static String DUPLICATES = KDE_PLASMA_DIR
			+ "kde-plasmaDups.bugs";
	
	//public final static String DEVELOPER_INFO = FIREFOX_CVS_DIR + "firefox.bug3NAMEdev"; 
	
	public static final int TESTING_MONTH = 10;
	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;
	
	public static final String[] USER_NAMES = {};
	


	public static final String URL = "https://bugs.kde.org";

	public static final String PROJECT = "plasmashell";
	
	
}
