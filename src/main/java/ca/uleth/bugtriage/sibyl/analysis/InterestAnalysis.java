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

public class InterestAnalysis extends RecommendationAnalysis {

	public InterestAnalysis(List<ViewEvent> viewEvents, List<ChangeEvent> events) {
		super(viewEvents, events);
	}

	@Override
	public DescriptiveStatistics accuracy() {

		List<String> topNames = new ArrayList<String>();
		topNames.add("steve_northover@ca.ibm.com");
		topNames.add("Tod_Creasey@ca.ibm.com");
		topNames.add("Boris_Bokowski@ca.ibm.com");
		topNames.add("daniel_megert@ch.ibm.com");
		topNames.add("Mike_Wilson@ca.ibm.com");
		topNames.add("markus_keller@ch.ibm.com");
		topNames.add("kim_horne@ca.ibm.com");

		int windowSize = 14; //Sibyl.NUM_CC_RECOMMENDATIONS;

		DescriptiveStatistics stats = new DescriptiveStatistics();
		DescriptiveStatistics topstats = new DescriptiveStatistics();
		DescriptiveStatistics adjustedStats = new DescriptiveStatistics();
		DescriptiveStatistics topstatsRefined = new DescriptiveStatistics();

		int controlReports = 0;
		int noCCs = 0;
		int noCcRecommendations = 0;

		for (ChangeEvent event : this.events) {

			if (event.reportId().equals("171784")) {
				// System.err.println("Halt");
			}
			if (rank(event) != 0) {
				if (event.ccRecommendations().size() == 1
						&& event.ccRecommendations().get(0).contains(
								"Control Report")) {
					// System.out.println("---> Control report");
					controlReports++;
					continue;
				}

				if (event.getActivityLog().getCCAdded().size() == 0) {
					noCCs++;
					continue;
				}

				if (event.ccRecommendations().isEmpty()) {
					noCcRecommendations++;
					continue;
				}

				List<BugActivityEvent> historyEvents = getHistoryEvents(event);
				List<BugActivityEvent> keep = new ArrayList<BugActivityEvent>();
				for (BugActivityEvent historyEvent : historyEvents) {
					if (historyEvent.getWhat().equals("CC"))
						keep.add(historyEvent);
				}
				historyEvents.retainAll(keep);

				if (historyEvents.size() > 1) {
					System.err.println("More than one CC event: "
							+ event.reportId());
				}

				if (historyEvents.isEmpty() == false) {

					String marker = historyEvents.get(0).getName();
					List<String> ccs = event.getActivityLog().getCCAdded();
					List<String> recommendations = event.ccRecommendations()
							.subList(0, windowSize);
					ccs.remove(marker);

					if (ccs.isEmpty() == false) {
						int numCorrect = this.numCorrect(ccs, event
								.ccRecommendations(), windowSize);
						double recall = numCorrect
								/ (ccs.size() * 1.0);
						adjustedStats.addValue(recall);

						numCorrect = this.numCorrect(ccs, topNames, windowSize);
						recall = numCorrect/ (ccs.size() * 1.0);
						topstatsRefined.addValue(recall);

						if (true) {
							System.out
									.println("-------------------------------");
							System.out.println("Report: " + event.reportId()
									+ "(" + marker + ")");
							System.out.println("CC: " + ccs);
							System.out.println("Suggested: " + recommendations);
							System.out.println("Recall: " + recall);
							System.out
									.println("-------------------------------");
						}
					}
				}

				List<String> correctList = new ArrayList<String>(event
						.getActivityLog().getCCAdded());
				int numCorrect = this.numCorrect(correctList, event
						.ccRecommendations(), windowSize);
				if (numCorrect != 0) {

					if (false) {
						System.out.println("------------------------");
						System.out.println("Report Id: " + event.reportId());
						System.out.println("Recommendations: "
								+ event.ccRecommendations().subList(0,
										windowSize));
						System.out.println("CC Changes: "
								+ event.getActivityLog().getCCAdded());
						System.out.println("------------------------");
					}

					double recall = numCorrect
							/ (event.getActivityLog().getCCAdded().size() * 1.0);
					// System.out.println("==> Recall: " + recall);
					stats.addValue(recall);

					numCorrect = this.numCorrect(correctList, topNames,
							windowSize);
					recall = numCorrect
							/ (event.getActivityLog().getCCAdded().size() * 1.0);
					topstats.addValue(recall);
				} else {
					/*
					 * System.out.println("------------------------");
					 * System.out.println("Report Id: " + event.reportId());
					 * System.out.println("Recommendations: " +
					 * event.ccRecommendations()); System.out.println("CC
					 * Changes: " + event.getActivityLog().getCCAdded());
					 * System.out.println("------------------------");
					 */
				}

			}
		}

		System.out.println("Control Reports: " + controlReports);
		System.out.println("No CCs: " + noCCs);
		System.out.println("No Recommendations: " + noCcRecommendations);
		
		System.out.println("Adjusted recall: " + adjustedStats.getMean() + " ("
				+ adjustedStats.getN() + ")");

		System.out.println("Top 7 recall: " + topstats.getMean() + " ("
				+ topstats.getN() + ")");
		System.out.println("Top 7 recall-refined: " + topstatsRefined.getMean()
				+ " (" + topstatsRefined.getN() + ")");

		System.out.println("WINDOW SIZE: " + windowSize);
		return stats;
	}

	private int numCorrect(List<String> ccAdded,
			List<String> ccRecommendations, int numRecommendations) {
		int numCorrect = 0;
		for (String recommendation : ccRecommendations) {
			if (ccAdded.contains(recommendation))
				numCorrect++;
		}
		return numCorrect;
	}

	@Override
	protected String assignment(ChangeEvent event) {
		return event.ccAdded();
	}

	@Override
	protected List<BugActivityEvent> getAssignmentEvents(BugActivity activity) {
		List<BugActivityEvent> events = new ArrayList<BugActivityEvent>();
		for (BugActivityEvent event : activity) {
			if (event.getWhat().equals("CC") && event.getRemoved().equals(""))
				events.add(event);
		}
		return events;
	}

	@Override
	protected TriageClassifier getClassifier(Project project)
			throws ClassifierNotFoundException {
		return project.getCCClassifier();
	}

	@Override
	protected int rank(ChangeEvent event) {
		return event.ccRank();
	}

	@Override
	protected List<String> getRecommendations(ChangeEvent event) {
		return event.ccRecommendations();
	}

}
