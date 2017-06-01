package ca.uleth.bugtriage.sibyl.heuristic;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Email;
import ca.uleth.bugtriage.sibyl.utils.LoginInfo;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class GCCHeuristic extends HeuristicClassifier {

	public GCCHeuristic() {
		Heuristics.setHeuristic(this);
	}

	@Override
	public Classification classifyReport(BugReport report) {
		if (report.equals(BugReport.ACCESS_DENIED_BUG)) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Access denied to bug", 1);
		}

		// Invalid reports
		if (report.getId() == 17888) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Not a bug", 1);
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

	@Override
	public void printStats() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();

	}

	@Override
	public void resetStats() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
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
						HeuristicClassifier classifier = Heuristics.classifier;
						User user = new User(User.UNKNOWN_USER_ID, Login.USER,
								Login.PASSWORD, Project.GCC);
						Classification prediction = Heuristics.classifier
								.getDupResolver(user, report,
										Heuristic.GCC.getClassifier());
						return new Classification(prediction
								.getClassification(), "Duplicate Heuristic - "
								+ prediction.getReason(), 1);

					}
				}
				return null;
			}
		},

		WORKSFORME {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.WORKSFORME)) {
						String resolver = Email.getAddress(resolution
								.getResolvedBy());
						String submitter = Email.getAddress(report
								.getReporter());
						if (resolver.equals(submitter) == false) {
							return new Classification(resolver, "Works For Me",
									1);
						}
					}
				}
				return null;
			}
		},

		/*
		 * If report is INVALID, assign to whomever made the decision, unless it
		 * was a submitter error
		 */
		INVALID {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.INVALID)) {
						String resolver = Email.getAddress(resolution
								.getResolvedBy());
						String submitter = Email.getAddress(report
								.getReporter());
						if (resolver.equals(submitter) == false) {
							// Check to see if it is spam
							String component = report.getComponent();
							if (component.toLowerCase().equals("spam") == false) {
								return new Classification(
										Email.getAddress(resolution
												.getResolvedBy()),
										"Invalid Report Heuristic", 1);
							}
							return new Classification(
									HeuristicClassifier.CANNOT_CLASSIFY,
									"Invalid Report (Spam) Heuristic", 1);
						}
					}
				}
				return null;
			}
		},
		/*
		 * If report is new (i.e. no one has accepted responsibility), then
		 * don't know who will resolve it
		 */
		NEW {
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
					// System.err.println("ASSIGNED CASE");
					return new Classification(Email.getAddress(report
							.getAssignedTo()), "Assigned Bug Heuristic", 1);
				}
				return null;
			}
		},
		WAITING {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.WAITING)) {
					BugActivity activity = report.getActivity();
					return new Classification(Email.getAddress(activity
							.getWhoSetStatus()), "WAITING Bug Heuristic", 1);
				}
				return null;
			}
		},

		SUSPENDED {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.SUSPENDED)) {
					BugActivity activity = report.getActivity();
					return new Classification(Email.getAddress(activity
							.getWhoSetStatus()), "SUSPENDED Bug Heuristic", 1);
				}
				return null;
			}
		},

		/* If report is unconfirmed, then don't know who will resolve it */
		UNCONFIRMED {
			public Classification classify(BugReport report) {
				if (report.getStatus().equals(StatusType.UNCONFIRMED)) {
					// System.err.println("UNCONFIRMED CASE");
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
					// System.err.println("REOPENED CASE: " + report.getId());
					return new Classification(
							HeuristicClassifier.CANNOT_CLASSIFY,
							"Reopened Bug Heuristic", 1);
				}
				return null;
			}
		},
		/* Assign whomever marked it as fixed */
		FIXED {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.FIXED)) {
						String resolver = null;// CVSCommit.getCommitter(report);

						if (resolver == null) {
							resolver = Email.getAddress(resolution
									.getResolvedBy());
						}

						return new Classification(resolver, "Fixed", 1);
					}
				}

				return null;
			}
		},
		/*
		 * Mark with whomever said that they wont fix the bug
		 */
		WONT_FIX {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.WONTFIX)) {
						String resolver = Email.getAddress(resolution
								.getResolvedBy());
						String submitter = Email.getAddress(report
								.getReporter());
						if (resolver.equals(submitter) == false) {
							return new Classification(Email
									.getAddress(resolution.getResolvedBy()),
									"Won't Fix Heuristic", 1);
						}
					}
				}
				return null;
			}
		},

		/*
		 * If the submitter retracted the bug, then don't know who would have
		 * fixed it
		 */
		SUBITTER_ERROR {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.INVALID)
							|| resolution.getType().equals(
									ResolutionType.WORKSFORME)
							|| resolution.getType().equals(
									ResolutionType.WONTFIX)) {
						String resolver = Email.getAddress(resolution
								.getResolvedBy());
						String submitter = Email.getAddress(report
								.getReporter());
						if (resolver.equals(submitter)) {
							// System.err.println("Submitter Error Case: " +
							// report.getId());
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
		 * Handle bugs that are marked as fixed, but activity only shows closed
		 * (e.g. 8270, 8620, 5133), even though CLOSED is not a valid status
		 */
		CLOSED {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if ((report.getStatus().equals(StatusType.RESOLVED) || report
						.getStatus().equals(StatusType.CLOSED))
						&& resolution == null) {
					System.err.println("RESOLVED/CLOSED: " + report.getId());
					return new Classification(Email.getAddress(activity
							.getWhoSetStatus()), "Reopened Bug Heuristic", 1);
				}

				return null;
			}
		},
		/* All other heuristics failed. */
		HEURISTIC_FAILURE {
			public Classification classify(BugReport report) {
				System.err.println("WARNING: Unable to classify report #"
						+ report.getId() + " Status: " + report.getStatus());
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
