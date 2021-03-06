package ca.uleth.bugtriage.sibyl;

import java.io.Serializable;

import ca.uleth.bugtriage.sibyl.report.BugReport;

public class Classification implements Comparable<Classification>, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7354493261019531378L;

	private final String classification;

	private String reason;

	private final double probability;

	public Classification(String classification, String reason,
			double probability) {
		this.classification = classification;
		this.reason = reason;
		this.probability = probability;
	}

	@Override
	public String toString() {
		return this.getClassification();// + "(" + this.probability + ")";
	}

	public String getClassification() {
		return this.classification;
	}

	public String getReason() {
		return this.reason;
	}

	public double getProbability() {
		return this.probability;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Classification) {
			Classification c = (Classification) obj;
			return this.equals(c);
		}
		return false;
	}
	
	public boolean equals(Classification c) {
		return c.getClassification().equals(this.classification);
	}
	
	public int compareTo(Classification c) {
		if (this.probability < c.getProbability()) {
			return -1;
		} else if (this.probability > c.getProbability()) {
			return 1;
		}
		return 0;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
