package ca.uleth.bugtriage.sibyl.utils;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.mylar.internal.bugzilla.core.IBugzillaConstants.BugzillaServerVersion;

public enum LoginInfo {
	ECLIPSE("https://bugs.eclipse.org/bugs", "janvik@cs.ubc.ca", "BugTriage", "Platform", BugzillaServerVersion.SERVER_220), 
	MOZILLA("https://bugzilla.mozilla.org", "janvik@cs.ubc.ca", "BugTriage", "Mozilla+Application+Suite", BugzillaServerVersion.SERVER_220),
	FIREFOX("https://bugzilla.mozilla.org", "janvik@cs.ubc.ca", "BugTriage", "Firefox", BugzillaServerVersion.SERVER_220),
	ANT("http://issues.apache.org/bugzilla", "janvik@cs.ubc.ca", "BugTriage", "Ant", BugzillaServerVersion.SERVER_218),
	TOMCAT("http://issues.apache.org/bugzilla", "janvik@cs.ubc.ca", "BugTriage", "Tomcat+5", BugzillaServerVersion.SERVER_218),
//	SPAMASSASSIN("http://bugzilla.spamassassin.org", "janvik@cs.ubc.ca", "BugTriage", "Spamassassin", BugzillaServerVersion.SERVER_),
//	EVOLUTION("http://bugzilla.gnome.org", "janvik@cs.ubc.ca", "BugTriage", "Evolution", true),
	GCC("http://gcc.gnu.org/bugzilla", "janvik@cs.ubc.ca", "BugTriage", "gcc", BugzillaServerVersion.SERVER_220),
	;

	/*
	 * public final static String URL = MOZILLA.getRepositoryURL(); public final
	 * static String USER_NAME = MOZILLA.getUserName(); public final static
	 * String PASSWORD = MOZILLA.getPassword();
	 */
	
	private URL repository;

	private final String userName;

	private final String password;
	private final String product;

	private final BugzillaServerVersion version;

	private LoginInfo(String repositoryURL, String userName, String password, String product, BugzillaServerVersion version) {
		this.version = version;
		try {
			this.repository = new URL(repositoryURL);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		this.userName = userName;
		this.password = password;
		this.product = product;
	}

	public String getPassword() {
		return this.password;
	}

	public URL getRepository() {
		return this.repository;
	}

	public String getUserName() {
		return this.userName;
	}

	public String getProduct() {
		return this.product;
	}

	public BugzillaServerVersion getVersion() {
		return this.version;
	}
}
