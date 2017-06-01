package ca.uleth.bugtriage.sibyl.analysis;

import java.util.Date;

public class ViewEvent {

	private final Date date;

	private final String id;

	public ViewEvent(Date date, String id) {
		this.date = date;
		this.id = id;
	}
	
	public String reportId() {
		return id;
	}

	public Date getDate() {
		return date;
	}

	@Override
	public String toString() {
		return this.id + "(VE):" + this.date;
	}
	
	
}
