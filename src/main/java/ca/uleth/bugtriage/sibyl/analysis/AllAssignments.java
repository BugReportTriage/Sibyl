package ca.uleth.bugtriage.sibyl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

//import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
//import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;

public class AllAssignments extends RecommendationAnalysis {

	private RecommendationAnalysis dev, comp, subcomp, cc;

	public AllAssignments(List<ViewEvent> viewEvents, List<ChangeEvent> events) {
		super(viewEvents, events);
		this.dev = new AssignmentAnalysis(viewEvents, events);
		this.comp = new ComponentAnalysis(viewEvents, events);
		this.subcomp = new SubcomponentAnalysis(viewEvents, events);
		this.cc = new InterestAnalysis(viewEvents, events);
	}

	@Override
	public DescriptiveStatistics accuracy() {
		return new DescriptiveStatistics();
	}

	@Override
	protected String assignment(ChangeEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<BugActivityEvent> getAssignmentEvents(BugActivity activity) {
		List<BugActivityEvent> events = new ArrayList<BugActivityEvent>();
		events.addAll(dev.getAssignmentEvents(activity));
		events.addAll(comp.getAssignmentEvents(activity));
		events.addAll(subcomp.getAssignmentEvents(activity));
		events.addAll(cc.getAssignmentEvents(activity));
		
		List<String> triagers = new ArrayList<String>();
		triagers.add("pwebster@ca.ibm.com");
		triagers.add("kim_horne@ca.ibm.com");
		triagers.add("Tod_Creasey@ca.ibm.com");
		triagers.add("Boris_Bokowski@ca.ibm.com");
		
		List<BugActivityEvent> cleanedEvents = new ArrayList<BugActivityEvent>();
		for (BugActivityEvent bugActivityEvent : events) {
			if(triagers.contains(bugActivityEvent.getName()))
				cleanedEvents.add(bugActivityEvent);
		}
		
		return cleanedEvents;
	}

	@Override
	protected TriageClassifier getClassifier(Project project)
			throws ClassifierNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected List<String> getRecommendations(ChangeEvent event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected int rank(ChangeEvent event) {
		// TODO Auto-generated method stub
		return 0;
	}

}
