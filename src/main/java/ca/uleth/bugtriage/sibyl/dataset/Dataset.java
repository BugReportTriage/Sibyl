package ca.uleth.bugtriage.sibyl.dataset;

import java.io.Serializable;
import java.util.List;

import ca.uleth.bugtriage.sibyl.report.BugReport;

import java.util.ArrayList;

public abstract class Dataset implements Serializable {

	protected final List<BugReport> reports;

	public Dataset() {
		this.reports = new ArrayList<BugReport>();
	}

	public abstract List<BugReport> getData();
	
}
