package ca.uleth.bugtriage.sibyl.analysis;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;

public class SubcomponentAnalysis extends RecommendationAnalysis {

	public SubcomponentAnalysis(List<ViewEvent> viewEvents,
			List<ChangeEvent> events) {
		super(viewEvents, events);
	}

	@Override
	protected int rank(ChangeEvent event) {
		return event.subcomponentRank();
	}

	@Override
	public DescriptiveStatistics accuracy() {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		
		int outOfView = 0;
		int controlReports = 0;
		int productChanged = 0;
		int componentChanged = 0;
		FrequencyTable outOfViewRanks = new FrequencyTable();
		
		for (ChangeEvent event : this.events) {
			
			if (event.componentRecommendations().size() == 1
					&& event.componentRecommendations().get(0).contains(
							"Control Report")) {
				// System.out.println("---> Control report");
				controlReports++;
				continue;
			}
			
			if (event.subcomponentRecommendations().isEmpty()) {
				// System.out.println("No recommendations recorded: " +
				// event.getDate());
				continue;
			}
			
			int rank = rank(event);
			if (rank != 0) {
				if (this.correct(event.getActivityLog()
						.getSubcomponentChanges(), event
						.subcomponentRecommendations(),
						Sibyl.NUM_SUBCOMPONENT_RECOMMENDATIONS)) {
					stats.addValue(1);
				} else {
					stats.addValue(0);
					if (this.correct(event.getActivityLog().getSubcomponentChanges(),
							event.subcomponentRecommendations(),
							Sibyl.LOGGED_SUBCOMPONENT_RECOMMENDATIONS)) {
						// System.out.println("==> Out of View <==");
						outOfView++;
						outOfViewRanks.add("Rank = " + this.rank(event));
						continue;
					}
			
					if (event.product().equals("Platform") == false) {
						// System.out.println("(Product changed) Report Id: " +
						// event.reportId() + " --> " + event.product());
						productChanged++;
						continue;
					}
					
					if (event.component().equals("UI") == false) {
						// System.out.println("(Component changed) Report Id: " +
						// event.reportId() + " --> " + event.component());
						componentChanged++;
						continue;
					}
/*
					System.out.println("------------------------");
					System.out.println("Report Id: " + event.reportId());
					System.out.println("Recommendations: "
							+ event.subcomponentRecommendations());
					System.out.println("Component Changes: "
							+ event.getActivityLog().getSubcomponentChanges());
					System.out.println("------------------------");
					*/
				}
			}
		}
		
		System.out.println("Out of View: " + outOfView);
		System.out.println(outOfViewRanks);
		System.out.println("Wrong Product: " + productChanged);
		System.out.println("Wrong Component: " + componentChanged);
		System.out.println("Control Reports: " + controlReports);
		
		return stats;
	}

	@Override
	// Overriding since we need to more complicated compare
	public boolean correct(List<String> assignments,
			List<String> recommendations, int recommendationsGiven) {
		String recommendation;
		int numRecommendations = Math.min(recommendationsGiven, recommendations
				.size());
		for (int rank = 0; rank < numRecommendations; rank++) {
			recommendation = recommendations.get(rank);
			for (String assignment : assignments) {
				if (recommendation.replace(" ", "").toLowerCase().equals(
						assignment.replace(" ", "").toLowerCase())) {
					return true;
				}
			}

		}
		return false;
	}

	@Override
	protected TriageClassifier getClassifier(Project project) throws ClassifierNotFoundException {
		return project.getSubcomponentClassifier();
	}

	@Override
	protected String assignment(ChangeEvent event) {
		List<String> subcomponents = event.getActivityLog()
				.getSubcomponentChanges();
		if (!subcomponents.isEmpty())
			return subcomponents.get(subcomponents.size() - 1);
		return "<No subcomponent>";
	}

	@Override
	protected List<BugActivityEvent> getAssignmentEvents(BugActivity activity) {
		List<BugActivityEvent> events = new ArrayList<BugActivityEvent>();
		for(BugActivityEvent event : activity) {
			if(event.getWhat().equals("Summary") && event.getRemoved().startsWith("[") == false && event.getAdded().startsWith("[") == true)
				events.add(event);
		}
		return events;
	}

	@Override
	protected List<String> getRecommendations(ChangeEvent event) {
	return event.subcomponentRecommendations();
	}
}
