package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.servlet.webpage.LoginPage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UBCWebpage;

public class LoginServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 5136097186739199650L;


	@Override
	protected UBCWebpage getPage() {
		return new LoginPage();
	}
}
