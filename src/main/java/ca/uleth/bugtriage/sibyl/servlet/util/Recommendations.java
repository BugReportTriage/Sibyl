package ca.uleth.bugtriage.sibyl.servlet.util;

import java.util.List;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.ComponentClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class Recommendations {

	private final BugReport report;

	private final Project project;

	public Recommendations(BugReport report, Project project) {
		this.report = report;
		this.project = project;
	}

	public List<Classification> getDeveloperRecommendations(String component)
			throws ClassifierNotFoundException {
		if(component != null)
			return getRecommendations(this.project.getDeveloperClassifier(), component);
		return getRecommendations(this.project.getDeveloperClassifier());
	}

	private List<Classification> getRecommendations(TriageClassifier developerClassifier, String component) {
		if(developerClassifier instanceof ComponentClassifier){
			ComponentClassifier classifier = (ComponentClassifier)developerClassifier;
			return classifier.classify(this.report, component);
		}
		
		return this.getRecommendations(developerClassifier);
	}

	public List<Classification> getComponentRecommendations()
			throws ClassifierNotFoundException {

		return getRecommendations(this.project.getComponentClassifier());
	}

	private List<Classification> getRecommendations(TriageClassifier classifier) {
		return classifier.classify(this.report);
	}

	public List<Classification> getSubcomponentRecommendations() throws ClassifierNotFoundException {
		return getRecommendations(this.project.getSubcomponentClassifier());
	}

	public List<Classification> getCCRecommendations() throws ClassifierNotFoundException {
		return getRecommendations(this.project.getCCClassifier());
	}
}
