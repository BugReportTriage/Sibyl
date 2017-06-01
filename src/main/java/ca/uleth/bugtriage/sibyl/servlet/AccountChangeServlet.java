package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.servlet.webpage.AccountChangeForm;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UBCWebpage;

public class AccountChangeServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 4651750712677584756L;

	@Override
	protected String createPage() {

		UBCWebpage page = new AccountChangeForm();
		return page.createPage(this.user);

	}

	
}
