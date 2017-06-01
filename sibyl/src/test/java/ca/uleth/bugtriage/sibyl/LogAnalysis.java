package ca.uleth.bugtriage.sibyl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.security.auth.login.LoginException;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math.stat.descriptive.SummaryStatistics;
import org.junit.Before;
import org.junit.Test;

import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.ActivityParser;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.analysis.AllAssignments;
import ca.uleth.bugtriage.sibyl.analysis.AssignmentAnalysis;
import ca.uleth.bugtriage.sibyl.analysis.ChangeEvent;
import ca.uleth.bugtriage.sibyl.analysis.ComponentAnalysis;
import ca.uleth.bugtriage.sibyl.analysis.InterestAnalysis;
import ca.uleth.bugtriage.sibyl.analysis.RecommendationAnalysis;
import ca.uleth.bugtriage.sibyl.analysis.SubcomponentAnalysis;
import ca.uleth.bugtriage.sibyl.analysis.SubmissionEvent;
import ca.uleth.bugtriage.sibyl.analysis.Survey;
import ca.uleth.bugtriage.sibyl.analysis.ViewEvent;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.servlet.util.LogTags;
import ca.uleth.bugtriage.sibyl.servlet.util.PerformanceData;
import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class LogAnalysis {

	private static final String BUILD_PAGE = "Build Page";

	private static final String RECOMMENDATION = "Make Recommendation";

	private static final String GET_REPORT = "Get Report";

	private File logFile;

	private final List<PerformanceData> performanceEvents = new ArrayList<PerformanceData>();

	private final List<ViewEvent> viewEvents = new ArrayList<ViewEvent>();

	private List<Survey> surveys = new ArrayList<Survey>();

	private List<ChangeEvent> changeEvents = new ArrayList<ChangeEvent>();

	private List<String> unknownEvents = new ArrayList<String>();

	private List<String> changedBugs = new ArrayList<String>();

	private Project project;

	private Map<String, BugActivity> logs = new HashMap<String, BugActivity>();

	private File activityLogsFile;

	private int multipleReportEvents = 0;

	private final File reportsFile;

	private Map<String, BugReport> reports = new HashMap<String, BugReport>();

	public LogAnalysis() {
		this.logFile = null;
		this.reportsFile = null;
	}

	public LogAnalysis(Project project, File logFile, File activityLogs, File reports) {
		this.project = project;
		this.logFile = logFile;
		this.activityLogsFile = activityLogs;
		this.reportsFile = reports;
	}

	public static void main(String[] args) {
		Project project = Project.PLATFORM;
		LogAnalysis analysis = new LogAnalysis(Project.PLATFORM, new File(
				"X:/sibyl/logAnalysis/all.log"), new File(
				"X:/sibyl/logAnalysis/all.activity"), new File("X:/sibyl/logAnalysis/all.reports"));
		analysis.process();

		System.out.println("Multiple Report Change Events: "
				+ analysis.getMultipleReportEvents() + " out of "
				+ analysis.changeEvents.size());

		//assignmentAnalysis(project, analysis);

		//componentAnalysis(project, analysis);

		//subcomponentAnalysis(project, analysis);

		//ccAnalysis(project, analysis);

		//System.out.println("\n--- Survey Analysis ---");
		//System.out.println(analysis.surveyAnalysis());

		// System.out.println("\n--- Performance Analysis ---");
		 //System.out.println(analysis.performanceAnalysis());

		SummaryStatistics timeStats;
		recommendationAnalysis(analysis.allAnalysis(), project);
		timeStats = analysis.allAnalysis().timeToAssign(true);
		System.out.println("Time Mean (Control): " + timeStats.getMean());
		timeStats = analysis.allAnalysis().timeToAssign(false);
		System.out.println("Time Mean (Recommended): " + timeStats.getMean());
		
	}

	
	
	private static void ccAnalysis(Project project, LogAnalysis analysis) {
		System.out.println("\n--- CC: Analysis ---");
		recommendationAnalysis(analysis.interestAnalysis(), project);
		
		SummaryStatistics timeStats;
		timeStats = analysis.interestAnalysis().timeToAssign(true);
		System.out.println("Time Mean (Control): " + timeStats.getMean());
		timeStats = analysis.interestAnalysis().timeToAssign(false);
		System.out.println("Time Mean (Recommended): " + timeStats.getMean());
		
	}

	private static void subcomponentAnalysis(Project project,
			LogAnalysis analysis) {
		SummaryStatistics timeStats;
		System.out.println("\n--- Subcomponent Analysis ---");
		SubcomponentAnalysis subComponentAnalysis = analysis
				.subcomponentAnalysis();
		//recommendationAnalysis(subComponentAnalysis, project);

		timeStats = analysis.subcomponentAnalysis().timeToAssign(true);
		System.out.println("Time Mean (Control): " + timeStats.getMean());
		timeStats = analysis.subcomponentAnalysis().timeToAssign(false);
		System.out.println("Time Mean (Recommended): " + timeStats.getMean());
	}

	private static void componentAnalysis(Project project, LogAnalysis analysis) {
		SummaryStatistics timeStats;
		System.out.println("\n--- Component Analysis ---");
		recommendationAnalysis(analysis.componentAnalysis(), project);

		timeStats = analysis.componentAnalysis().timeToAssign(true);
		System.out.println("Time Mean (Control): " + timeStats.getMean());
		timeStats = analysis.componentAnalysis().timeToAssign(false);
		System.out.println("Time Mean (Recommended): " + timeStats.getMean());
	}

	private static void assignmentAnalysis(Project project, LogAnalysis analysis) {
		System.out.println("\n--- Assignment Analysis ---");
		AssignmentAnalysis assignAnalysis = analysis.assignmentAnalysis();
		recommendationAnalysis(assignAnalysis, project);
		System.out.println("Recommendation CCs: "
				+ assignAnalysis.recommendationCC());

		/*
		SummaryStatistics timeStats = assignAnalysis.timeToAssign(true);
		System.out.println("Time Mean (Control): " + timeStats.getMean());
		timeStats = assignAnalysis.timeToAssign(false);
		System.out.println("Time Mean (Recommended): " + timeStats.getMean());
		
		assignAnalysis.reportedByTeam(analysis.reports);
		*/
	}

	private static void recommendationAnalysis(RecommendationAnalysis analysis,
			Project project) {
		DescriptiveStatistics stats = analysis.ranks();
		System.out.println("Mean Rank: " + stats.getMean());
		System.out.println("Max Rank: " + stats.getMax());
		System.out.println("Min Rank: " + stats.getMin());
		System.out.println("Recommendation Correct: " + stats.getN());

		Map<Date, ChangeEvent> failures = analysis.failures();
		Map<Date, String> unknowns = analysis.unknownAssignments(project);
		System.out.println("Recommendation Failures: " + failures.size()
				+ "[Unknown: " + unknowns.size() + "]");
		for (Date date : unknowns.keySet()) {
			System.out.println("\t" + unknowns.get(date) + "(" + date + ")");
		}

		DescriptiveStatistics accuracyStats = analysis.accuracy();
		System.out.println("Accuracy: " + accuracyStats.getMean()
				+ "[Instances: " + accuracyStats.getN() + "]");
	}

	@Before
	public void testStartup() {
		this.project = Project.PLATFORM;
		this.logFile = new File("X:/sibyl/logAnalysis/test.log");
		this.activityLogsFile = new File("X:/sibyl/logAnalysis/test.activity");
		this.process();
	}

	@Test
	public void testProcess() {
		assertEquals(7, this.performanceEvents.size());
		assertEquals(7, this.viewEvents.size());
		assertEquals(7, this.changeEvents.size());
		assertEquals(1, this.surveys.size());
		assertEquals(0, this.unknownEvents.size());

		SubmissionEvent change;
		change = this.changeEvents.get(0);
		assertEquals(42, change.size());

		change = this.changeEvents.get(1);
		assertEquals(42, change.size());

		change = this.changeEvents.get(2);
		assertEquals(43, change.size());

		change = this.changeEvents.get(6);
		assertEquals(36, change.size());
		assertEquals(null, change.getData().get("comment"));

		Survey survey = this.surveys.get(0);
		assertEquals(7, survey.size());

		assertEquals(1, this.multipleReportEvents);
	}

	@Test
	public void testChangeEvent() {
		ChangeEvent change = this.changeEvents.get(0);

		assertEquals("162455", change.reportId());
		assertFalse(change.commentAdded());
		assertEquals(10, change.assignmentRecommendations().size());
		assertEquals("Boris_Bokowski@ca.ibm.com", change
				.assignmentRecommendations().get(0));
		assertEquals("Karice_McIntyre@ca.ibm.com", change
				.assignmentRecommendations().get(5));
		assertEquals("daniel_megert@ch.ibm.com", change
				.assignmentRecommendations().get(9));
		assertEquals(5, change.assignmentRank());

		change = this.changeEvents.get(2);
		assertEquals(0, change.assignmentRank());

	}

	@Test
	public void testAssignmentAnalysis() {
		AssignmentAnalysis analysis = this.assignmentAnalysis();
		DescriptiveStatistics stats = analysis.ranks();
		assertEquals(5.0, stats.getMean(), 0.1);
		assertEquals(5.0, stats.getMax());
		assertEquals(5.0, stats.getMin());
		assertTrue(2 == stats.getN());
		assertEquals(2, analysis.failures().size());

		assertEquals(2, analysis.recommendationCC());
		// assertTrue("Expected 6, Actual: " + analysis.accuracy().getN(),
		// 6 == analysis.accuracy().getN());
		// assertEquals(0.333, analysis.accuracy().getMean(), 0.001);

		System.out.println("Doing time experiment");
		SummaryStatistics timeStats = analysis.timeToAssign(false);

	}

	@Test
	public void testComponentAnalysis() {
		RecommendationAnalysis analysis = this.componentAnalysis();
		DescriptiveStatistics stats = analysis.ranks();
		assertEquals(1.0, stats.getMean());
		assertEquals(1.0, stats.getMax());
		assertEquals(1.0, stats.getMin());
		assertTrue(2 == stats.getN());

		assertEquals(2, analysis.failures().size());

		assertTrue("Expected 4, Actual: " + analysis.accuracy().getN(),
				4 == analysis.accuracy().getN());
		assertEquals(0.5, analysis.accuracy().getMean(), 0.001);
	}

	@Test
	public void testSubcomponentAnalysis() {
		RecommendationAnalysis analysis = this.subcomponentAnalysis();
		DescriptiveStatistics stats = analysis.ranks();
		assertEquals(2.25, stats.getMean(), 0.01);
		assertEquals(5.0, stats.getMax());
		assertEquals(1.0, stats.getMin());
		assertTrue(4 == stats.getN());
		assertEquals(2, analysis.failures().size());

		assertTrue("Expected 6, Actual: " + analysis.accuracy().getN(),
				6 == analysis.accuracy().getN());
		assertEquals(0.5, analysis.accuracy().getMean(), 0.001);
	}

	@Test
	public void testSurvey() {
		Survey survey = this.surveys.get(0);
		assertEquals("ok", survey.subcomponentNumRecommendations());
		assertEquals("no", survey.subcomponentMultiple());
		assertFalse("Said component used", survey.componentUsed());
		assertEquals("no", survey.assignmentMultiple());
		assertEquals("ok", survey.assignmentNumRecommendations());
		assertEquals("", survey.subcomponentReasoning());
		assertTrue(survey.subcomponentUsed());

		System.out.println(this.surveyAnalysis());
	}

	@Test
	public void testPerformance() {
		Map<String, DescriptiveStatistics> performance = this.performanceData();

		DescriptiveStatistics stats = performance.get(GET_REPORT);
		assertEquals(2.988, stats.getMean(), 0.001);
		assertEquals(4.598, stats.getMax(), 0.001);
		assertEquals(2.298, stats.getMin(), 0.001);

		stats = performance.get(RECOMMENDATION);
		assertEquals(0.052, stats.getMean(), 0.001);
		assertEquals(0.077, stats.getMax(), 0.001);
		assertEquals(0.023, stats.getMin(), 0.001);

		stats = performance.get(BUILD_PAGE);
		assertEquals(1.605, stats.getMean(), 0.001);
		assertEquals(2.147, stats.getMax(), 0.001);
		assertEquals(1.326, stats.getMin(), 0.001);

	}

	public AssignmentAnalysis assignmentAnalysis() {
		return new AssignmentAnalysis(this.viewEvents, this.changeEvents);
	}

	public ComponentAnalysis componentAnalysis() {
		return new ComponentAnalysis(this.viewEvents, this.changeEvents);
	}

	public SubcomponentAnalysis subcomponentAnalysis() {
		return new SubcomponentAnalysis(this.viewEvents, this.changeEvents);
	}

	public RecommendationAnalysis interestAnalysis() {
		return new InterestAnalysis(this.viewEvents, this.changeEvents);
	}
	
	public RecommendationAnalysis allAnalysis() {
		return new AllAssignments(this.viewEvents, this.changeEvents);
	}

	public FrequencyTable surveyAnalysis() {
		FrequencyTable table = new FrequencyTable();
		Map<String, String> data;
		for (Survey survey : this.surveys) {
			data = survey.getData();
			for (String key : data.keySet()) {
				if (!key.equals("assignmentSurvey") && !key.equals("userId")) {
					table.add(key + "-->" + data.get(key));
				}
			}
		}
		return table;
	}

	private String performanceAnalysis() {
		Map<String, DescriptiveStatistics> data = performanceData();

		StringBuffer sb = new StringBuffer();
		DescriptiveStatistics stats;
		for (String key : data.keySet()) {
			stats = data.get(key);
			sb.append(key + ": " + stats.getMean() + " [Min: " + stats.getMin()
					+ " Max: " + stats.getMax() + "]\n");
		}
		return sb.toString();
	}

	private Map<String, DescriptiveStatistics> performanceData() {
		Map<String, DescriptiveStatistics> stats = new HashMap<String, DescriptiveStatistics>();
		stats.put(BUILD_PAGE, new DescriptiveStatistics());
		stats.put(RECOMMENDATION, new DescriptiveStatistics());
		stats.put(GET_REPORT, new DescriptiveStatistics());
		for (PerformanceData performance : this.performanceEvents) {
			stats.get(BUILD_PAGE).addValue(performance.getBuildPageTime());
			stats.get(RECOMMENDATION).addValue(
					performance.getRecommendationsTime());
			stats.get(GET_REPORT).addValue(performance.getReportTime());
		}
		return stats;
	}

	
	private void process() {
		try {
			System.out.print("Loading Activity Logs....");
			this.loadActivityLogs();
			System.out.println("Done");
			
			//System.out.print("Loading Reports ....");
			//this.loadReports();
		//	System.out.println("Done");
			
			BufferedReader reader = new BufferedReader(new FileReader(
					this.logFile));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				line = line.trim();
				// Sibyl performance information
				// e.g. 25 Oct 2006 06:43:05 [performance] getReport=2.598
				// getRecommendations=0.091 buildPage=1.337
				if (line.contains(LogTags.PERFORMANCE)) {
					int splitIndex = line.indexOf(LogTags.PERFORMANCE)
							+ LogTags.PERFORMANCE.length();
					String data = line.substring(splitIndex);
					this.performanceEvents.add(new PerformanceData(data));
					continue;
				}

				/*
				 * View events e.g. 25 Oct 2006 06:43:05 [view]
				 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=161884
				 */
				if (line.contains(LogTags.VIEW)) {
					try {
						String[] split = line.split("\\Q" + LogTags.VIEW
								+ "\\E");
						int idIndex = split[1].indexOf("id=") + "id=".length();
						String id = split[1].substring(idIndex);
						Date date = Recorder.FORMATTER.parse(split[0]);
						this.viewEvents.add(new ViewEvent(date, id));
						continue;
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				/* Change events */
				if (line.contains(LogTags.CHANGE)) {
					ChangeEvent event = new ChangeEvent();
					String[] split = line.split("\\Q" + LogTags.CHANGE + "\\E");
					try {
						Date date = Recorder.FORMATTER.parse(split[0]);
						event.setDate(date);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					for (line = reader.readLine(); line.equals("") == false; line = reader
							.readLine()) {
						line = line.trim();
						if (line.startsWith("comment:")) {
							// Returns the next line after the comment
							line = handleMultiline(event, line.trim(), reader);
						}
						event.addData(line);
					}

					String id = event.reportId();
					if (id == null) {
						System.err.println("Problem getting id");
					}
					BugActivity log = getLog(event.reportId());
					event.setActivityLog(log);
					this.changeEvents.add(event);
					continue;
				}

				/* Assignment survey */
				if (line.contains(LogTags.ASSIGNMENT_SURVEY_GIVEN)) {
					continue;
				}

				if (line.contains(LogTags.ASSIGNMENT_SURVEY_SUBMITTED)) {
					Survey event = new Survey();
					for (line = reader.readLine(); line.equals("") == false; line = reader
							.readLine()) {
						/*
						 * -- No one seems to add reasoning if
						 * (line.contains("Reasoning:")) { // Returns the next
						 * line after the reasoning line =
						 * handleMultiline(event, line.trim(), reader); if
						 * (line.equals("")) break; }
						 */
						event.addData(line.trim());
					}
					this.surveys.add(event);
					continue;
				}

				if (line.contains(LogTags.RECOMMENDER_SURVEY_GIVEN)) {
					continue;
				}
				System.err.println("Unknown event: " + line);
				this.unknownEvents.add(line);

			}
			//this.saveActivityLogs();
//this.saveReports();
			
			// Determine if multiple change events happened to the same report
			List<String> changeIds = new ArrayList<String>();
			for (ChangeEvent event : this.changeEvents) {
				String reportId = event.reportId();
				if (changeIds.contains(reportId)) {
					this.multipleReportEvents++;
				} else {
					changeIds.add(reportId);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void loadReports() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					this.reportsFile));
			this.reports = (Map<String, BugReport>) in.readObject();
		} catch (FileNotFoundException e) {
			System.err.println("Reports file not found: "
					+ this.reportsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadActivityLogs() {
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					this.activityLogsFile));
			this.logs = (Map<String, BugActivity>) in.readObject();
		} catch (FileNotFoundException e) {
			System.err.println("Activitiy logs file not found: "
					+ this.activityLogsFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void saveActivityLogs() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(this.activityLogsFile));
			out.writeObject(this.logs);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void saveReports() {
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(this.reportsFile));
			out.writeObject(this.reports);
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private BugActivity getLog(String bugId) {
		
		if(false)
		return this.getReport(bugId).getActivity();
		
		
		try {
			BugActivity log = this.logs.get(bugId);
			if (log == null) {
				ActivityParser parser = new ActivityParser();
				System.out.println("Getting log for " + bugId);
				log = parser.retrieve(this.project.getUrl(), Login.USER,
						Login.PASSWORD, Integer.parseInt(bugId));
				this.logs.put(bugId, log);
			}
			return log;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private BugReport getReport(String bugId) {
		try {
			BugReport report = this.reports.get(bugId);
			if (report == null) {
				System.out.println("Getting report for " + bugId);
				report = Utils.getReport(this.project.getUrl(), Login.USER,
						Login.PASSWORD, Integer.parseInt(bugId));
				
				
				this.reports.put(bugId, report);
			}
			return report;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private String handleMultiline(SubmissionEvent event, String line,
			BufferedReader reader) {
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(line);

			line = reader.readLine().trim();
			while (Pattern.matches("^[-\\w]+:[-@\\.\\w]*", line) == false) {
				sb.append(line + "\n");
				line = reader.readLine().trim();
			}
			event.addData(sb.toString().trim());
			return line;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public int getMultipleReportEvents() {
		return multipleReportEvents;
	}
}
