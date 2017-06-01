package ca.uleth.bugtriage.sibyl.servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.exceptions.UserNotFoundException;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.BugzillaPage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Messages;
import ca.uleth.bugtriage.sibyl.utils.Utils;

/*
 * This servlet captures and stores the cookie that Bugzilla returns with a search is done.
 */
public class BugzillaSearchServlet extends AbstractRecommenderServlet {

	/**
	 * Id for serialization
	 */
	private static final long serialVersionUID = 4861766458966485932L;

	protected String createPage() {
		BugzillaPage page = new BugzillaPage();
		try {
			
			// Forward search request
			URL queryURL = new URL(ca.uleth.bugtriage.sibyl.servlet.util.Utils
					.addCredentials(this.url, this.user.getName(), this.user
							.getPassword(), this.messages));
			HttpURLConnection connection = Utils.connectToServer(queryURL,
					this.messages);

			connection.addRequestProperty("Cookie", this.user.getLastOrder());
			
			// Store BUGLIST cookies
			Map<String, List<String>> headers = connection.getHeaderFields();
			List<String> cookies = headers.get("Set-Cookie");
			// String cookie = connection.getHeaderField("Set-Cookie");
			for (String cookie : cookies) {
				String[] split = cookie.split("=");
				if (split[0].equals("BUGLIST")) {
					this.user.saveBuglist(cookie);
					//this.messages.add(cookie);
				}
				
				if (split[0].equals("LASTORDER")) {
					this.user.saveLastOrder(cookie);
					//this.messages.add(cookie);
				}
			}
			
			
			/* Send back search results */
			connection = Utils.connectToServer(queryURL,
					this.messages);
			connection.addRequestProperty("Cookie", this.user.getLastOrder() + "; " + this.user.getBuglist());
			
			page.get(connection.getInputStream(), this.messages);
			page.update(this.user);

		} catch (MalformedURLException e) {
			this.messages.add(e.getMessage());
		} catch (IOException e) {
			this.messages.add(e.getMessage());
		}
		return page.toString();
	}

	public static void main(String[] args) {
		BugzillaSearchServlet servlet = new BugzillaSearchServlet();
		Messages msgs = new Messages();

		try {
			if (Environment.DEVELOPMENT_INSTANCE == false)
				throw new RuntimeException(
						"Not using the development instance.");
			servlet.user = SibylUser.getUser("1", msgs);
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		servlet.query = "https://stavanger.cs.ubc.ca:8443/sibyl/search?userId=1&http://ws.cs.ubc.ca/~janvik/bugs/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=Sibyl&component=Web+Service&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&bug_status=NEW&emailassigned_to1=1&emailtype1=substring&email1=&emailassigned_to2=1&emailreporter2=1&emailcc2=1&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=";
		servlet.url = "http://ws.cs.ubc.ca/~janvik/bugs/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=Sibyl&component=Web+Service&long_desc_type=substring&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&bug_status=NEW&emailassigned_to1=1&emailtype1=substring&email1=&emailassigned_to2=1&emailreporter2=1&emailcc2=1&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=&chfieldto=Now&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=";
		System.out.println(servlet.createPage());

	}
}
