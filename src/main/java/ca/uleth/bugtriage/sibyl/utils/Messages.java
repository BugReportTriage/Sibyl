package ca.uleth.bugtriage.sibyl.utils;

import java.util.ArrayList;
import java.util.List;

import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;

public class Messages {

	private static final String BANNER = "Sibyl Messages";
	private List<String> messages;

	public Messages() {
		this.messages = new ArrayList<String>();
	}

	public void add(String msg) {
		this.messages.add(msg);

	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if (this.messages.isEmpty() == false) {
			sb.append(Webpage.banner(BANNER) + ">\n");
		}
		for (String msg : this.messages) {
			sb.append(Webpage.message(msg) + "\n");
		}
		if (this.messages.isEmpty() == false) {
			sb.append(Webpage.DIVIDER + "\n");
		}
		return sb.toString();
	}

	public void clear() {
		this.messages.clear();

	}

	public int size() {
		return this.messages.size();
	}
}
