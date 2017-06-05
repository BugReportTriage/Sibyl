package ca.uleth.bugtriage.sibyl.heuristic;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public abstract class HeuristicClassifier extends Classifier {

	public static final String CANNOT_CLASSIFY = "Cannot classify";

	public static final String HEURISTIC_FAILURE = "Heuristics Failed";

	protected abstract Classification classifyReport(BugReport report);

	private boolean useDuplicateResolver;

	private Map<Integer, BugReport> dataset;

	public HeuristicClassifier() {
		this.useDuplicateResolver = true;
		this.dataset = new HashMap<Integer, BugReport>();
	}

	@Override
	public Classification classify(BugReport report) {
		return this.classifyReport(report);
	}

	public void setDataset(String dataset) {
		Set<BugReport> bugs = Utils.getReports(dataset);

		this.setDuplicateCache(bugs);
	}

	private void setDuplicateCache(Set<BugReport> reports) {

		for (BugReport report : reports) {
			this.dataset.put(new Integer(report.getId()), report);
		}
		
		//System.err.println(this.dataset.keySet());
	}

	public void useDuplicateResolver(boolean flag) {
		this.useDuplicateResolver = flag;
	}

	public void writeDupsToDownload(String fileName) {
		List<BugReport> duplicates = new ArrayList<BugReport>(
				this.dataset.values());
		System.err.println("Dups to Store: " + duplicates.size());

		
		// Write out dataset
		try {
			System.out.println("Writing out duplicate bug list: " + fileName);
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(fileName));
			System.out.println("Writing to: " + out);
			out.writeObject(duplicates);
			out.close();
			System.out.println("Bug list written out: " + fileName);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public Classification getDupResolver(BugReport report,
			HeuristicClassifier classifier) {

		if (this.useDuplicateResolver) {
			int bugId = report.getDupId();// getDuplicateId(user, report);
			//System.out.println("Getting dup " + report.getId() + " --> " + bugId);
			// System.out.println(report.getId() + " dup of " + bugId);
			if (bugId != -1) {
				BugReport dupReport = null;
				try {
					
					if (this.dataset.containsKey(bugId)) {
						// System.out.println("Getting from map: " + bugId);
						dupReport = this.dataset.get(bugId);
					} else {
						System.out.println("Getting duplicate for "
								+ report.getId());
						// System.out.println("Getting from server");
						
						// FIXME
						//dupReport = Utils.getReport(user.getRepository(), user
							//	.getName(), user.getPassword(), bugId);
						//System.out.println("Report id: " + dupReport.getId());
						System.out.println("Adding " + bugId + " to dups");
						this.dataset.put(bugId, dupReport);
					}

					if (true) {
						//System.out.println("Updating description");
						// Add duplicate description to report description
						//System.out.println(report.getDescription());
						//System.out.println("-------------------------");
						String description = report.getDescription();
						String newDescription = description + "\n"
								+ dupReport.getDescription();
						//System.out.println(newDescription);
						//System.out.println("*************************");
						report.setDescription(newDescription);
						//System.out.println(report.getDescription());
						//System.out
							//	.println("===================================");
					}

					Classification classification = classifier
							.classify(dupReport);
					return classification;
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					/*
					 * Mylar Bugzilla 0.5.2 library assumes an unmodified
					 * Bugzilla and Evolution has added the "NEEDINFO" status.
					 * Check if that is the cause of the problem and ignore if
					 * appropriate.
					 */
					if (e.getMessage().contains("NEEDINFO")) {
						System.out
								.println("Duplicate report "
										+ bugId
										+ " couldn't be created by the Bugzilla library: "
										+ e.getMessage());
						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"Non standard Bugzilla", 1);
					}
					throw e;
				}
			} else {
				System.err.println("WARNING: Unable to parse dup bug id: "
						+ report.getId());
			}
		} else {
			System.err.println("Not using dup resolver");
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Duplicate Resolver Not Used", 1);
		}

		return null;
	}

	private static int getDuplicateId(BugReport report) {
		
		if(true)
			throw new UnsupportedOperationException();

		//try {
			//String reportUrl = user.getRepository() + "/show_bug.cgi?id="
				//	+ report.getId();
/*
			BugzillaPage page = new BugzillaPage();
			//URL bugReportURL = new URL(reportUrl);
			Messages messages = new Messages();
			//ca.uleth.bugtriage.sibyl.utils.Utils.connectToServer(bugReportURL, messages);
			//page.get(bugReportURL.openStream(), messages);

			if (messages.size() != 0) {
				System.out.println(messages);
			}

			Pattern dupIdPattern = Pattern
					.compile("\\*\\*\\*\\sThis.*show_bug.cgi\\?id=(\\d+).*");
			Matcher matcher = dupIdPattern.matcher(page.toString());
			while (matcher.find()) {
				String dupIdStr = matcher.group(1);
				return Integer.parseInt(dupIdStr);
			}
 		*/
		//} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}

		return -1;
	}	
}
