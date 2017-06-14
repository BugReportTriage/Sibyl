package ca.uleth.bugtriage.sibyl.analysis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.AssignmentEvent;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;

public class AssignmentAnalysis extends RecommendationAnalysis {

	public AssignmentAnalysis(List<ViewEvent> viewEvents, List<ChangeEvent> events) {
		super(viewEvents, events);
	}

	@Override
	protected int rank(ChangeEvent event) {
		return event.assignmentRank();
	}

	/*
	 * Returns the number of times a cc is added that was an assignment
	 * recommendation
	 */
	public int recommendationCC() {
		int recommendationCCs = 0;
		for (ChangeEvent event : this.events) {
			String ccAdded = event.ccAdded();
			if (ccAdded != "") {
				for (String recommendation : event.assignmentRecommendations()) {
					if (ccAdded.equals(recommendation)) {
						System.out
								.println("Recommendation added to CC: " + recommendation + " --> " + event.reportId());
						recommendationCCs++;
					}
				}
			}
		}
		return recommendationCCs;
	}

	@Override
	public DescriptiveStatistics accuracy() {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		int outOfView = 0;
		int resolvedByRecommendation = 0;
		int componentChanged = 0;
		int productChanged = 0;
		int controlReports = 0;

		FrequencyTable outOfViewRanks = new FrequencyTable();

		for (ChangeEvent event : this.events) {
			if (rank(event) != 0) {

				if (event.assignmentRecommendations().size() == 1
						&& event.assignmentRecommendations().get(0).contains("Control Report")) {
					// System.out.println("---> Control report");
					controlReports++;
					continue;
				}

				if (event.assignmentRecommendations().isEmpty()) {
					continue;
				}

				if (this.correct(event.getActivityLog().getAllAssignedTo(), event.assignmentRecommendations(),
						Sibyl.NUM_DEVELOPER_RECOMMENDATIONS)) {
					stats.addValue(1);
				} else {
					if (this.correct(event.getActivityLog().getResolvers(), event.assignmentRecommendations(),
							Sibyl.NUM_DEVELOPER_RECOMMENDATIONS)) {
						resolvedByRecommendation++;
						stats.addValue(1);
						continue;
					}
					stats.addValue(0);

					if (this.correct(event.getActivityLog().getAllAssignedTo(), event.assignmentRecommendations(),
							Sibyl.LOGGED_DEVELOPER_RECOMMENDATIONS)) {
						outOfView++;
						outOfViewRanks.add("Rank = " + this.rank(event));
						continue;
					}

					if (event.component().equals("UI") == false) {
						System.out.println(
								"(Component changed) Report Id: " + event.reportId() + " --> " + event.component());
						componentChanged++;
						continue;
					}

					if (event.product().equals("Platform") == false) {
						productChanged++;
						continue;
					}
				}
			}
		}

		System.out.println("Out of View: " + outOfView);
		System.out.println(outOfViewRanks);
		System.out.println("Resolved by Recommendation: " + resolvedByRecommendation);
		System.out.println("Wrong Component: " + componentChanged);
		System.out.println("Wrong Product: " + productChanged);
		System.out.println("Control Reports: " + controlReports);

		return stats;
	}

	@Override
	protected TriageClassifier getClassifier(Project project) throws ClassifierNotFoundException {
		return project.getDeveloperClassifier();
	}

	@Override
	protected String assignment(ChangeEvent event) {
		return event.assigned();
	}

	protected List<BugActivityEvent> getAssignmentEvents(BugActivity activity) {
		List<BugActivityEvent> events = new ArrayList<BugActivityEvent>();
		for (AssignmentEvent event : activity.getAssignmentEvents()) {
			events.add(event);
		}
		return events;
	}

	public void reportedByTeam(Map<String, BugReport> reports) {
		int reportedByARecommendation = 0;
		List<String> examined = new ArrayList<String>();
		for (ChangeEvent event : this.events) {
			if (examined.contains(event.reportId()) == false) {
				if (event.assignmentRecommendations().contains(event)) {
					reportedByARecommendation++;
				}
			}
		}
		System.out.println("Reported by recommendation: " + reportedByARecommendation);
	}

	@Override
	protected List<String> getRecommendations(ChangeEvent event) {
		return event.assignmentRecommendations();
	}

}
