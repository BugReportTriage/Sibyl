
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
    "assignmentEvents",
    "resolution",
    "approvedAttachments",
    "mostFrequentAttachmentSubmitter",
    "allAssignedTo",
    "whoSetStatus",
    "subcomponentChanges",
    "fixers",
    "resolvers",
    "ccadded",
    "componentChanges"
})
public class Activity {

    @JsonProperty("assignmentEvents")
    private List<Object> assignmentEvents = null;
    @JsonProperty("resolution")
    private Object resolution;
    @JsonProperty("approvedAttachments")
    private List<Object> approvedAttachments = null;
    @JsonProperty("mostFrequentAttachmentSubmitter")
    private String mostFrequentAttachmentSubmitter;
    @JsonProperty("allAssignedTo")
    private List<Object> allAssignedTo = null;
    @JsonProperty("whoSetStatus")
    private Object whoSetStatus;
    @JsonProperty("subcomponentChanges")
    private List<Object> subcomponentChanges = null;
    @JsonProperty("fixers")
    private List<Object> fixers = null;
    @JsonProperty("resolvers")
    private List<Object> resolvers = null;
    @JsonProperty("ccadded")
    private List<Object> ccadded = null;
    @JsonProperty("componentChanges")
    private List<Object> componentChanges = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("assignmentEvents")
    public List<Object> getAssignmentEvents() {
        return assignmentEvents;
    }

    @JsonProperty("assignmentEvents")
    public void setAssignmentEvents(List<Object> assignmentEvents) {
        this.assignmentEvents = assignmentEvents;
    }

    @JsonProperty("resolution")
    public Object getResolution() {
        return resolution;
    }

    @JsonProperty("resolution")
    public void setResolution(Object resolution) {
        this.resolution = resolution;
    }

    @JsonProperty("approvedAttachments")
    public List<Object> getApprovedAttachments() {
        return approvedAttachments;
    }

    @JsonProperty("approvedAttachments")
    public void setApprovedAttachments(List<Object> approvedAttachments) {
        this.approvedAttachments = approvedAttachments;
    }

    @JsonProperty("mostFrequentAttachmentSubmitter")
    public String getMostFrequentAttachmentSubmitter() {
        return mostFrequentAttachmentSubmitter;
    }

    @JsonProperty("mostFrequentAttachmentSubmitter")
    public void setMostFrequentAttachmentSubmitter(String mostFrequentAttachmentSubmitter) {
        this.mostFrequentAttachmentSubmitter = mostFrequentAttachmentSubmitter;
    }

    @JsonProperty("allAssignedTo")
    public List<Object> getAllAssignedTo() {
        return allAssignedTo;
    }

    @JsonProperty("allAssignedTo")
    public void setAllAssignedTo(List<Object> allAssignedTo) {
        this.allAssignedTo = allAssignedTo;
    }

    @JsonProperty("whoSetStatus")
    public Object getWhoSetStatus() {
        return whoSetStatus;
    }

    @JsonProperty("whoSetStatus")
    public void setWhoSetStatus(Object whoSetStatus) {
        this.whoSetStatus = whoSetStatus;
    }

    @JsonProperty("subcomponentChanges")
    public List<Object> getSubcomponentChanges() {
        return subcomponentChanges;
    }

    @JsonProperty("subcomponentChanges")
    public void setSubcomponentChanges(List<Object> subcomponentChanges) {
        this.subcomponentChanges = subcomponentChanges;
    }

    @JsonProperty("fixers")
    public List<Object> getFixers() {
        return fixers;
    }

    @JsonProperty("fixers")
    public void setFixers(List<Object> fixers) {
        this.fixers = fixers;
    }

    @JsonProperty("resolvers")
    public List<Object> getResolvers() {
        return resolvers;
    }

    @JsonProperty("resolvers")
    public void setResolvers(List<Object> resolvers) {
        this.resolvers = resolvers;
    }

    @JsonProperty("ccadded")
    public List<Object> getCcadded() {
        return ccadded;
    }

    @JsonProperty("ccadded")
    public void setCcadded(List<Object> ccadded) {
        this.ccadded = ccadded;
    }

    @JsonProperty("componentChanges")
    public List<Object> getComponentChanges() {
        return componentChanges;
    }

    @JsonProperty("componentChanges")
    public void setComponentChanges(List<Object> componentChanges) {
        this.componentChanges = componentChanges;
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
