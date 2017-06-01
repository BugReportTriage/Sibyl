package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.ValidateEmail;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class AccountChangeForm extends AccountSetupForm {

	private static final String PAGE_TITLE = Sibyl.NAME + " Project Account Change";

	private static final String ACCOUNT_CHANGE_SERVLET = Environment
			.getServletUrl()
			+ "makeChange";

	private static final String SUBMIT_LABEL = "Change Account";

	public static final String DELIMETER = "-";

	public static final String FORM_NAME = "accountChange";

	@Override
	protected String scripts() {
		return checkFields() + checkRepository() + ValidateEmail.get()
				+ checkPasswordSame();
	}

	protected String checkFields() {
		return "\n\nfunction checkFields(form){\n" + "\tif(\n"
				+ "\t\tcheckProject(form) && \n"
				+ "\t\temailCheck(form.username.value) &&\n"
				+ "\t\tcheckPasswordSame(form." + PASSWORD + ".value, form."
				+ PASSWORD_CHECK + ".value, \"the project\") && \n"
				+ "\t\tcheckPasswordSame(form." + SIBYL_PASSWORD
				+ ".value, form." + SIBYL_PASSWORD_CHECK + ".value, \""
				+ Sibyl.NAME + " system\") \n" + "){\n\t\treturn true;\n\t}"
				+ "return false;\n" + "}\n";
	}

	private String changeAccount(User user) {
		StringBuffer accountSetup = new StringBuffer();
		accountSetup.append(Webpage.paragraph(project(user.getProject())));
		accountSetup.append(Webpage
				.paragraph(repositoryUserName(user.getName())));
		accountSetup.append(Webpage.paragraph(repositoryPassword()));
		accountSetup.append(Webpage.paragraph(sibylPassword()));
		return accountSetup.toString();
	}

	private String studyId(String studyId) {
		return Webpage.heading("Changing account information for user # "
				+ studyId);
	}

	@Override
	protected String content(User user) {
		StringBuffer page = new StringBuffer();
		page.append(studyId(user.getId()));
		page.append(Webpage.startForm(ACCOUNT_CHANGE_SERVLET, FORM_NAME,
				"return checkFields(this)"));
		page.append(Webpage.hiddenData(SibylUser.USER_ID_KEY, user.getId()));
		page.append(changeAccount(user));
		page.append(Webpage.endForm(SUBMIT_LABEL, FORM_NAME));
		page.append(Webpage.paragraph("<em>Sensitive account information is stored in an encrypted format.</em>"));
		return page.toString();
	}

	@Override
	protected String getPageTitle() {
		return PAGE_TITLE;
	}

}
