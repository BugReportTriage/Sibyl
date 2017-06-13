package ca.uleth.bugtriage.sibyl.misc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.events.AssignmentEvent;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class BreakDownDataMonths {

	public static void main(String[] args) {

		Project project = Project.PLATFORM;
		Set<BugReport> reports = null;//Utils.getReports(Utils.getTrainingSet(EclipseData.ECLIPSE_DIR, 8, EclipseData.LAST_TRAINING_MONTH));

		Map<Date, Set<BugReport>> reportSets = breakupReports(reports);

		for (Date date : reportSets.keySet()) {
			System.out.println(date);
		}

		//writeoutReports(project, project.getDataDir(), reportSets);
	}
	
	private static Map<Date, Set<BugReport>> breakupReports(Set<BugReport> reports) {
		System.out.println("Breaking up reports");
		Map<Date, Set<BugReport>> reportSets = new TreeMap<Date, Set<BugReport>>();
		Set<BugReport> reportSet;
		String date = "";
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		for (BugReport report : reports) {
			if (report != null) {
				Date resolutionDate;
				try {
					switch (report.getStatus()) {
					case ASSIGNED:
						List<AssignmentEvent> assignments = report.getActivity().getAssignmentEvents();
						if (assignments.isEmpty() == false) {
							date = assignments.get(assignments.size() - 1).getDate();
						} else {
							// Handle strange cases like Eclipse #131669
							// List<BugActivityEvent> activity = report
							// .getActivity().getEvents();
							// date = activity.get(activity.size() -
							// 1).getDate();
						}
						break;
					case RESOLVED:
					case VERIFIED:
					case CLOSED:
						date = report.getActivity().resolution().getDate();
						break;
					default:
						System.err.println("Strange status: " + report.getStatus());
						continue;
					}

					// System.out.println(date);
					// if(date.contains("-05-")){
					// System.err.println("Hold your horses!");
					// }
					resolutionDate = formatter.parse(date);

					if (reportSets.containsKey(resolutionDate) == false) {
						reportSets.put(resolutionDate, new HashSet<BugReport>());
					}
					reportSet = reportSets.get(resolutionDate);
					reportSet.add(report);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NullPointerException e) {
					e.printStackTrace();
				} catch (ArrayIndexOutOfBoundsException e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
		return reportSets;
	}
}
