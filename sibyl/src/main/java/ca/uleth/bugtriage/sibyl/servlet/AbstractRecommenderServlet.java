package ca.uleth.bugtriage.sibyl.servlet;

import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.exceptions.UserNotFoundException;
import ca.uleth.bugtriage.sibyl.servlet.util.Utils;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UBCWebpage;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Messages;

/**
 * Abstract class for the many servlets used in the recommendation evaluation
 * 
 * @author janvik
 * 
 */
public abstract class AbstractRecommenderServlet extends HttpServlet {

	protected UBCWebpage getPage(){
		throw new UnsupportedOperationException();
	}

	protected final Messages messages = new Messages();

	protected SibylUser user;

	protected Map<String, String> data;

	protected String query;

	protected String url;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {
		this.messages.clear();
		this.query = request.getQueryString();

		if (null == this.query) { // No query string, try POST method
			this.doPost(request, response);
			return;
		}

		String page;
		try {
			this.user = Utils.getUser(this.query, this.messages);
			this.url = getURL(this.query);
			page = this.createPage();
		} catch (UserNotFoundException e) {
			this.messages.add("Could not find user information. (" + this.query
					+ ")");
			page = "";
		} catch (PasswordNotFoundException e) {
			this.messages.add("Could not find password for user. (" + this.query
					+ ")");
			page = "";
		}

		Utils.sendPage(response, page, this.messages);
	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) {
		this.messages.clear();
		this.data = Utils.getData(request, this.messages);
		//this.printData();

		String userId = this.data.get(SibylUser.USER_ID_KEY);
		try {
			this.user = SibylUser.getUser(userId, this.messages);
		} catch (UserNotFoundException e) {
			this.user = new SibylUser(userId);
		} catch (PasswordNotFoundException e) {
			// Ignore
		}
		String page = this.createPage();
		Utils.sendPage(response, page, this.messages);
	}

	protected String createPage() {
		return this.getPage().createPage(this.user);
	}

	protected void printData(){
		for (String key : this.data.keySet()) {
			this.messages.add(key + ": " + this.data.get(key));
		}
	}
	
	/*
	 * Necessary because the value of HttpServletRequest.getParameter("url")
	 * gets truncated if '&' is in the parameter value.
	 * 
	 * Example:
	 * url=https://bugzilla.mozilla.org/show_bug.cgi?query_format=specific&order=relevance+desc&bug_status=__open__&id=9895
	 */

	private String getURL(String queryString) {
		if (null == queryString) {
			return null;
		}
		int httpLocation = queryString.lastIndexOf("http");
		if (httpLocation == -1) {
			return null;
		}
		return queryString.substring(httpLocation);
	}
}
