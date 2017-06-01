package ca.uleth.bugtriage.sibyl.classifier.libreoffice;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class LibreOfficeData {
	public final static String LIBRE_OFFICE_DIR = Environment.getBugDataDir() + "libreoffice/";

	public final static String LIBRE_OFFICE_CVS_DIR = Environment.getCVSDataDir()
	+ "libreoffice/";
	
	public final static String DUPLICATES = LIBRE_OFFICE_DIR
			+ "libreOfficeDups.bugs";
	
	//public final static String DEVELOPER_INFO = FIREFOX_CVS_DIR + "firefox.bug3NAMEdev"; 
	
	public static final int TESTING_MONTH = 10;
	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;
	
	public static final String[] USER_NAMES = {};
	


	public static final String URL = "https://bugs.documentfoundation.org";

	public static final String PROJECT = "LibreOffice";

	public static final String PRODUCT = "LibreOffice";
	
	
}
