package ca.uleth.bugtriage.sibyl.heuristic;

import java.util.ArrayList;
import java.util.List;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.activity.BugActivity;
import ca.uleth.bugtriage.sibyl.activity.events.AttachmentEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Email;

public class MozillaHeuristic extends HeuristicClassifier {

	public MozillaHeuristic() {
		Heuristics.setHeuristic(this);
	}

	public Classification classifyReport(BugReport report) {
		
		/*
		 * False bug - someone testing Bugzilla (discovered purely by accident)
		 * May 30/05
		 */
		if (report.getId() == 288759) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"The Bug That Should Never Have Been", 1);
		}

		/*
		 * Circular duplicates of bugs (281536 and 281535) and (244067 and
		 * 244068)
		 */
		if (report.getId() == 281536 || report.getId() == 244068) {
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Bug with circular dependency", 1);
		}
		
		if(false){ // only fixed reports
		ResolutionEvent resolution = report.getActivity().getResolution();
		if (resolution != null
				&& resolution.getType().equals(ResolutionType.FIXED) == false && resolution
						.getType().equals(ResolutionType.DUPLICATE) == false) {
			 //System.out.println("not fixed: " + resolution.getType());
			return new Classification(HeuristicClassifier.CANNOT_CLASSIFY,
					"Not Fixed Heuristic", 1);
		}}
		
		if(report.getId() == 318396){
			System.out.println("Pause");
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
						
						// FIXME
						
						//User user = new User(User.UNKNOWN_USER_ID, Login.USER,
							//	Login.PASSWORD, Project.FIREFOX);
						//Classification prediction = Heuristics.classifier
							//	.getDupResolver(user, report, Heuristic.MOZILLA
								//		.getClassifier());
						//return new Classification(prediction
							//	.getClassification(), "Duplicate Heuristic - "
								//+ prediction.getReason(), 1);

					}
				}
				return null;
			}
		},
		/*
		 * A WORKSFORME report is usually intercepted by a triager, so don't
		 * know which developer would have fixed it
		 */
		WORKSFORME {
			public Classification classify(BugReport report) {
				BugActivity activity = report.getActivity();
				ResolutionEvent resolution = activity.getResolution();
				if (resolution != null) {
					if (resolution.getType().equals(ResolutionType.WORKSFORME)) {
						return new Classification(
								HeuristicClassifier.CANNOT_CLASSIFY,
								"Works For Me", 1);
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
							return new Classification(Email
									.getAddress(resolution.getResolvedBy()),
									"Invalid Report Heuristic", 1);
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
		SUBITTER_ERROR {
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

//	public static void main(String[] args) {
//		Classifier heuristic = Heuristic.MOZILLA.getClassifier();
//
//		BugReport report = Utils.getReport(Project.FIREFOX.getURL(),
//				Login.USER, Login.PASSWORD, 326186);
//		Classification prediction = heuristic.classify(report);
//		System.out.println(prediction.getClassification() + ": " + prediction.getReason());
//	}
}
