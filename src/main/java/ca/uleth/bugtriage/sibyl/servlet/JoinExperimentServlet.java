package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.servlet.webpage.ConsentForm;

public class JoinExperimentServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 8788787137723987454L;

	@Override
	protected String createPage() {
		return ConsentForm.get();
	}

}
