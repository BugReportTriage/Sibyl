package ca.uleth.bugtriage.sibyl.dataset;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class CreateDataset {

	public static DatasetInfo getMonth(Project project, int month, int year) {
		String startDay = "01";
		String endDay = "01";
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			endDay = "31";
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			endDay = "30";
			break;
		case 2:
			if (year % 4 == 0)
				endDay = "29";
			else
				endDay = "28";
			break;
		default:
			break;
		}

		String monthName;
		monthName = getMonthName(month);
		try {
			return new DatasetInfo(project.getProduct().toLowerCase() + "_" + monthName + year + ".bugs",
					project.getURL()
							/* RESOLVED and ASSIGNED */
							+ "/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product="
							+ URLEncoder.encode(project.getProduct(),
									null)
					+ "&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&keywords_type=allwords&keywords=&bug_status=ASSIGNED&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom="
					+ year + "-" + month + "-" + startDay + "&chfieldto=" + +year + "-" + month + "-" + endDay
					+ "&chfield=bug_status&chfield=resolution&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// Should never happen
		return null;
		/*
		 * RESOVLED only +
		 * "/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product="
		 * + serverInfo.getProduct() +
		 * "&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&keywords_type=allwords&keywords=&bug_status=RESOLVED&bug_status=VERIFIED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom="
		 * + year + "-" + month + "-" + start + "&chfieldto=" + year + "-" +
		 * month + "-" + end +
		 * "&chfield=resolution&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0="
		 * );
		 */

	}

	public static String getMonthName(int month) {
		String monthName;
		switch (month) {
		case 1:
			monthName = "Jan";
			break;
		case 2:
			monthName = "Feb";
			break;
		case 3:
			monthName = "Mar";
			break;
		case 4:
			monthName = "Apr";
			break;
		case 5:
			monthName = "May";
			break;
		case 6:
			monthName = "Jun";
			break;
		case 7:
			monthName = "Jul";
			break;
		case 8:
			monthName = "Aug";
			break;
		case 9:
			monthName = "Sep";
			break;
		case 10:
			monthName = "Oct";
			break;
		case 11:
			monthName = "Nov";
			break;
		case 12:
			monthName = "Dec";
			break;
		default:
			monthName = "Unknown";
			break;
		}
		return monthName;
	}

	private final Project project;

	public CreateDataset(Project p) {
		this.project = p;

	}
}

class DatasetInfo {
	private final String queryURL;

	private final String filename;

	public DatasetInfo(String file, String query) {
		this.queryURL = query;
		this.filename = file;
	}

	public String getFilename() {
		return this.filename;
	}

	public String getQueryURL() {
		return this.queryURL;
	}
}