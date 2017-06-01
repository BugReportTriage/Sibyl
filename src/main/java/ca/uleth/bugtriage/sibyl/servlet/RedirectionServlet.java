package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.servlet.webpage.RedirectPage;


public class RedirectionServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 8471887262627629741L;

	@Override
	protected String createPage() {
		
		return  RedirectPage.get(this.query);
	}
}
