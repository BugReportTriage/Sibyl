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

public class ComponentAnalysis extends RecommendationAnalysis {

	public ComponentAnalysis(List<ViewEvent> viewEvents,
			List<ChangeEvent> events) {
		super(viewEvents, events);
	}

	@Override
	protected int rank(ChangeEvent event) {
		return event.componentRank();
	}

	@Override
	public DescriptiveStatistics accuracy() {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int outOfView = 0;
		int controlReports = 0;
		int ideComponent = 0;
		int productChanged = 0;
		FrequencyTable outOfViewRanks = new FrequencyTable();
		for (ChangeEvent event : this.events) {

			if (event.componentRecommendations().size() == 1
					&& event.componentRecommendations().get(0).contains(
							"Control Report")) {
				// System.out.println("---> Control report");
				controlReports++;
				continue;
			}

			if (event.componentRecommendations().isEmpty()) {
				// System.out.println("No recommendations recorded: " +
				// event.getDate());
				continue;
			}

			int rank = rank(event);
			if (rank != 0) {

				if (this.correct(event.getActivityLog().getComponentChanges(),
						event.componentRecommendations(),
						Sibyl.NUM_COMPONENT_RECOMMENDATIONS)) {
					stats.addValue(1);
				} else {
					stats.addValue(0);
					if (this.correct(event.getActivityLog()
							.getComponentChanges(), event
							.componentRecommendations(),
							Sibyl.LOGGED_COMPONENT_RECOMMENDATIONS)) {
						// System.out.println("==> Out of View <==");
						outOfView++;
						outOfViewRanks.add("Rank = " + this.rank(event));
						continue;
					}

					if (event.component().equals("IDE")) {
						// System.out.println("(Component changed) Report Id: "
						// +
						// event.reportId() + " --> " + event.component());
						ideComponent++;
						continue;
					}

					if (event.product().equals("Platform") == false) {
						// System.out.println("(Product changed) Report Id: " +
						// event.reportId() + " --> " + event.product());
						productChanged++;
						continue;
					}

					/*
					 * System.out.println("------------------------");
					 * System.out.println("Report Id: " + event.reportId());
					 * System.out.println("Recommendations: " +
					 * event.componentRecommendations());
					 * System.out.println("Component Changes: " +
					 * event.getActivityLog().getComponentChanges());
					 * System.out.println("------------------------");
					 */
				}

			}
		}

		System.out.println("Out of View: " + outOfView);
		System.out.println(outOfViewRanks);
		System.out.println("IDE Component: " + ideComponent);
		System.out.println("Wrong Product: " + productChanged);
		System.out.println("Control Reports: " + controlReports);

		return stats;
	}

	@Override
	protected TriageClassifier getClassifier(Project project)
			throws ClassifierNotFoundException {
		return project.getComponentClassifier();
	}

	@Override
	protected String assignment(ChangeEvent event) {
		return event.component();
	}

	@Override
	protected List<BugActivityEvent> getAssignmentEvents(BugActivity activity) {
		List<BugActivityEvent> events = new ArrayList<BugActivityEvent>();
		for (BugActivityEvent event : activity) {
			if (event.getWhat().equals("Component"))
				events.add(event);
		}
		return events;
	}

	@Override
	protected List<String> getRecommendations(ChangeEvent event) {
		return event.componentRecommendations();
	}
}
