
package ca.uleth.bugtriage.sibyl.report.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "component",
    "summary",
    "reporter",
    "resolution",
    "created",
    "hardware",
    "priority",
    "duplicateOf",
    "severity",
    "description",
    "attachments",
    "comments",
    "status",
    "activity",
    "subcomponent",
    "assignedTo",
    "dupId",
    "cc",
    "os",
    "id"
})
public class BugReport {

    @JsonProperty("component")
    private String component;
    @JsonProperty("summary")
    private String summary;
    @JsonProperty("reporter")
    private String reporter;
    @JsonProperty("resolution")
    private String resolution;
    @JsonProperty("created")
    private Object created;
    @JsonProperty("hardware")
    private String hardware;
    @JsonProperty("priority")
    private String priority;
    @JsonProperty("duplicateOf")
    private Object duplicateOf;
    @JsonProperty("severity")
    private String severity;
    @JsonProperty("description")
    private String description;
    @JsonProperty("attachments")
    private Object attachments;
    @JsonProperty("comments")
    private List<Comment> comments = null;
    @JsonProperty("status")
    private Object status;
    @JsonProperty("activity")
    private Activity activity;
    @JsonProperty("subcomponent")
    private Object subcomponent;
    @JsonProperty("assignedTo")
    private String assignedTo;
    @JsonProperty("dupId")
    private Integer dupId;
    @JsonProperty("cc")
    private List<String> cc = null;
    @JsonProperty("os")
    private String os;
    @JsonProperty("id")
    private Integer id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("component")
    public String getComponent() {
        return component;
    }

    @JsonProperty("component")
    public void setComponent(String component) {
        this.component = component;
    }

    @JsonProperty("summary")
    public String getSummary() {
        return summary;
    }

    @JsonProperty("summary")
    public void setSummary(String summary) {
        this.summary = summary;
    }

    @JsonProperty("reporter")
    public String getReporter() {
        return reporter;
    }

    @JsonProperty("reporter")
    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    @JsonProperty("resolution")
    public String getResolution() {
        return resolution;
    }

    @JsonProperty("resolution")
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    @JsonProperty("created")
    public Object getCreated() {
        return created;
    }

    @JsonProperty("created")
    public void setCreated(Object created) {
        this.created = created;
    }

    @JsonProperty("hardware")
    public String getHardware() {
        return hardware;
    }

    @JsonProperty("hardware")
    public void setHardware(String hardware) {
        this.hardware = hardware;
    }

    @JsonProperty("priority")
    public String getPriority() {
        return priority;
    }

    @JsonProperty("priority")
    public void setPriority(String priority) {
        this.priority = priority;
    }

    @JsonProperty("duplicateOf")
    public Object getDuplicateOf() {
        return duplicateOf;
    }

    @JsonProperty("duplicateOf")
    public void setDuplicateOf(Object duplicateOf) {
        this.duplicateOf = duplicateOf;
    }

    @JsonProperty("severity")
    public String getSeverity() {
        return severity;
    }

    @JsonProperty("severity")
    public void setSeverity(String severity) {
        this.severity = severity;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("attachments")
    public Object getAttachments() {
        return attachments;
    }

    @JsonProperty("attachments")
    public void setAttachments(Object attachments) {
        this.attachments = attachments;
    }

    @JsonProperty("comments")
    public List<Comment> getComments() {
        return comments;
    }

    @JsonProperty("comments")
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    @JsonProperty("status")
    public Object getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(Object status) {
        this.status = status;
    }

    @JsonProperty("activity")
    public Activity getActivity() {
        return activity;
    }

    @JsonProperty("activity")
    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @JsonProperty("subcomponent")
    public Object getSubcomponent() {
        return subcomponent;
    }

    @JsonProperty("subcomponent")
    public void setSubcomponent(Object subcomponent) {
        this.subcomponent = subcomponent;
    }

    @JsonProperty("assignedTo")
    public String getAssignedTo() {
        return assignedTo;
    }

    @JsonProperty("assignedTo")
    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    @JsonProperty("dupId")
    public Integer getDupId() {
        return dupId;
    }

    @JsonProperty("dupId")
    public void setDupId(Integer dupId) {
        this.dupId = dupId;
    }

    @JsonProperty("cc")
    public List<String> getCc() {
        return cc;
    }

    @JsonProperty("cc")
    public void setCc(List<String> cc) {
        this.cc = cc;
    }

    @JsonProperty("os")
    public String getOs() {
        return os;
    }

    @JsonProperty("os")
    public void setOs(String os) {
        this.os = os;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
