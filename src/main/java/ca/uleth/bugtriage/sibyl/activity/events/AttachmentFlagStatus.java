package ca.uleth.bugtriage.sibyl.activity.events;

public enum AttachmentFlagStatus {
	REVIEW("review"), /* Firefox */
	NEEDINFO("needinfo"), /* Firefox */
	QE_VERIFY("qe-verify"), /* Firefox */
	BACKLOG("firefox-backlog"), /* Firefox */
	TEST_SUITE("in-testsuite"),  /* Firefox */
	
	UNKNOWN("UNKNOWN")
	// SUPERREVIEW /* Firefox */,
	// APPROVAL /* Firefox */,
	// UI /* Firefox */,
	// BRANCH /* Firefox */,
	// OBSOLETE,
	// COMMITTED /* Gnome */,
	// ACCEPTED /* Gnome accepted-committ_now*/,
	// COMMENTED /* Gnome */,
	// NONE /* Gnome */,
	// REJECTED/* Gnome */,
	;

	public final String label;

	private AttachmentFlagStatus(String s) {
		label = s;
	}
}
