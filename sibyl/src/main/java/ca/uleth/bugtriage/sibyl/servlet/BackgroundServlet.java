package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.servlet.util.Recorder;
import ca.uleth.bugtriage.sibyl.servlet.webpage.AccountSetupForm;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UBCWebpage;

public class BackgroundServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 7761592274588919130L;

	@Override
	protected String createPage() {
		Recorder.recordBackground(this.user, this.data, this.messages);
		UBCWebpage page = new AccountSetupForm();
		return page.createPage(this.user);
	}
}
