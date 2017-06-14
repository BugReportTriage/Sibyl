package ca.uleth.bugtriage.sibyl.dataset;

import java.util.ArrayList;
import java.util.List;

import ca.uleth.bugtriage.sibyl.report.BugReport;

public abstract class Dataset {

	protected final List<BugReport> reports;

	public Dataset() {
		this.reports = new ArrayList<BugReport>();
	}

	public abstract List<BugReport> getData();
	
}
