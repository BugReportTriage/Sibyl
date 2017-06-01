package ca.uleth.bugtriage.sibyl.classifier.ant;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class AntData {
	public final static String ANT_DIR = Environment.getBugDataDir() + "ant/";

	public final static String ANT_CVS_DIR = Environment.getCVSDataDir()
	+ "ant/";
	
	public final static String DUPLICATES = ANT_DIR
			+ "antDups.bugs";
	
	public final static String DEVELOPER_INFO = ANT_CVS_DIR + "ant.bug3NAMEdev"; 
	
	public static final int TESTING_MONTH = 7;
	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;
	
	public static final String[] USER_NAMES = {

		};
}
