
package ca.uleth.bugtriage.sibyl.report.bugzilla.json;

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
    "bugs",
    "faults"
})
 
public class BugReports {

    @JsonProperty("bugs")
    private List<Report> bugs = null;
    @JsonProperty("faults")
    private List<Object> faults = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("bugs")
    public List<Report> getBugs() {
        return bugs;
    }

    @JsonProperty("bugs")
    public void setBugs(List<Report> bugs) {
        this.bugs = bugs;
    }

    @JsonProperty("faults")
    public List<Object> getFaults() {
        return faults;
    }

    @JsonProperty("faults")
    public void setFaults(List<Object> faults) {
        this.faults = faults;
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
