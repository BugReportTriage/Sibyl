package ca.uleth.bugtriage.sibyl.report;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;

/**
 * A bug report entered in Bugzilla.
 */
public class BugReport implements Comparable<BugReport> {

    private int reportId;
    private List<String> ccList;
    private String component;
    private String summary;
    private String reporter;
    private String assigned;
    private Date created;
    private String operatingSystem;
    private String hardware;
    private String changed;
    private String priority;
    private String duplicateOf;
    private String severity;

    private String description;

    private List<Comment> comments;
    private StatusType status;
    private BugActivity activity;
    private ResolutionType resolution;

    public static final Pattern SUBCOMPONENT_REGEX = Pattern.compile("\\[((\\w+\\/*\\s*)+)\\]");

    public BugReport() {
    }

    public void setId(int id) {
	this.reportId = id;
    }

    public BugActivity getActivity() {
	return this.activity;
    }

    public void setActivity(BugActivity activity) {
	this.activity = activity;
    }

    public List<String> getCCList() {
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
	if (this.comments.isEmpty()) {
	    System.err.println("Report #" + getId() + " has no comments?");
	    return "";
	} else {
	    return this.comments.get(0).getText();
	}
    }

    public String getReporter() {
	return this.reporter;
    }

    public StatusType getStatus() {
	return this.status;
    }

    public String getAssigned() {
	return this.assigned;
    }

    public ResolutionType getResolution() {
	return this.resolution;
    }

    public Date getCreated() {
	return this.created;
    }

    @Override
    public boolean equals(Object obj) {
	if (obj instanceof BugReport) {
	    BugReport tbr = (BugReport) obj;
	    return this.equals(tbr);
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

    public String getOperatingSystem() {
	return this.operatingSystem;
    }

    public String getHardware() {
	return this.hardware;
    }

    public String lastModified() {
	return this.changed;
    }

    public String subcomponent() {
	Matcher subcomponentMatcher = SUBCOMPONENT_REGEX.matcher(this.summary);
	if (subcomponentMatcher.find()) {
	    return subcomponentMatcher.group(1);
	}
	return null;
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

    public void setDescription(String desc) {
	this.description = desc;
    }

    public void setReportId(int reportId) {
	this.reportId = reportId;
    }

    public void setCCList(List<String> ccList) {
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
	this.resolution = ResolutionType.convert(resolution);
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

    public String getPriority() {
	return priority;
    }

    public void setPriority(String priority) {
	this.priority = priority;
    }

    public String getDuplicateOf() {
	return duplicateOf;
    }

    public void setDuplicateOf(String duplicateOf) {
	this.duplicateOf = duplicateOf;
    }

    public String getSeverity() {
	return severity;
    }

    public void setSeverity(String severity) {
	this.severity = severity;
    }

    @Override
    public String toString() {
	return Integer.toString(this.getId());
    }
}
