
package ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.JsonNode;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)

public class Bugs {

    private Map<String, JsonNode> report = new HashMap<String, JsonNode>();

    @JsonAnyGetter
    public Map<String, JsonNode> getReport() {
        return this.report;
    }

    @JsonAnySetter
    public void setReport(String name, JsonNode value) {
        this.report.put(name, value);
    }

}
