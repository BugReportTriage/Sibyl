package ca.uleth.bugtriage.sibyl.servlet;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.servlet.util.Links;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.DirectoryPage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.InvalidPasswordPage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.LoginPage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UBCWebpage;
import ca.uleth.bugtriage.sibyl.servlet.webpage.UnknownUserPage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class DirectoryServlet extends AbstractRecommenderServlet {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 3475595605787687825L;

	@Override
	protected UBCWebpage getPage() {
		if (this.user.getName().equals(User.UNKNOWN_USER_NAME)) {
			return new UnknownUserPage();
		}

		String providedPassword = this.data.get(LoginPage.PASSWORD_KEY);
		String userPassword = getPassword();
		if (providedPassword.equals(userPassword)) {
			return new DirectoryPage();
		}
		return new InvalidPasswordPage();
	}

	private String getPassword() {
		try {
			return Utils.getPassword(this.user.getSibylPasswordFile());
		} catch (PasswordNotFoundException e) {
			e.printStackTrace();
		}
		
		return "";
	}

}
