/*******************************************************************************
 * Copyright (c) 2005 University Of British Columbia and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     University Of British Columbia - initial API and implementation
 *******************************************************************************/
package ca.uleth.bugtriage.sibyl.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import javax.security.auth.login.LoginException;

/**
 * Parses Bugzilla bug activity page to fill in a BugActivity object
 * 
 * @author John Anvik
 */
public class ActivityParser {
	public BugActivity retrieve(String repository, String userName, String password, int bugID) throws IOException,
			MalformedURLException, LoginException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parse the activity page for events
	 * 
	 * @return A BugActivity object containing the activity history
	 * @throws IOException
	 * @throws ParseException
	 */
	public BugActivity getActivity(BufferedReader in, int bugID)
			throws IOException, ParseException, LoginException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parse the events that have happened to the bug
	 * 
	 * @param tokenizer
	 *            the token stream
	 * @param activity
	 *            the activity object to store the events in
	 * @return
	 */
	private void parseActivity(BugActivity activity) throws IOException, ParseException {
		throw new UnsupportedOperationException();
	}

	/**
	 * Parse an activity entry
	 * 
	 * @param tokenizer
	 *            the stream of tokens
	 * @param activity
	 *            the activity object to store events in
	 */
	private void parseEvent(BugActivity activity) {

		throw new UnsupportedOperationException();
	}

	private String getData() {

		throw new UnsupportedOperationException();
	}
}
