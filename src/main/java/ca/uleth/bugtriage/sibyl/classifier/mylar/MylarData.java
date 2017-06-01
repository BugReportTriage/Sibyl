package ca.uleth.bugtriage.sibyl.classifier.mylar;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class MylarData {
	public final static String MYLAR_DIR = Environment.getBugDataDir() + "mylar/";
	public static final String DUPLICATES = MYLAR_DIR + "mylarDups.bugs";
	public final static String MYLAR_CVS_DIR = Environment.getCVSDataDir()
	+ "mylar/";
	public static final String DEVELOPER_INFO = MYLAR_CVS_DIR
	+ "mylar.bugNAMEdev";
	
	public static final int TESTING_MONTH = 10;

	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;

	public static final String[] USER_NAMES = {
		"spingel=steffen.pingel@eclipse.org,steffenp@gmx.de",
	"ibull=irbull@cs.uvic.ca",
	"sminto=sminto@gmail.com",
	"mkersten=mik.kersten@eclipse.org",
	"relves=rob.elves@eclipse.org,relves@cs.ubc.ca",
	
	};
}