
package ca.uleth.bugtriage.sibyl.report.bugzilla.json.history;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "alias",
    "history",
    "id"
})
public class BugHistory {

    @JsonProperty("alias")
    private Object alias;
    @JsonProperty("history")
    private List<HistoryBugzilla> history = null;
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("alias")
    public Object getAlias() {
        return alias;
    }

    @JsonProperty("alias")
    public void setAlias(Object alias) {
        this.alias = alias;
    }

    @JsonProperty("history")
    public List<HistoryBugzilla> getHistory() {
        return history;
    }

    @JsonProperty("history")
    public void setHistory(List<HistoryBugzilla> history) {
        this.history = history;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

}
