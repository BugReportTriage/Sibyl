package ca.uleth.bugtriage.sibyl.heuristic;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.AttachmentEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Email;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.LoginInfo;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EvolutionHeuristicOptimized extends HeuristicClassifier {

	public Classification classifyReport(BugReport report) {

		if (report.equals(BugReport.ACCESS_DENIED_BUG)) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Access denied to bug", 1);
		}

		BugActivity activity = report.getActivity();
		ResolutionEvent resolution = activity.getResolution();
		
		if (resolution != null) {
			ResolutionType type = resolution.getType();
			/*
			 * Assign to the class of the report that this report is a duplicate
			 * of.
			 */
			if (ResolutionType.DUPLICATE.equals(type)) {
				User user = new User(User.UNKNOWN_USER_ID, Login.USER,
						Login.PASSWORD, Project.EVOLUTION);
				Classification prediction = this.getDupResolver(user, report,
						Heuristic.EVOLUTION.getClassifier());
				return new Classification(prediction.getClassification(),
						"Duplicate Heuristic - " + prediction.getReason(), 1);
			}

			/* Assign report to submitter of approved attachment */
			if (ResolutionType.FIXED.equals(type)) {
				List<AttachmentEvent> approvedAttachments = activity
						.getApprovedAttachments();
				if (approvedAttachments.isEmpty()) {
					/*
					 * Bug was fixed without a patch, so assign whomever marked
					 * it as fixed
					 */
					return new Classification(Email.getAddress(resolution
							.getResolvedBy()), "Fixed, No Patch", 1);
				}

				List<String> submitters = new ArrayList<String>(activity
						.getAttachmentSubmitters(approvedAttachments));

				if (submitters.size() == 0) {
					/*
					 * Cannot determine who submitted the approved patch or, so
					 * choose the assigned developer
					 */
					System.err.println("WARNING: No submitters found for: "
							+ report.getId());
					return new Classification(Email.getAddress(report
							.getAssignedTo()), "Patch Submitter Unknown", 1);
				}

				if (submitters.size() == 1) {
					String submitter = submitters.get(0);
					return new Classification(Email.getAddress(submitter),
							"Approved Patch Heuristic", 1);
				}

				if (submitters.size() > 1) {
					String prolificSubmitter = activity
							.getMostFrequentAttachmentSubmitter();
					if (prolificSubmitter.equals("")) {
						int numSubmitters = submitters.size() - 1;
						String lastSubmitter = submitters.get(numSubmitters);
						return new Classification(Email
								.getAddress(lastSubmitter),
								"Fixed, Last Submitter ", 1);
					}

					return new Classification(Email
							.getAddress(prolificSubmitter),
							"Fixed, Prolific Submitter", 1);

				}
			}

			/*
			 * If report is INVALID, it is typically intercepted by the triager
			 */
			if (ResolutionType.INVALID.equals(type)) {
				/*
				 * If the submitter retracted the bug, then don't know who would
				 * have fixed it
				 */
				String resolver = Email.getAddress(resolution.getResolvedBy());
				String submitter = Email.getAddress(report.getReporter());
				if (resolver.equals(submitter)) {
					return new Classification(
							HeuristicClassifier.CANNOT_CLASSIFY,
							"Submitter Error Heuristic", 1);
				}

				return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
						"Invalid Report Heuristic", 1);
			}

			/*
			 * As WONTFIX is usually reached by consenus, don't know who would
			 * have fixed it
			 */
			if (ResolutionType.WONTFIX.equals(type)) {
				return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
						"Won't Fix Heuristic", 1);
			}

			/*
			 * Traiger intercepts INCOMPLETE, so can't classify
			 */
			if (ResolutionType.INCOMPLETE.equals(type)) {
				return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
						"Incomplete Heuristic", 1);
			}

			/*
			 * Typically a lot of discussion happens before a report is deemed
			 * NOTABUG, so can't make a prediction
			 */
			if (ResolutionType.NOTABUG.equals(type)) {
				return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
						"NotABug Heuristic", 1);
			}

			/*
			 * Typically a lot of discussion happens before a report is deemed
			 * NOTGNOME, so can't make a prediction
			 */
			if (ResolutionType.NOTGNOME.equals(type)) {
				return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
						"NotGnome Heuristic", 1);
			}

			/* Triagers tend to determine if something is OBSOLETE */
			if (ResolutionType.OBSOLETE.equals(type)) {
				return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
						"NotGnome Heuristic", 1);
			}
		}
		
		StatusType status = report.getStatus();
		/*
		 * If report is new (i.e. no one has accepted responsibility), then
		 * don't know who will resolve it
		 */
		if (StatusType.NEW.equals(status)) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"New Bug Heuristic", 1);
		}

		/* If report has been assigned then label with that person */
		if (StatusType.ASSIGNED.equals(status)) {
			return new Classification(Email.getAddress(report
					.getAssignedTo()), "Assigned Bug Heuristic", 1);
		}

		/* If report is unconfirmed, then don't know who will resolve it */
		if (StatusType.UNCONFIRMED.equals(status)) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Unconfirmed Bug Heuristic", 1);
		}

		/*
		 * If report is reopened, then don't (necessarily) know who will
		 * resolve it
		 */
		if (StatusType.REOPENED.equals(status)) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Reopened Bug Heuristic", 1);
		}
		
		// Some case was not covered
		System.err.println("WARNING: Unable to classify report #"
				+ report.getId());
		return new Classification(HeuristicClassifier.HEURISTIC_FAILURE,
				"Heuristic Failure", 1);

	}
}
