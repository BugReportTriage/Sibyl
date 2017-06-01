package ca.uleth.bugtriage.sibyl.analysis;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubmissionEvent {

	protected final Map<String, String> data = new HashMap<String, String>();
	private Date date;

	public void addData(String str) {
		String[] split = str.split(":", 2);
		if (split.length == 2 && (split[1].equals("") == false)) {
			this.data.put(split[0], split[1]);
		}
	}

	public Object size() {
		return this.data.size();
	}

	public void setDate(Date date) {
		this.date = date;

	}

	public Date getDate() {
		return date;
	}

	public Map<String, String> getData() {
		return data;
	}
}
