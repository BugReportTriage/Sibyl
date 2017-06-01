package ca.uleth.bugtriage.sibyl.heuristic;

public class ClassificationTestReport {

	public final int reportId;
	public final String classification;
	
	public ClassificationTestReport(final int reportId, final String classification) {
		this.reportId = reportId;
		this.classification = classification;
	}
}
