package restservices.repositoryBean;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "frequencyCutoff"
})
@XmlRootElement
public class TriageRecommender {
	private Map<String, Integer> frequencyCutoff;
	
	private Map<String, Integer>  resolutionGroup;

	private List<ResolutionType> resolutionTypes;
	
	private int totalBugReport;
	public Map<String, Integer> getFrequencyCutoff() {
		return frequencyCutoff;
	}

	public void setFrequencyCutoff(Map<String, Integer> frequencyCutoff) {
		this.frequencyCutoff = frequencyCutoff;
	}

	public Map<String, Integer> getResolutionGroup() {
		return resolutionGroup;
	}

	public void setResolutionGroup(Map<String, Integer> resolutionGroup) {
		this.resolutionGroup = resolutionGroup;
	}

	public List<ResolutionType> getResolutionTypes() {
		return resolutionTypes;
	}

	public void setResolutionTypes(List<ResolutionType> resolutionTypes) {
		this.resolutionTypes = resolutionTypes;
	}

	public int getTotalBugReport() {
		return totalBugReport;
	}

	public void setTotalBugReport(int totalBugReport) {
		this.totalBugReport = totalBugReport;
	}
	
}
