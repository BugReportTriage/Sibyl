package ca.uleth.bugtriage.sibyl.analysis;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.ComponentClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;

public abstract class RecommendationAnalysis {

	/*
	 * return rank of recommendation that was the actual assignment, 0 if no
	 * assignment made and -1 if not recommended
	 */
	protected abstract int rank(ChangeEvent event);

	protected abstract TriageClassifier getClassifier(Project project)
			throws ClassifierNotFoundException;

	public abstract DescriptiveStatistics accuracy();

	protected abstract String assignment(ChangeEvent event);

	protected abstract List<BugActivityEvent> getAssignmentEvents(
			BugActivity activity);

	protected final List<ChangeEvent> events;

	private final Map<Date, ChangeEvent> failures;

	protected final List<ViewEvent> viewEvents;

	public RecommendationAnalysis(List<ViewEvent> viewEvents,
			List<ChangeEvent> events) {
		this.viewEvents = viewEvents;
		this.events = events;
		this.failures = new HashMap<Date, ChangeEvent>();

	}

	public DescriptiveStatistics ranks() {
		int rank;
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (ChangeEvent event : this.events) {
			rank = rank(event);
			if (rank > 0) {
				stats.addValue(rank);
			} else if (rank == -1) {
				this.failures.put(event.getDate(), event);
			}
		}

		return stats;
	}

	public Map<Date, ChangeEvent> failures() {
		return this.failures;
	}

	public boolean correct(List<String> assignments,
			List<String> recommendations, int recommendationsGiven) {
		String recommendation;
		int numRecommendations = Math.min(recommendationsGiven, recommendations
				.size());
		for (int rank = 0; rank < numRecommendations; rank++) {
			recommendation = recommendations.get(rank);
			for (String assignment : assignments) {
				if (recommendation.equals(assignment)) {
					return true;
				}
			}

		}
		return false;
	}

	public Map<Date, String> unknownAssignments(Project project) {

		Map<Date, String> unknowns = new HashMap<Date, String>();
		try {
			ChangeEvent event;
			String assigned;
			List<String> knowns;
			TriageClassifier classifier = this.getClassifier(project);

			for (Date date : this.failures.keySet()) {
				boolean known = false;
				event = this.failures.get(date);
				assigned = assignment(event);
				if (classifier instanceof ComponentClassifier) {
					knowns = ((ComponentClassifier) classifier)
							.getClasses(event.component());
				} else {
					knowns = classifier.getClasses();
					System.out.println("Num classes: " + knowns);
				}

				for (String name : knowns) {
					if (name.equals(assigned)) {
						known = true;
						break;
					}
				}
				if (!known) {
					unknowns.put(date, assigned);
				}
			}
		} catch (ClassifierNotFoundException e) {
			System.err.println("Classifier not found for project "
					+ project.name);
		}
		return unknowns;
	}

	private static final DateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static final long TIME_WINDOW = 5 * 60 * 1000;

	protected double getTime(List events) {

		Collections.sort(events, new Comparator() {

			public int compare(Object event1, Object event2) {
				Date date1 = null, date2 = null;
				if (event1 instanceof ViewEvent)
					date1 = ((ViewEvent) event1).getDate();
				if (event2 instanceof ViewEvent)
					date2 = ((ViewEvent) event2).getDate();
				if (event1 instanceof ChangeEvent)
					date1 = ((ChangeEvent) event1).getDate();
				if (event2 instanceof ChangeEvent)
					date2 = ((ChangeEvent) event2).getDate();

				return date1.compareTo(date2);
			}
		});

		List<ChangeEvent> toRemove = new ArrayList<ChangeEvent>();
		for (Object event : events) {
			if (event instanceof ChangeEvent) {
				ChangeEvent ce = (ChangeEvent) event;
				BugActivity activity = ce.getActivityLog();
				List<BugActivityEvent> assignments = getAssignmentEvents(activity);

				if (assignments.isEmpty()) {
					toRemove.add(ce);
					continue;
				}

				for (BugActivityEvent ae : assignments) {
					try {
						Date aeDate = adjustTimeZone(formatter.parse(ae
								.getDate()));
						long aeTime = aeDate.getTime();
						long ceTime = ce.getDate().getTime();
						// System.out.println(ce.reportId() + ": " +
						// ae.getDate() + " ?=? " + ce.getDate());
						// System.out.println(Math.abs(aeTime-ceTime) + "?>?" +
						// TIME_LIMIT);
						if (Math.abs(aeTime - ceTime) > TIME_WINDOW) {
							//toRemove.add(ce);
						}
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		// System.out.println(events);
		events.removeAll(toRemove);
		// System.out.println(events);

		SummaryStatistics timeStats = new SummaryStatistics();
		ViewEvent firstView = null;
		ChangeEvent assignmentMade;
		for (Object event : events) {
			if (event instanceof ViewEvent) {
				if (firstView == null)
					firstView = (ViewEvent) event;
			}

			if (event instanceof ChangeEvent) {
				assignmentMade = (ChangeEvent) event;
				if (firstView != null) {
					long timeTaken = assignmentMade.getDate().getTime()
							- firstView.getDate().getTime();
					timeStats.addValue(timeTaken);
				}
			}
		}

		if (timeStats.getN() == 0) {
			return -1;
		}
		// System.out.println("Assignment Time: " + timeStats.getMean() / 1000);
		return timeStats.getMean() / 1000;
	}

	private Date adjustTimeZone(Date date) {
		// System.out.println("DATE: " + date);
		Calendar calDate = Calendar.getInstance();
		calDate.setTime(date);
		// System.out.println("OLD: " + calDate.getTime());
		calDate.add(Calendar.HOUR, -3);
		// System.out.println("NEW: " + calDate.getTime());
		return calDate.getTime();
	}

	public SummaryStatistics timeToAssign(boolean controlEvents) {
		Map<String, List> reportEvents = new HashMap<String, List>();
		for (ChangeEvent event : this.events) {

			//List<String> recommendations = this.getRecommendations(event);
			
			if (isControlEvent(event)) {
				if (!controlEvents) {
					continue;
				}
			} else {
				if (controlEvents)
					continue;
				//if(recommendations.isEmpty())
					//continue;
			}

			String id = event.reportId();
			if (!reportEvents.containsKey(id)) {
				reportEvents.put(id, new ArrayList());
			}
			List changeEvents = reportEvents.get(id);
			changeEvents.add(event);
		}

		for (ViewEvent event : this.viewEvents) {
			String id = event.reportId();
			if (!reportEvents.containsKey(id)) {
				continue;
			}
			List changeEvents = reportEvents.get(id);
			changeEvents.add(event);
		}

		SummaryStatistics timeStats = new SummaryStatistics();
		for (String reportId : reportEvents.keySet()) {
			List events = reportEvents.get(reportId);

			double time = getTime(events);
			if (time > 0) {
				System.out.println(time);
				timeStats.addValue(time);
			}
		}

		return timeStats;
	}

	private boolean isControlEvent(ChangeEvent event) {
		List<String> recommendations = event.assignmentRecommendations();
		return recommendations.isEmpty() == false
				&& recommendations.get(0).contains("Control");
	}

	protected abstract List<String> getRecommendations(ChangeEvent event);

	protected List<BugActivityEvent> getHistoryEvents(ChangeEvent event) {

		List<BugActivityEvent> historyEvents = new ArrayList<BugActivityEvent>();
		Date eventDate = event.getDate();
		for (BugActivityEvent historyEvent : event.getActivityLog()) {
			try {
				Date historyEventDate = adjustTimeZone(formatter
						.parse(historyEvent.getDate()));
				long timeDiff = Math.abs(historyEventDate.getTime()
						- eventDate.getTime());
				if (timeDiff < TIME_WINDOW) {
					historyEvents.add(historyEvent);
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (historyEvents.isEmpty()) {
			System.err.println("No history? " + event.reportId());
		}
		return historyEvents;
	}
}
