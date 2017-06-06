
package ca.uleth.bugtriage.sibyl.report.bugzilla.json.history;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bugs"
})
public class BugReportHistoryBugzilla {

    @JsonProperty("bugs")
    private List<BugHistory> bugs = null;

    @JsonProperty("bugs")
    public List<BugHistory> getBugs() {
        return bugs;
    }

    @JsonProperty("bugs")
    public void setBugs(List<BugHistory> bugs) {
        this.bugs = bugs;
    }

}
