package ca.uleth.bugtriage.sibyl.heuristic;

import java.util.List;

import org.eclipse.mylar.provisional.bugzilla.core.Comment;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Email;
import ca.uleth.bugtriage.sibyl.utils.LoginInfo;

public class EclipseFixedHeuristic extends HeuristicClassifier {

	private int ruleCount[] = new int[9];

	public void printStats() {
		for (int ruleNum = 0; ruleNum < this.ruleCount.length; ruleNum++) {
			System.out.println(ruleNum + ": " + this.ruleCount[ruleNum]);
		}
	}

	public void resetStats() {
		for (int ruleNum = 0; ruleNum < this.ruleCount.length; ruleNum++) {
			this.ruleCount[ruleNum] = 0;
		}
	}

	public Classification classifyReport(BugReport report) {

		ResolutionEvent resolution = report.getActivity().getResolution();
		String resolver = null;

		if (resolution != null
				&& resolution.getType().equals(ResolutionType.FIXED) == false && resolution
						.getType().equals(ResolutionType.DUPLICATE) == false) {
			 //System.out.println("not fixed: " + resolution.getType());
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Not Fixed Heuristic", 1);
		} else if (resolution != null) {
			 //System.out.println("fixed: " + resolution.getType());
		}

		// Heuristic #6
		if (resolution == null) {
			this.ruleCount[7]++;
			String lastAssigned = report.getActivity().lastAssignedTo();
			if (lastAssigned == null) { // bug report was never reassigned
				return new Classification(Email.getAddress(report
						.getAssignedTo()), "Heuristic #6", 1);
			}
			return new Classification(Email.getAddress(lastAssigned),
					"Heuristic #6", 1);
		}

		resolver = resolution.getResolvedBy();

		String assignedTo = Email.getAddress(report.getAssignedTo());
		String reporter = Email.getAddress(report.getReporter());
		ResolutionType type = resolution.getType();

		// Heuristic #1
		if (assignedTo.contains(resolver)) {
			this.ruleCount[0]++;
			return new Classification(Email.getAddress(report.getAssignedTo()),
					"Heuristic #1", 1);
		}

		// Heuristic #2
		if (!reporter.contains(resolver)) {
			this.ruleCount[1]++;
			return new Classification(resolver, "Heuristic #2 ", 1);
		}

		// Heuristic #3
		if (type.equals(ResolutionType.FIXED)) {
			this.ruleCount[2]++;
			return new Classification(resolver, "Heuristic #3", 1);
		}

		// Heuristic #4 & 5
		if (!type.equals(ResolutionType.FIXED) && reporter.contains(resolver)
				&& !assignedTo.contains(resolver)) {

			// Heuristic #7 -- Added to cover the case of a developer
			// posting a report as a 'ToDo' item
			if (type.equals(ResolutionType.LATER)
					|| type.equals(ResolutionType.WONTFIX)) {
				this.ruleCount[5]++;
				return new Classification(reporter, "ToDo Heuristic", 1);
				// return new Classification(CANNOT_CLASSIFY, "Heuristic #5",
				// 1);
			}

			// Heuristic #8 -- Added so that the resolver of the report
			// that this is a duplicate of is the one who would have fixed this
			if (type.equals(ResolutionType.DUPLICATE)) {
				this.ruleCount[6]++;
				User user = new User(User.UNKNOWN_USER_ID, Login.USER,
						Login.PASSWORD, Project.PLATFORM);
				Classification prediction = this.getDupResolver(user, report,
						this);
				return new Classification(prediction.getClassification(),
						"Duplicate Heuristic - " + prediction.getReason(), 1);
				// return new Classification(reporter, "Duplicate Heuristic",
				// 1);
				// return new Classification(CANNOT_CLASSIFY, "Heuristic #5",
				// 1);
			}

			List<Comment> comments = report.getComments();
			for (Comment comment : comments) {
				String authour = Email.getAddress(comment.getAuthor());
				// Find the first person to respond that is not the reporter
				if (!authour.contains(reporter)) { // Heuristic #4
					this.ruleCount[3]++;
					return new Classification(authour, "Heuristic #4", 1);
				}
			}
			// Heuristic #5
			this.ruleCount[4]++;
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Heuristic #5", 1);

		}

		this.ruleCount[8]++;
		System.err.println("WARNING: Unable to classify report #"
				+ report.getId());
		return new Classification(HeuristicClassifier.HEURISTIC_FAILURE,
				"Heuristic Failure", 1);
	}
}
