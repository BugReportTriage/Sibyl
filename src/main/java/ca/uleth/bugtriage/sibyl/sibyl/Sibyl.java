package ca.uleth.bugtriage.sibyl.sibyl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ca.uleth.bugtriage.sibyl.Classification;

public class Sibyl {
	
	public static final int SURVEY_FREQUENCY = 10;
	
	public static final int CONTROL_COUNT = 5;

	public static final Classification CONTROL_CLASSIFICATION = new Classification(
			"Control Report - No Recomendations Made", "Control Instance", 1);
	public static final Classification CLASSIFICATION_UNAVAILABLE = new Classification(
			"Recommendations Not Available - Error Occurred", "Error Occured", 1);

	public static final String NAME = "Sibyl";

	public static final DateFormat DMY_formatter = new SimpleDateFormat("d-MM-yy",
	Locale.CANADA);

	public static final String SUBMITTED_DIR_NAME = "submitted";
	
	public static final String LOG_FILE = "current.log";

	public static final String CONSENT_FILE = "consent.user";

	public static final String BACKGROUND_FILE = "background.user";

	public static final String ACCOUNT_FILE = "account.user";

	public static final String ASSIGNMENT_COUNT_FILE = "assignments.count";
/*	
	public static File createSubmittedDir(SibylUser user, Messages messages){
		File submittedDir = new File(user.getUserDataDir() + Sibyl.SUBMITTED_DIR_NAME);
		if (submittedDir.exists() == false) {
			if (submittedDir.mkdir() == false) {
				try {
					messages.add("Problem creating submit directory: "
							+ submittedDir.getCanonicalPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return submittedDir;
	}
*/
	public static final int NUM_SUBCOMPONENT_RECOMMENDATIONS = 3;

	public static final int NUM_COMPONENT_RECOMMENDATIONS = 3;

	public static final int NUM_DEVELOPER_RECOMMENDATIONS = 4;

	public static final int LOGGED_DEVELOPER_RECOMMENDATIONS = 10;

	public static final int LOGGED_SUBCOMPONENT_RECOMMENDATIONS = 6;
	
	public static final int LOGGED_CC_RECOMMENDATIONS = 14;

	public static final int LOGGED_COMPONENT_RECOMMENDATIONS = 6;
	
	public static final int DEFAULT_THRESHOLD = 9;

	public static final int NUM_CC_RECOMMENDATIONS = 7;

	
}
