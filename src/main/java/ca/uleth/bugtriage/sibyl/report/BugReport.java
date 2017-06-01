package ca.uleth.bugtriage.sibyl.report;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;
import ca.uleth.bugtriage.sibyl.report.BugzillaReport;

/**
 * A bug report entered in Bugzilla.
 */
public class BugReport implements Serializable, Comparable<BugReport> {

	/**
	 * Generated serialization id
	 */
	private static final long serialVersionUID = 8129839924723442681L;

	private BugActivity activity;

	private int reportId;
	private List<String> ccList;
	private String component;
	private String summary;
	private String reporter;	
	private String assigned;
	private String resolution;
	private Date created;
	private String operatingSystem;
	private String hardware;
	private String changed;
	private String priority;
	private String duplicateOf;
	private String severity;
	
	private String description;
	
	private List<ReportAttachment> attachments;
	private List<Comment> comments;
	private StatusType status;	

	public static final Pattern SUBCOMPONENT_REGEX = Pattern.compile("\\[((\\w+\\/*\\s*)+)\\]");

	public BugReport(int id) {
		this.reportId = id;
	}

	/**
	 * Get the change history
	 * 
	 * @return The change history of the bug
	 * 
	 *         (As getting the history is expensive (requires parsing a seperate
	 *         HTML file) the history is retrieved lazily)
	 */
	public BugActivity getActivity() {
		return this.activity;
	}

	public void setActivity(BugActivity activity) {
		this.activity = activity;
	}

	public List<String> getCC() {
		return this.ccList;
	}

	public String getComponent() {
		return this.component;
	}

	public int getId() {
		return this.reportId;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public String getSummary() {
		return this.summary;
	}

	public String getDescription() {		
		return this.description;
	}

	public String getReporter() {
		return this.reporter;
	}

	public StatusType getStatus() {		
		return this.status;
	}

	public String getAssignedTo() {
		return this.assigned;
	}

	public String getResolution() {
		return this.resolution;
	}

	public Date getCreated() {
		return this.created;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BugReport) {
			BugReport tbr = (BugReport) obj;
			this.equals(tbr);
		}
		return false;
	}

	public boolean equals(BugReport report) {
		return report.getId() == this.getId();
	}

	@Override
	public int hashCode() {
		// System.out.println("using hashcode");
		return this.getId();
	}

	public String getOS() {
		return this.operatingSystem;
	}

	public String getHardware() {
		return this.hardware;
	}

	public String lastModified() {
		return this.changed;
	}

	public String getSubcomponent() {
		Matcher subcomponentMatcher = SUBCOMPONENT_REGEX.matcher(this.summary);
		if (subcomponentMatcher.find()) {
			return subcomponentMatcher.group(1);
		}
		return null;
	}

	public int getDupId() {
		Pattern dupIdPattern = Pattern
				.compile("\\*\\*\\* This bug has been marked as a duplicate of (bug )?(\\d+) \\*\\*\\*");
		for (Comment comment : this.getComments()) {
			String text = comment.getText();
			Matcher matcher = dupIdPattern.matcher(text);
			if (matcher.find()) {
				String dupIdStr = matcher.group(2);
				return Integer.parseInt(dupIdStr);
			}
		}
		return -1;
	}

	public int compareTo(BugReport reportToCompare) {
		if (reportToCompare.getId() > this.getId()) {
			return 1;
		}
		if (reportToCompare.getId() < this.getId()) {
			return -1;
		}
		return 0;
	}

	public List<ReportAttachment> getAttachments() {
		return this.attachments;
	}

	public void setDescription(String desc) {
		this.description = desc;
	}

	public void setReportId(int reportId) {
		this.reportId = reportId;
	}

	public void setCcList(List<String> ccList) {
		this.ccList = ccList;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public void setReporter(String reporter) {
		this.reporter = reporter;
	}

	public void setAssigned(String assigned) {
		this.assigned = assigned;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setOperatingSystem(String operatingSystem) {
		this.operatingSystem = operatingSystem;
	}

	public void setHardware(String hardware) {
		this.hardware = hardware;
	}

	public void setChanged(String changed) {
		this.changed = changed;
	}

	public void setStatus(String status) {		
		this.status = StatusType.convert(status);
	}
}
