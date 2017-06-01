package ca.uleth.bugtriage.sibyl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import junit.framework.TestCase;

import org.junit.Assert;

import ca.uleth.bugtriage.sibyl.servlet.util.Utils;

public class Cookies extends TestCase {

	private static final String user = "jkanvik@gmail.com";

	private static final String password = "BugTriage";

	private static String SEARCH_QUERY = "http://ws.cs.ubc.ca/~janvik/bugs/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=Sibyl&component=Web+Service&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&bug_status=NEW&emailassigned_to1=1&emailtype1=substring&email1=&emailassigned_to2=1&emailreporter2=1&emailcc2=1&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=";

	private static String BUG_QUERY = "http://ws.cs.ubc.ca/~janvik/bugs/show_bug.cgi?id=20";

	private URL searchPage;

	private URL bugPage;

	private String cookie;

	protected void setUp() throws Exception {
		this.searchPage = new URL(Utils.addCredentials(SEARCH_QUERY, user,
				password, null));
		this.bugPage = new URL(Utils.addCredentials(BUG_QUERY, user, password,
				null));
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testSearchResultCookie() {
		try {
			URLConnection urlConnection = this.searchPage
					.openConnection(Proxy.NO_PROXY);
			if (urlConnection != null) {
				this.cookie = urlConnection.getHeaderField("Set-Cookie");
				Assert
						.assertEquals(
								"BUGLIST=27%3A20%3A19; path=/; expires=Fri, 01-Jan-2038 00:00:00 GMT",
								this.cookie);
			}

			System.out.println(this.bugPage);
			urlConnection = this.bugPage.openConnection();
			if (urlConnection != null) {
				// System.out.println(this.cookie);
				urlConnection.setRequestProperty("Cookie", this.cookie);
				// urlConnection.connect();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						urlConnection.getInputStream()));
				for (String line = new String(); line != null; line = in
						.readLine()) {
					if (line.contains(">First<")) {
						Assert.assertEquals(
								"<a href=\"show_bug.cgi?id=27\">First</a>",
								line.trim());
					}
					if (line.contains(">Last<")) {
						Assert
								.assertEquals(
										"<a href=\"show_bug.cgi?id=19\">Last</a>",
										line.trim());
					}
					if (line.contains(">Prev<")) {
						Assert
								.assertEquals(
										"<a href=\"show_bug.cgi?id=27\">Prev</a>",
										line.trim());
					}
					if (line.contains(">Next<")) {
						Assert
								.assertEquals(
										"<a href=\"show_bug.cgi?id=19\">Next</a>",
										line.trim());
					}
					if (line.contains("Show last search results")) {
						Assert
								.assertEquals(
										"&nbsp;&nbsp;<a href=\"buglist.cgi?regetlastlist=1\">Show last search results</a>",
										line.trim());
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
