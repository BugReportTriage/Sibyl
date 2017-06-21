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

	public static final boolean DEVELOPMENT_INSTANCE = false;

	private static final String CVS_DATA_DIR = "sibyl/cvsEval/";

	private static String BUG_DATA_DIR = null;

	public static String getRootDir() {
		return System.getProperty("user.dir") + "/";
	}

	
	public static String getBugDataDir() {
		if (BUG_DATA_DIR == null) {
			BUG_DATA_DIR = getRootDir() + "data/";			
		}
		return BUG_DATA_DIR;
	}

	public static String getClassifierDir() {
		if (CLASSIFIER_DIR == null) {
			CLASSIFIER_DIR = getRootDir() + "classifiers/";
			File dir = new File(CLASSIFIER_DIR);
			if(dir.exists() == false)
				dir.mkdirs();
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
				reader.close();
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
