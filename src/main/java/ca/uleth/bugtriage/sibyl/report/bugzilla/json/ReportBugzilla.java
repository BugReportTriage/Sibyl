
package ca.uleth.bugtriage.sibyl.report.bugzilla.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "assigned_to", "assigned_to_detail", "cc", "cc_detail", "component", "creation_time", "creator",
		"creator_detail", "dupe_of", "id", "last_change_time", "op_sys", "platform", "priority", "resolution",
		"severity", "status", "summary" })

public class ReportBugzilla {

	@JsonProperty("assigned_to")
	private String assigned_to;
	@JsonProperty("assigned_to_detail")
	private AssignedToDetail assigned_to_detail;
	@JsonProperty("cc")
	private List<String> cc = null;
	@JsonProperty("cc_detail")
	private List<CcDetail> cc_detail = null;
	@JsonProperty("component")
	private String component;
	@JsonProperty("creation_time")
	private String creation_time;
	@JsonProperty("creator")
	private String creator;
	@JsonProperty("creator_detail")
	private CreatorDetail creator_detail;
	@JsonProperty("dupe_of")
	private Integer dupe_of;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("last_change_time")
	private String last_change_time;
	@JsonProperty("op_sys")
	private String op_sys;
	@JsonProperty("platform")
	private String platform;
	@JsonProperty("priority")
	private String priority;
	@JsonProperty("resolution")
	private String resolution;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("status")
	private String status;
	@JsonProperty("summary")
	private String summary;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public String getAssigned_to() {
		return assigned_to;
	}

	public void setAssigned_to(String assigned_to) {
		this.assigned_to = assigned_to;
	}

	public AssignedToDetail getAssigned_to_detail() {
		return assigned_to_detail;
	}

	public void setAssigned_to_detail(AssignedToDetail assigned_to_detail) {
		this.assigned_to_detail = assigned_to_detail;
	}

	public List<String> getCc() {
		return cc;
	}

	public void setCc(List<String> cc) {
		this.cc = cc;
	}

	public List<CcDetail> getCc_detail() {
		return cc_detail;
	}

	public void setCc_detail(List<CcDetail> cc_detail) {
		this.cc_detail = cc_detail;
	}

	public String getComponent() {
		return component;
	}

	public void setComponent(String component) {
		this.component = component;
	}

	public String getCreation_time() {
		return creation_time;
	}

	public void setCreation_time(String creation_time) {
		this.creation_time = creation_time;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public CreatorDetail getCreator_detail() {
		return creator_detail;
	}

	public void setCreator_detail(CreatorDetail creator_detail) {
		this.creator_detail = creator_detail;
	}

	public Integer getDupe_of() {
		return dupe_of;
	}

	public void setDupe_of(Integer dupe_of) {
		this.dupe_of = dupe_of;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLast_change_time() {
		return last_change_time;
	}

	public void setLast_change_time(String last_change_time) {
		this.last_change_time = last_change_time;
	}

	public String getOp_sys() {
		return op_sys;
	}

	public void setOp_sys(String op_sys) {
		this.op_sys = op_sys;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@Override
	public String toString() {
		return "Bug [assigned_to=" + assigned_to + ", assignedToDetail=" + assigned_to_detail + ", cc=" + cc
				+ ", cc_detail=" + cc_detail + ", cf_blocking_fennec=" + ", component=" + component + ", creation_time="
				+ creation_time + ", creator=" + creator + ", creator_detail=" + creator_detail + ", dupe_of=" + dupe_of
				+ ", last_change_time=" + last_change_time + ", op_sys=" + op_sys + ", platform=" + platform
				+ ", priority=" + priority + ", resolution=" + resolution + ", severity=" + severity + ", status="
				+ status + ", summary=" + summary + "]";
	}

}
