
package ca.uleth.bugtriage.sibyl.report.bugzilla.json.history;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "added",
    "attachment_id",
    "field_name",
    "removed"
})
@JsonIgnoreProperties(ignoreUnknown=true)

public class ChangeBugzilla {

    @JsonProperty("added")
    private String added;    
    @JsonProperty("attachment_id")    
    private String attachmentId;
    @JsonProperty("field_name")
    private String fieldName;
    @JsonProperty("removed")
    private String removed;

    @JsonProperty("added")
    public String getAdded() {
        return added;
    }

    @JsonProperty("added")
    public void setAdded(String added) {
        this.added = added;
    }
    
    @JsonProperty("attachment_id")    
    public String getAttachmentId() {
        return attachmentId;
    }    
    @JsonProperty("attachment_id")    
    public void setAttachmentId(String attachmentId) {
        this.attachmentId = attachmentId;
    }

    @JsonProperty("field_name")
    public String getFieldName() {
        return fieldName;
    }

    @JsonProperty("field_name")
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    @JsonProperty("removed")
    public String getRemoved() {
        return removed;
    }

    @JsonProperty("removed")
    public void setRemoved(String removed) {
        this.removed = removed;
    }

}
