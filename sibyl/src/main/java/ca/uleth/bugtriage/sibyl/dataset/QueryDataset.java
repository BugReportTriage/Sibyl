package ca.uleth.bugtriage.sibyl.dataset;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaSearch;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;

public class QueryDataset extends Dataset {

	private static final long serialVersionUID = 1L;

	private final String queryURL;

	private final User user;

	public QueryDataset(User user, String queryURL) {
		this.queryURL = queryURL;
		this.user = user;
	}

	@Override
	public List<BugReport> getData() {

		if (this.reports.isEmpty()) {
			BugzillaSearch searchEngine = new BugzillaSearch(this.user, this.queryURL);
			this.reports.addAll(searchEngine.getReports(searchEngine.search()));
		}
		return this.reports;
	}
	
	public static String constructUrl(User user, Date start, Date end) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return user.getRepository()
					/* RESOLVED and ASSIGNED */
					+ "/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product="
					+ URLEncoder.encode(user.getProject().getProduct(),
							Webpage.ENCODE_FORMAT)
					+ "&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&keywords_type=allwords&keywords=&bug_status=ASSIGNED&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom="
					+ formatter.format(start)
					+ "&chfieldto="
					+ formatter.format(end)
					+ "&chfield=bug_status&chfield=resolution&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=";
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Should never reach here */
		return null;

	}
}
