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

public class EvolutionHeuristic extends HeuristicClassifier {

	public EvolutionHeuristic() {
		Heuristics.setHeuristic(this);
	}

	public Classification classifyReport(BugReport report) {

		if (report.equals(BugReport.ACCESS_DENIED_BUG)) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Access denied to bug", 1);
		}

		for (Heuristics heuristic : Heuristics.values()) {
			Classification classification = heuristic.classify(report);
			if (classification != null) {
				heuristic.incrementCount();
				return classification;
			}
		}

		// Should never happen
		return null;
	}

	public void printStats() {
		for (Heuristics heuristic : Heuristics.values()) {
			System.out.println(heuristic.name() + ": " + heuristic.getCount());
		}
	}

	public void resetStats() {
		for (Heuristics heuristic : Heuristics.values()) {
			heuristic.reset();
		}
	}

	private enum Heuristics {
		/*
		 * Assign to the class of the report that this report is a duplicate of.
		 */
		DUPLICATE {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.DUPLICATE)) {
						User user = new User(User.UNKNOWN_USER_ID, Login.USER,
								Login.PASSWORD, Project.EVOLUTION);
						Classification prediction = Heuristics.classifier
								.getDupResolver(user, report,
										Heuristic.EVOLUTION.getClassifier());
						return new Classification(prediction
								.getClassification(), "Duplicate Heuristic - "
								+ prediction.getReason(), 1);

					}
				}
				return null;
			}
		},

		/* Assign report to submitter of approved attachment */
		PATCH {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.FIXED)) {
						List<AttachmentEvent> approvedAttachments = activity
								.getApprovedAttachments();
						if (approvedAttachments.isEmpty() == false) {
							List<String> submitters = new ArrayList<String>(
									activity
											.getAttachmentSubmitters(approvedAttachments));

							if (submitters.size() == 0) {
								/*
								 * Cannot determine who submitted the approved
								 * patch or, so choose the assigned developer
								 */
								System.err
										.println("WARNING: No submitters found for: "
												+ report.getId());
								return new Classification(Email
										.getAddress(report.getAssignedTo()),
										"Patch Submitter Unknown", 1);
							}

							if (submitters.size() == 1) {
								String submitter = submitters.get(0);
								return new Classification(Email
										.getAddress(submitter),
										"Approved Patch Heuristic", 1);
							}

							if (submitters.size() > 1) {
								String prolificSubmitter = activity
										.getMostFrequentAttachmentSubmitter();
								if (prolificSubmitter.equals("")) {
									int numSubmitters = submitters.size() - 1;
									String lastSubmitter = submitters
											.get(numSubmitters);
									return new Classification(Email
											.getAddress(lastSubmitter),
											"Fixed, Last Submitter ", 1);
								}

								return new Classification(Email
										.getAddress(prolificSubmitter),
										"Fixed, Prolific Submitter", 1);

							}
						}
					}
				}
				return null;
			}
		},
		/*
		 * If report is INVALID, it is typically intercepted by the triager
		 */
		INVALID {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.INVALID)) {

						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"Invalid Report Heuristic", 1);
					}

				}
				return null;
			}
		},
		/*
		 * If report is new (i.e. no one has accepted responsibility), then
		 * don't know who will resolve it
		 */
		NEW_BUG {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.NEW)) {
					return new Classification(
							HeuristicClassifier.CANNOT_CLASSIFY,
							"New Bug Heuristic", 1);
				}
				return null;
			}
		},
		/* If report has been assigned then label with that person */
		ASSIGNED {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.ASSIGNED)) {
					return new Classification(Email.getAddress(report
							.getAssignedTo()), "Assigned Bug Heuristic", 1);
				}
				return null;
			}
		},
		/* If report is unconfirmed, then don't know who will resolve it */
		UNCONFIRMED {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.UNCONFIRMED)) {
					return new Classification(
							HeuristicClassifier.CANNOT_CLASSIFY,
							"Unconfirmed Bug Heuristic", 1);
				}
				return null;
			}
		},
		/*
		 * If report is reopened, then don't (necessarily) know who will resolve
		 * it
		 */
		REOPENED {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.REOPENED)) {
					return new Classification(
							HeuristicClassifier.CANNOT_CLASSIFY,
							"Reopened Bug Heuristic", 1);
				}
				return null;
			}
		},
		/* Bug was fixed without a patch, so assign whomever marked it as fixed */
		FIXED {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.FIXED)) {
						List<AttachmentEvent> approvedAttachments = activity
								.getApprovedAttachments();
						if (approvedAttachments.isEmpty()) {
							return new Classification(Email
									.getAddress(resolution.getResolvedBy()),
									"Fixed, No Patch", 1);
						}
					}
				}
				return null;
			}
		},
		/*
		 * As WONTFIX is usually reached by consenus, don't know who would have
		 * fixed it
		 */
		WONT_FIX {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.WONTFIX)) {
						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"Won't Fix Heuristic", 1);
					}
				}
				return null;
			}
		},

		/*
		 * If the submitter retracted the bug, then don't know who would have
		 * fixed it
		 */
		SUBMITTER_ERROR {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.INVALID)) {
						String resolver = Email.getAddress(resolution
								.getResolvedBy());
						String submitter = Email.getAddress(report
								.getReporter());
						if (resolver.equals(submitter)) {
							return new Classification(
									HeuristicClassifier.CANNOT_CLASSIFY,
									"Submitter Error Heuristic", 1);
						}
					}
				}
				return null;
			}
		},

		/*
		 * Traiger intercepts INCOMPLETE, so can't classify
		 */
		INCOMPLETE {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.INCOMPLETE)) {

						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"Incomplete Heuristic", 1);

					}
				}
				return null;
			}
		},

		/*
		 * Typically a lot of discussion happens before a report is deemed
		 * NOTABUG, so can't make a prediction
		 */
		NOTABUG {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.NOTABUG)) {

						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"NotABug Heuristic", 1);

					}
				}
				return null;
			}
		},

		/*
		 * Typically a lot of discussion happens before a report is deemed
		 * NOTGNOME, so can't make a prediction
		 */
		NOTGNOME {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.NOTGNOME)) {

						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"NotGnome Heuristic", 1);

					}
				}
				return null;
			}
		},

		/*
		 * Typically a lot of discussion happens before a report is deemed
		 * NOTGNOME, so can't make a prediction
		 */
		OBSOLETE {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.OBSOLETE)) {

						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"NotGnome Heuristic", 1);

					}
				}
				return null;
			}
		},

		/* All other heuristics failed. */
		HEURISTIC_FAILURE {
			public Classification classify(BugReport report) {
				System.err.println("WARNING: Unable to classify report #"
						+ report.getId());
				return new Classification(
						HeuristicClassifier.HEURISTIC_FAILURE,
						"Heuristic Failure", 1);
			}
		};

		abstract Classification classify(BugReport report);

		private int count;

		public void reset() {
			this.count = 0;
		}

		public void incrementCount() {
			this.count++;
		}

		public int getCount() {
			return this.count;
		}

		protected static HeuristicClassifier classifier;

		public static void setHeuristic(HeuristicClassifier heuristic) {
			classifier = heuristic;
		}
	}
}
