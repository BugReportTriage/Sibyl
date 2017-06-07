
package ca.uleth.bugtriage.sibyl.report.bugzilla.json.history;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "changes",
    "when",
    "who"
})
public class HistoryBugzilla {

    @JsonProperty("changes")
    private List<Change> changes = null;
    @JsonProperty("when")
    private String when;
    @JsonProperty("who")
    private String who;

    @JsonProperty("changes")
    public List<Change> getChanges() {
        return changes;
    }

    @JsonProperty("changes")
    public void setChanges(List<Change> changes) {
        this.changes = changes;
    }

    @JsonProperty("when")
    public String getWhen() {
        return when;
    }

    @JsonProperty("when")
    public void setWhen(String when) {
        this.when = when;
    }

    @JsonProperty("who")
    public String getWho() {
        return who;
    }

    @JsonProperty("who")
    public void setWho(String who) {
        this.who = who;
    }

}
