package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.webpage.BackgroundForm;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;

public class ConsentServlet extends AbstractRecommenderServlet {

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = 3343469150421526197L;

	public static final String NAME_KEY = "SubjectName";

	public static final String EMAIL_KEY = "Email";

	public static final String DATE_KEY = "Date";

	@Override
	protected String createPage() {

		String name = this.data.get(NAME_KEY);
		String email = this.data.get(EMAIL_KEY);
		String date = this.data.get(DATE_KEY);

		String studyId = SibylUser.getNewId(this.messages);
		this.user = new SibylUser(studyId);
		this.user.createDataDir();
		Recorder.recordConsent(this.user, name, email, date, this.messages);
		return BackgroundForm.get(this.user.getId());
	}
}
