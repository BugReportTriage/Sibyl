
package ca.uleth.bugtriage.sibyl.report.bugzilla.json;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "alias", "assigned_to", "assigned_to_detail", "blocks", "cc", "cc_detail", "cf_blocking_fennec",
		"cf_blocking_fx", "cf_crash_signature", "cf_fx_iteration", "cf_fx_points", "cf_has_regression_range",
		"cf_has_str", "cf_last_resolved", "cf_platform_rel", "cf_qa_whiteboard", "cf_rank", "cf_status_firefox51",
		"cf_status_firefox52", "cf_status_firefox53", "cf_status_firefox54", "cf_status_firefox55",
		"cf_status_firefox55", "cf_status_firefox_esr45", "cf_status_firefox_esr52", "cf_status_thunderbird_esr45",
		"cf_status_thunderbird_esr52", "cf_tracking_firefox51", "cf_tracking_firefox52", "cf_tracking_firefox53",
		"cf_tracking_firefox54", "cf_tracking_firefox_esr45", "cf_tracking_firefox_esr52",
		"cf_tracking_firefox_relnote", "cf_tracking_thunderbird_esr45", "cf_tracking_thunderbird_esr52",
		"cf_user_story", "classification", "comment_count", "component", "creation_time", "creator", "creator_detail",
		"depends_on", "dupe_of", "flags", "groups", "id", "is_cc_accessible", "is_confirmed", "is_creator_accessible",
		"is_open", "keywords", "last_change_time", "mentors", "mentors_detail", "op_sys", "platform", "priority",
		"product", "qa_contact", "resolution", "see_also", "severity", "status", "summary", "target_milestone", "url",
		"version", "votes", "whiteboard" })
public class Report {

	@JsonProperty("alias")
	private Object alias;
	@JsonProperty("assigned_to")
	private String assigned_to;
	@JsonProperty("assigned_to_detail")
	private AssignedToDetail assigned_to_detail;
	@JsonProperty("blocks")
	private List<Integer> blocks = null;
	@JsonProperty("cc")
	private List<String> cc = null;
	@JsonProperty("cc_detail")
	private List<CcDetail> cc_detail = null;
	@JsonProperty("cf_blocking_fennec")
	private String cf_blocking_fennec;
	@JsonProperty("cf_blocking_fx")
	private String cf_blocking_fx;
	@JsonProperty("cf_crash_signature")
	private String cf_crash_signature;
	@JsonProperty("cf_fx_iteration")
	private String cf_fx_iteration;
	@JsonProperty("cf_fx_points")
	private String cf_fx_points;
	@JsonProperty("cf_has_regression_range")
	private String cf_has_regression_range;
	@JsonProperty("cf_has_str")
	private String cf_has_str;
	@JsonProperty("cf_last_resolved")
	private String cf_last_resolved;
	@JsonProperty("cf_platform_rel")
	private String cf_platform_rel;
	@JsonProperty("cf_qa_whiteboard")
	private String cf_qa_whiteboard;
	@JsonProperty("cf_rank")
	private Object cf_rank;
	@JsonProperty("cf_status_firefox51")
	private String cf_status_firefox51;
	@JsonProperty("cf_status_firefox52")
	private String cf_status_firefox52;
	@JsonProperty("cf_status_firefox53")
	private String cf_status_firefox53;
	@JsonProperty("cf_status_firefox54")
	private String cf_status_firefox54;
	@JsonProperty("cf_status_firefox55")
	private String cf_status_firefox55;
	@JsonProperty("cf_status_firefox_esr45")
	private String cf_status_firefox_esr45;
	@JsonProperty("cf_status_firefox_esr52")
	private String cf_status_firefox_esr52;
	@JsonProperty("cf_status_thunderbird_esr45")
	private String cf_status_thunderbird_esr45;
	@JsonProperty("cf_status_thunderbird_esr52")
	private String cf_status_thunderbird_esr52;
	@JsonProperty("cf_tracking_firefox51")
	private String cf_tracking_firefox51;
	@JsonProperty("cf_tracking_firefox52")
	private String cf_tracking_firefox52;
	@JsonProperty("cf_tracking_firefox53")
	private String cf_tracking_firefox53;
	@JsonProperty("cf_tracking_firefox54")
	private String cf_tracking_firefox54;
	@JsonProperty("cf_tracking_firefox_esr45")
	private String cf_tracking_firefox_esr45;
	@JsonProperty("cf_tracking_firefox_esr52")
	private String cf_tracking_firefox_esr52;
	@JsonProperty("cf_tracking_firefox_relnote")
	private String cf_tracking_firefox_relnote;
	@JsonProperty("cf_tracking_thunderbird_esr45")
	private String cf_tracking_thunderbird_esr45;
	@JsonProperty("cf_tracking_thunderbird_esr52")
	private String cf_tracking_thunderbird_esr52;
	@JsonProperty("cf_user_story")
	private String cf_user_story;
	@JsonProperty("classification")
	private String classification;
	@JsonProperty("comment_count")
	private Integer comment_count;
	@JsonProperty("component")
	private String component;
	@JsonProperty("creation_time")
	private String creation_time;
	@JsonProperty("creator")
	private String creator;
	@JsonProperty("creator_detail")
	private CreatorDetail creator_detail;
	@JsonProperty("depends_on")
	private List<Object> depends_on = null;
	@JsonProperty("dupe_of")
	private Integer dupe_of;
	@JsonProperty("flags")
	private List<Object> flags = null;
	@JsonProperty("groups")
	private List<Object> groups = null;
	@JsonProperty("id")
	private Integer id;
	@JsonProperty("is_cc_accessible")
	private Boolean is_cc_accessible;
	@JsonProperty("is_confirmed")
	private Boolean is_confirmed;
	@JsonProperty("is_creator_accessible")
	private Boolean is_creator_accessible;
	@JsonProperty("is_open")
	private Boolean is_open;
	@JsonProperty("keywords")
	private List<String> keywords = null;
	@JsonProperty("last_change_time")
	private String last_change_time;
	@JsonProperty("mentors")
	private List<Object> mentors = null;
	@JsonProperty("mentors_detail")
	private List<Object> mentors_detail = null;
	@JsonProperty("op_sys")
	private String op_sys;
	@JsonProperty("platform")
	private String platform;
	@JsonProperty("priority")
	private String priority;
	@JsonProperty("product")
	private String product;
	@JsonProperty("qa_contact")
	private String qa_contact;
	@JsonProperty("resolution")
	private String resolution;
	@JsonProperty("see_also")
	private List<Object> see_also = null;
	@JsonProperty("severity")
	private String severity;
	@JsonProperty("status")
	private String status;
	@JsonProperty("summary")
	private String summary;
	@JsonProperty("target_milestone")
	private String target_milestone;
	@JsonProperty("url")
	private String url;
	@JsonProperty("version")
	private String version;
	@JsonProperty("votes")
	private Integer votes;
	@JsonProperty("whiteboard")
	private String whiteboard;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	public Object getAlias() {
		return alias;
	}

	public void setAlias(Object alias) {
		this.alias = alias;
	}

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

	public List<Integer> getBlocks() {
		return blocks;
	}

	public void setBlocks(List<Integer> blocks) {
		this.blocks = blocks;
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

	public String getCf_blocking_fennec() {
		return cf_blocking_fennec;
	}

	public void setCf_blocking_fennec(String cf_blocking_fennec) {
		this.cf_blocking_fennec = cf_blocking_fennec;
	}

	public String getCf_blocking_fx() {
		return cf_blocking_fx;
	}

	public void setCf_blocking_fx(String cf_blocking_fx) {
		this.cf_blocking_fx = cf_blocking_fx;
	}

	public String getCf_crash_signature() {
		return cf_crash_signature;
	}

	public void setCf_crash_signature(String cf_crash_signature) {
		this.cf_crash_signature = cf_crash_signature;
	}

	public String getCf_fx_iteration() {
		return cf_fx_iteration;
	}

	public void setCf_fx_iteration(String cf_fx_iteration) {
		this.cf_fx_iteration = cf_fx_iteration;
	}

	public String getCf_fx_points() {
		return cf_fx_points;
	}

	public void setCf_fx_points(String cf_fx_points) {
		this.cf_fx_points = cf_fx_points;
	}

	public String getCf_has_regression_range() {
		return cf_has_regression_range;
	}

	public void setCf_has_regression_range(String cf_has_regression_range) {
		this.cf_has_regression_range = cf_has_regression_range;
	}

	public String getCf_has_str() {
		return cf_has_str;
	}

	public void setCf_has_str(String cf_has_str) {
		this.cf_has_str = cf_has_str;
	}

	public String getCf_last_resolved() {
		return cf_last_resolved;
	}

	public void setCf_last_resolved(String cf_last_resolved) {
		this.cf_last_resolved = cf_last_resolved;
	}

	public String getCf_platform_rel() {
		return cf_platform_rel;
	}

	public void setCf_platform_rel(String cf_platform_rel) {
		this.cf_platform_rel = cf_platform_rel;
	}

	public String getCf_qa_whiteboard() {
		return cf_qa_whiteboard;
	}

	public void setCf_qa_whiteboard(String cf_qa_whiteboard) {
		this.cf_qa_whiteboard = cf_qa_whiteboard;
	}

	public Object getCf_rank() {
		return cf_rank;
	}

	public void setCf_rank(Object cf_rank) {
		this.cf_rank = cf_rank;
	}

	public String getCf_status_firefox51() {
		return cf_status_firefox51;
	}

	public void setCf_status_firefox51(String cf_status_firefox51) {
		this.cf_status_firefox51 = cf_status_firefox51;
	}

	public String getCf_status_firefox52() {
		return cf_status_firefox52;
	}

	public void setCf_status_firefox52(String cf_status_firefox52) {
		this.cf_status_firefox52 = cf_status_firefox52;
	}

	public String getCf_status_firefox53() {
		return cf_status_firefox53;
	}

	public void setCf_status_firefox53(String cf_status_firefox53) {
		this.cf_status_firefox53 = cf_status_firefox53;
	}

	public String getCf_status_firefox54() {
		return cf_status_firefox54;
	}

	public String getCf_status_firefox55() {
		return cf_status_firefox55;
	}

	public void setCf_status_firefox54(String cf_status_firefox54) {
		this.cf_status_firefox54 = cf_status_firefox54;
	}

	public void setCf_status_firefox55(String cf_status_firefox55) {
		this.cf_status_firefox55 = cf_status_firefox55;
	}

	public String getCf_status_firefox_esr45() {
		return cf_status_firefox_esr45;
	}

	public void setCf_status_firefox_esr45(String cf_status_firefox_esr45) {
		this.cf_status_firefox_esr45 = cf_status_firefox_esr45;
	}

	public String getCf_status_firefox_esr52() {
		return cf_status_firefox_esr52;
	}

	public void setCf_status_firefox_esr52(String cf_status_firefox_esr52) {
		this.cf_status_firefox_esr52 = cf_status_firefox_esr52;
	}

	public String getCf_status_thunderbird_esr45() {
		return cf_status_thunderbird_esr45;
	}

	public void setCf_status_thunderbird_esr45(String cf_status_thunderbird_esr45) {
		this.cf_status_thunderbird_esr45 = cf_status_thunderbird_esr45;
	}

	public String getCf_status_thunderbird_esr52() {
		return cf_status_thunderbird_esr52;
	}

	public void setCf_status_thunderbird_esr52(String cf_status_thunderbird_esr52) {
		this.cf_status_thunderbird_esr52 = cf_status_thunderbird_esr52;
	}

	public String getCf_tracking_firefox51() {
		return cf_tracking_firefox51;
	}

	public void setCf_tracking_firefox51(String cf_tracking_firefox51) {
		this.cf_tracking_firefox51 = cf_tracking_firefox51;
	}

	public String getCf_tracking_firefox52() {
		return cf_tracking_firefox52;
	}

	public void setCf_tracking_firefox52(String cf_tracking_firefox52) {
		this.cf_tracking_firefox52 = cf_tracking_firefox52;
	}

	public String getCf_tracking_firefox53() {
		return cf_tracking_firefox53;
	}

	public void setCf_tracking_firefox53(String cf_tracking_firefox53) {
		this.cf_tracking_firefox53 = cf_tracking_firefox53;
	}

	public String getCf_tracking_firefox54() {
		return cf_tracking_firefox54;
	}

	public void setCf_tracking_firefox54(String cf_tracking_firefox54) {
		this.cf_tracking_firefox54 = cf_tracking_firefox54;
	}

	public String getCf_tracking_firefox_esr45() {
		return cf_tracking_firefox_esr45;
	}

	public void setCf_tracking_firefox_esr45(String cf_tracking_firefox_esr45) {
		this.cf_tracking_firefox_esr45 = cf_tracking_firefox_esr45;
	}

	public String getCf_tracking_firefox_esr52() {
		return cf_tracking_firefox_esr52;
	}

	public void setCf_tracking_firefox_esr52(String cf_tracking_firefox_esr52) {
		this.cf_tracking_firefox_esr52 = cf_tracking_firefox_esr52;
	}

	public String getCf_tracking_firefox_relnote() {
		return cf_tracking_firefox_relnote;
	}

	public void setCf_tracking_firefox_relnote(String cf_tracking_firefox_relnote) {
		this.cf_tracking_firefox_relnote = cf_tracking_firefox_relnote;
	}

	public String getCf_tracking_thunderbird_esr45() {
		return cf_tracking_thunderbird_esr45;
	}

	public void setCf_tracking_thunderbird_esr45(String cf_tracking_thunderbird_esr45) {
		this.cf_tracking_thunderbird_esr45 = cf_tracking_thunderbird_esr45;
	}

	public String getCf_tracking_thunderbird_esr52() {
		return cf_tracking_thunderbird_esr52;
	}

	public void setCf_tracking_thunderbird_esr52(String cf_tracking_thunderbird_esr52) {
		this.cf_tracking_thunderbird_esr52 = cf_tracking_thunderbird_esr52;
	}

	public String getCf_user_story() {
		return cf_user_story;
	}

	public void setCf_user_story(String cf_user_story) {
		this.cf_user_story = cf_user_story;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public Integer getComment_count() {
		return comment_count;
	}

	public void setComment_count(Integer comment_count) {
		this.comment_count = comment_count;
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

	public List<Object> getDepends_on() {
		return depends_on;
	}

	public void setDepends_on(List<Object> depends_on) {
		this.depends_on = depends_on;
	}

	public Integer getDupe_of() {
		return dupe_of;
	}

	public void setDupe_of(Integer dupe_of) {
		this.dupe_of = dupe_of;
	}

	public List<Object> getFlags() {
		return flags;
	}

	public void setFlags(List<Object> flags) {
		this.flags = flags;
	}

	public List<Object> getGroups() {
		return groups;
	}

	public void setGroups(List<Object> groups) {
		this.groups = groups;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getIs_cc_accessible() {
		return is_cc_accessible;
	}

	public void setIs_cc_accessible(Boolean is_cc_accessible) {
		this.is_cc_accessible = is_cc_accessible;
	}

	public Boolean getIs_confirmed() {
		return is_confirmed;
	}

	public void setIs_confirmed(Boolean is_confirmed) {
		this.is_confirmed = is_confirmed;
	}

	public Boolean getIs_creator_accessible() {
		return is_creator_accessible;
	}

	public void setIs_creator_accessible(Boolean is_creator_accessible) {
		this.is_creator_accessible = is_creator_accessible;
	}

	public Boolean getIs_open() {
		return is_open;
	}

	public void setIs_open(Boolean is_open) {
		this.is_open = is_open;
	}

	public List<String> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<String> keywords) {
		this.keywords = keywords;
	}

	public String getLast_change_time() {
		return last_change_time;
	}

	public void setLast_change_time(String last_change_time) {
		this.last_change_time = last_change_time;
	}

	public List<Object> getMentors() {
		return mentors;
	}

	public void setMentors(List<Object> mentors) {
		this.mentors = mentors;
	}

	public List<Object> getMentors_detail() {
		return mentors_detail;
	}

	public void setMentors_detail(List<Object> mentors_detail) {
		this.mentors_detail = mentors_detail;
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getQa_contact() {
		return qa_contact;
	}

	public void setQa_contact(String qa_contact) {
		this.qa_contact = qa_contact;
	}

	public String getResolution() {
		return resolution;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public List<Object> getSee_also() {
		return see_also;
	}

	public void setSee_also(List<Object> see_also) {
		this.see_also = see_also;
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

	public String getTarget_milestone() {
		return target_milestone;
	}

	public void setTarget_milestone(String target_milestone) {
		this.target_milestone = target_milestone;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Integer getVotes() {
		return votes;
	}

	public void setVotes(Integer votes) {
		this.votes = votes;
	}

	public String getWhiteboard() {
		return whiteboard;
	}

	public void setWhiteboard(String whiteboard) {
		this.whiteboard = whiteboard;
	}

	public Map<String, Object> getAdditionalProperties() {
		return additionalProperties;
	}

	public void setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
	}

	@Override
	public String toString() {
		return "Bug [alias=" + alias + ", assigned_to=" + assigned_to + ", assignedToDetail=" + assigned_to_detail
				+ ", blocks=" + blocks + ", cc=" + cc + ", cc_detail=" + cc_detail + ", cf_blocking_fennec="
				+ cf_blocking_fennec + ", cf_blocking_fx=" + cf_blocking_fx + ", cf_crash_signature="
				+ cf_crash_signature + ", cf_fx_iteration=" + cf_fx_iteration + ", cf_fx_points=" + cf_fx_points
				+ ", cf_has_regression_range=" + cf_has_regression_range + ", cf_has_str=" + cf_has_str
				+ ", cf_last_resolved=" + cf_last_resolved + ", cf_platform_rel=" + cf_platform_rel
				+ ", cf_qa_whiteboard=" + cf_qa_whiteboard + ", cf_rank=" + cf_rank + ", cf_status_firefox51="
				+ cf_status_firefox51 + ", cf_status_firefox52=" + cf_status_firefox52 + ", cf_status_firefox53="
				+ cf_status_firefox53 + ", cf_status_firefox54=" + cf_status_firefox54 + ", cf_status_firefox55="
				+ cf_status_firefox55 + "," + "cf_status_firefox_esr45=" + cf_status_firefox_esr45
				+ ", cf_status_firefox_esr52=" + cf_status_firefox_esr52 + ", cf_status_thunderbird_esr45="
				+ cf_status_thunderbird_esr45 + ", cf_status_thunderbird_esr52=" + cf_status_thunderbird_esr52
				+ ", cf_tracking_firefox51=" + cf_tracking_firefox51 + ", cf_tracking_firefox52="
				+ cf_tracking_firefox52 + ", cf_tracking_firefox53=" + cf_tracking_firefox53
				+ ", cf_tracking_firefox54=" + cf_tracking_firefox54 + ", cf_tracking_firefox_esr45="
				+ cf_tracking_firefox_esr45 + ", cf_tracking_firefox_esr52=" + cf_tracking_firefox_esr52
				+ ", cf_tracking_firefox_relnote=" + cf_tracking_firefox_relnote + ", cf_tracking_thunderbird_esr45="
				+ cf_tracking_thunderbird_esr45 + ", cf_tracking_thunderbird_esr52=" + cf_tracking_thunderbird_esr52
				+ ", cf_user_story=" + cf_user_story + ", classification=" + classification + ", comment_count="
				+ comment_count + ", component=" + component + ", creation_time=" + creation_time + ", creator="
				+ creator + ", creator_detail=" + creator_detail + ", depends_on=" + depends_on + ", dupe_of=" + dupe_of
				+ ", flags=" + flags + ", groups=" + groups + ", id=" + id + ", is_cc_accessible=" + is_cc_accessible
				+ ", is_confirmed=" + is_confirmed + ", is_creator_accessible=" + is_creator_accessible + ", is_open="
				+ is_open + ", keywords=" + keywords + ", last_change_time=" + last_change_time + ", mentors=" + mentors
				+ ", mentors_detail=" + mentors_detail + ", op_sys=" + op_sys + ", platform=" + platform + ", priority="
				+ priority + ", product=" + product + ", qa_contact=" + qa_contact + ", resolution=" + resolution
				+ ", see_also=" + see_also + ", severity=" + severity + ", status=" + status + ", summary=" + summary
				+ ", target_milestone=" + target_milestone + ", url=" + url + ", version=" + version + ", votes="
				+ votes + ", whiteboard=" + whiteboard + ", additionalProperties=" + additionalProperties + "]";
	}

}
