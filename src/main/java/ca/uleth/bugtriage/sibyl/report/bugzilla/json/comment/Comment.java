
package ca.uleth.bugtriage.sibyl.report.bugzilla.json.comment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "attachment_id",
    "author",
    "bug_id",
    "count",
    "creation_time",
    "creator",
    "id",
    "is_private",
    "raw_text",
    "tags",
    "text",
    "time"
})

@JsonIgnoreProperties(ignoreUnknown=true)

public class Comment {

    @JsonProperty("attachment_id")
    private Object attachmentId;
    @JsonProperty("author")
    private String author;
    @JsonProperty("bug_id")
    private Integer bugId;
    @JsonProperty("count")
    private Integer count;
    @JsonProperty("creation_time")
    private String creationTime;
    @JsonProperty("creator")
    private String creator;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("is_private")
    private Boolean isPrivate;
    @JsonProperty("raw_text")
    private String rawText;
    @JsonProperty("tags")
    private List<Object> tags = null;
    @JsonProperty("text")
    private String text;
    @JsonProperty("time")
    private String time;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("attachment_id")
    public Object getAttachmentId() {
        return attachmentId;
    }

    @JsonProperty("attachment_id")
    public void setAttachmentId(Object attachmentId) {
        this.attachmentId = attachmentId;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    @JsonProperty("author")
    public void setAuthor(String author) {
        this.author = author;
    }

    @JsonProperty("bug_id")
    public Integer getBugId() {
        return bugId;
    }

    @JsonProperty("bug_id")
    public void setBugId(Integer bugId) {
        this.bugId = bugId;
    }

    @JsonProperty("count")
    public Integer getCount() {
        return count;
    }

    @JsonProperty("count")
    public void setCount(Integer count) {
        this.count = count;
    }

    @JsonProperty("creation_time")
    public String getCreationTime() {
        return creationTime;
    }

    @JsonProperty("creation_time")
    public void setCreationTime(String creationTime) {
        this.creationTime = creationTime;
    }

    @JsonProperty("creator")
    public String getCreator() {
        return creator;
    }

    @JsonProperty("creator")
    public void setCreator(String creator) {
        this.creator = creator;
    }

    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    @JsonProperty("is_private")
    public Boolean getIsPrivate() {
        return isPrivate;
    }

    @JsonProperty("is_private")
    public void setIsPrivate(Boolean isPrivate) {
        this.isPrivate = isPrivate;
    }

    @JsonProperty("raw_text")
    public String getRawText() {
        return rawText;
    }

    @JsonProperty("raw_text")
    public void setRawText(String rawText) {
        this.rawText = rawText;
    }

    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    @JsonProperty("text")
    public String getText() {
        return text;
    }

    @JsonProperty("text")
    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("time")
    public String getTime() {
        return time;
    }

    @JsonProperty("time")
    public void setTime(String time) {
        this.time = time;
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
