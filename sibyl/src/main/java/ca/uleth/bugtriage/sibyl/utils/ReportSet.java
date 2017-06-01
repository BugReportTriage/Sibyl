package ca.uleth.bugtriage.sibyl.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.BugReportJsonBugzillaTest;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class ReportSet extends AbstractSet<BugReport> {

	private Map<String, BugReport> backingStore = new HashMap<String, BugReport>();

	public boolean add(BugReport report) {
		if (this.contains(report)) {
			BugReport existingReport = this.backingStore.get(String
					.valueOf(report.getId()));

			if (existingReport == report)
				return false;

			BugReport newest = newestReport(existingReport, report);
			if (newest != existingReport) {
				this.backingStore.put(String.valueOf(report.getId()), newest);
				return true;
			}
			return false;
		}

		this.backingStore.put(String.valueOf(report.getId()), report);
		return true;
	}

	public boolean contains(Object o) {
		if (o instanceof BugReport) {
			BugReport report = (BugReport) o;
			String key = String.valueOf(report.getId());
			return this.backingStore.containsKey(key);
		}
		return false;
	}

	private BugReport newestReport(BugReport report1,
			BugReport report2) {

		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String report1LastModified = report1.lastModified();
		String report2LastModified = report2.lastModified();

		if (report1LastModified == null)
			return report2;

		if (report2LastModified == null)
			return report1;

		if(report1LastModified.equals(report2LastModified)){
			return report2; // Arbitrary choice
		}
		
		try {
			Date report1Modified = formatter.parse(report1LastModified);
			Date report2Modified = formatter.parse(report2LastModified);

			if (report1Modified.before(report2Modified)) {
				return report2;
			}

			return report1;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Iterator<BugReport> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return 0;
	}

}
