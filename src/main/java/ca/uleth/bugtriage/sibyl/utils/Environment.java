package ca.uleth.bugtriage.sibyl.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Environment {

	/**
	 * The strange series of method calls to get various file directories was
	 * done so that the system adapts to whereever the classes run.
	 */

	private static final String CONFIGURATION_FILE = "sibyl.conf";

	private static String SERVLET_URL = null;

	private static String TOMCAT_DIR = null;

	private static String SERVLET_DIR = null;

	private static String SERVLET_DATA_DIR = null;

	private static String CLASSIFIER_DIR = null;

	private static final String WINDOWS_DIR = "C:/";

	private static final String SERVER_DIR = "/isd/se2/usr/janvik/";

	public static final boolean DEVELOPMENT_INSTANCE = false;

	private static final String CVS_DATA_DIR = "sibyl/cvsEval/";

	private static String BUG_DATA_DIR = null;

	private static String ROOT_DIR;

	public static String getRootDir() {
		if (ROOT_DIR == null) {
			System.err.println("Setting root dir");
			File serverDir = new File(SERVER_DIR);
			File windowsDir = new File(WINDOWS_DIR);
			if (serverDir.exists()) {
				ROOT_DIR = SERVER_DIR;
			} else if (windowsDir.exists()) {
				ROOT_DIR = WINDOWS_DIR;
			} else {
				System.err
						.println("I have no clue where I am! Please check the filesystem I am running on.");
			}
			System.err.println("ROOT_DIR: " + ROOT_DIR);
		}
		return ROOT_DIR;
	}

	
	public static String getBugDataDir() {
		if (BUG_DATA_DIR == null) {
			BUG_DATA_DIR = getRootDir() + "Documents and Settings/John/My Documents/Work/Sibyl/dataSets/";
			//BUG_DATA_DIR = getServletDataDir() + "dataSets/";
		}
		return BUG_DATA_DIR;
	}

	public static String getClassifierDir() {
		if (CLASSIFIER_DIR == null) {
			CLASSIFIER_DIR = getRootDir() + "Documents and Settings/John/My Documents/Work/Sibyl/classifiers/";
			//CLASSIFIER_DIR = getServletDataDir() + "classifier/";
		}
		return CLASSIFIER_DIR;
	}

	public static String getServletDataDir() {
		if (SERVLET_DATA_DIR == null) {
			SERVLET_DATA_DIR = getServletDir() + "data/";
		}
		return SERVLET_DATA_DIR;
	}

	private static String getServletDir() {
		if (SERVLET_DIR == null) {
			SERVLET_DIR = getTomcatDir() + "webapps/sibyl/";
		}
		return SERVLET_DIR;
	}

	private static String getTomcatDir() {
		if (TOMCAT_DIR == null) {
			if (DEVELOPMENT_INSTANCE)
				TOMCAT_DIR = getRootDir() + "sibyl/tomcat-dev/";
			else
				TOMCAT_DIR = getRootDir() + "sibyl/tomcat/";

		}
		return TOMCAT_DIR;
	}

	public static String getWebPageDir() {
		return getServletDir();
	}

	public static String getServletUrl() {
		if (SERVLET_URL == null) {
			String confFile = getServletDir() + CONFIGURATION_FILE;
			try {
				File file = new File(confFile);
				BufferedReader reader = new BufferedReader(new FileReader(file));
				SERVLET_URL = reader.readLine();
			} catch (FileNotFoundException e) {
				System.err.println("Configuration file not found: " + confFile);
			} catch (IOException e) {
				System.err.println("Problem reading: " + confFile);
			}
		}
		return SERVLET_URL;
	}

	public static String getCVSDataDir() {
		return getRootDir() + CVS_DATA_DIR;
	}
}
