package ca.uleth.bugtriage.sibyl.servlet.webpage;

import java.util.ArrayList;
import java.util.List;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.ValidateEmail;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class AccountSetupForm extends UBCWebpage {

	private static final String PAGE_TITLE = Sibyl.NAME + " Project Account Setup";

	private static final String PAGE_FOOTER = "";

	private static final String ACCOUNT_SETUP_SERVLET = Environment
			.getServletUrl()
			+ "accountSetup";

	private static final String SUBMIT_LABEL = "Setup Account";

	public static final String FORM_NAME = "accountSetup";

	public static final String PASSWORD_CHECK = "passwordCheck";

	public static final String PASSWORD = "password";

	public static final String USERNAME = "username";

	public static final String SIBYL_PASSWORD = "sibylPassword";

	public static final String SIBYL_PASSWORD_CHECK = "sibylPasswordCheck";

	private List<Project> excludedProjects;

	private String testField(String form, String fieldName) {
		return form + "." + fieldName + ".value != \"\"";
	}

	public AccountSetupForm() {
		this.excludedProjects = new ArrayList<Project>();
		this.excludedProjects.add(Project.UNKNOWN);
		this.excludedProjects.add(Project.EVOLUTION);
		this.excludedProjects.add(Project.GCC);
		this.excludedProjects.add(Project.FEDORA);
		//this.excludedProjects.add(Project.FIREFOX);
	}

	@Override
	protected String scripts() {
		return checkFields() + checkRepository() + ValidateEmail.get()
				+ checkPasswordSame() + checkPasswordEntered()
				+ checkPasswordEnteredTwice();
	}

	protected String checkPasswordEntered() {
		return "\n\nfunction checkPasswordEntered(password, passwordFor) {\n"
				+ "if (\n\tpassword != \"\"\n\t"
				+ "\n){\n"
				+ "\treturn (true);\n"
				+ "}\n"
				+ "\talert(\"Please provide a \" + passwordFor + \" password.\");\n"
				+ "\treturn (false);\n" + "}\n";
	}

	protected String checkPasswordEnteredTwice() {
		return "\n\nfunction checkPasswordEnteredTwice(passwordCheck, passwordFor) {\n"
				+ "if (\n\tpasswordCheck != \"\"\n){\n"
				+ "\treturn (true);\n"
				+ "}\n"
				+ "\talert(\"Please enter your \" + passwordFor + \" password twice for verification.\");\n"
				+ "\treturn (false);\n" + "}\n";
	}

	protected String checkPasswordSame() {
		return "\n\nfunction checkPasswordSame(password, passwordCheck, passwordFor){\n"
				+ "if(password != passwordCheck){\n"
				+ "\talert(\"Entered passwords for \" + passwordFor + \" do not match.\");\n"
				+ "\treturn false;\n" + "}\nreturn  true;\n}\n";
	}

	protected String checkFields() {
		return "\n\nfunction checkFields(form){\n" + "\tif(\n"
				+ "\t\tcheckProject(form) && \n"
				+ "\t\temailCheck(form.username.value) &&\n"
				+ "\t\tcheckPasswordEntered(form." + PASSWORD
				+ ".value, \"project\") && \n"
				+ "\t\tcheckPasswordEnteredTwice(form." + PASSWORD_CHECK
				+ ".value, \"the project\") && \n"
				+ "\t\tcheckPasswordSame(form." + PASSWORD + ".value, form."
				+ PASSWORD_CHECK + ".value, \"the project\") && \n"
				+ "\t\tcheckPasswordEntered(form." + SIBYL_PASSWORD
				+ ".value, \"" + Sibyl.NAME + " system\") && \n"
				+ "\t\tcheckPasswordEnteredTwice(form." + SIBYL_PASSWORD_CHECK
				+ ".value, \"" + Sibyl.NAME + " system\") && \n"
				+ "\t\tcheckPasswordSame(form." + SIBYL_PASSWORD
				+ ".value, form." + SIBYL_PASSWORD_CHECK + ".value, \""
				+ Sibyl.NAME + " system\") \n" + "){\n\t\treturn true;\n\t}"
				+ "return false;\n" + "}\n";
	}

	protected String checkRepository() {
		return "\n\nfunction checkProject(form) {\n" + "if (\n\t"
				+ testField("form", "project") + "\n){\n"
				+ "\treturn (true);\n" + "}\n"
				+ "\talert(\"Please select a project.\");\n"
				+ "\treturn (false);\n" + "}\n";
	}

	protected String accountSetup() {
		StringBuffer accountSetup = new StringBuffer();
		accountSetup.append(project(Project.UNKNOWN));
		accountSetup.append(repositoryUserName(""));
		accountSetup.append(repositoryPassword());
		accountSetup.append(sibylPassword());
		return accountSetup.toString();
	}

	protected String sibylPassword() {
		return "<p><strong>Password to log into " + Sibyl.NAME
				+ " system</strong>: " + "<input name=\"" + SIBYL_PASSWORD
				+ "\" size=\"30\" type=\"password\">"
				+ "<p>Re-enter password: " + "<input name=\""
				+ SIBYL_PASSWORD_CHECK + "\" size=\"30\" type=\"password\">";
	}

	protected String repositoryUserName(String userName) {
		return "<p><strong>User name to log into repository</strong>: "
				+ "<input name=\"" + USERNAME + "\" value = \"" + userName
				+ "\"size=\"30\" type=\"text\">";
	}

	protected String repositoryPassword() {
		return "<p><strong>Password to log into repository</strong>: "
				+ "<input name=\"" + PASSWORD
				+ "\" size=\"30\" type=\"password\">"
				+ "<p>Re-enter password: " + "<input name=\"" + PASSWORD_CHECK
				+ "\" size=\"30\" type=\"password\">";
	}

	protected String project(Project proj) {
		StringBuffer sb = new StringBuffer();
		sb.append("Project: \n" + "<select name=\"" + Project.PROJECT_ID_TAG
				+ "\">");
		for (Project project : Project.values()) {
			if (this.excludedProjects.contains(project))
				continue;

			sb.append("<option value=\"" + project.getProduct() + "\"");
			if (proj.getProduct().equals(project.getProduct())) {
				sb.append(" selected=\"selected\" ");
			}
			sb.append("\">" + project.getName());

			sb.append("</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	@Override
	protected String content(User user) {
		StringBuffer page = new StringBuffer();
		page.append(Webpage.startForm(ACCOUNT_SETUP_SERVLET, FORM_NAME,
				"return checkFields(this)"));
		page.append(Webpage.embedId(user.getId()));
		page.append(accountSetup());
		page.append(Webpage.endForm(SUBMIT_LABEL, FORM_NAME));
		page.append(notes());
		return page.toString();
	}

	private String notes() {
		StringBuffer sb = new StringBuffer();

		sb.append(Webpage.startList());
		sb.append(Webpage.listItem("Sensitive account information is stored in an encrypted format."));
		sb.append(Webpage.listItem("Your Bugzilla username and password are used to retrieve the bug report page using your Bugzilla permissions and to properly submit the changes that you make the Bugzilla server."));
		sb.append(Webpage.listItem("The Sibyl password is used to prevent other users from changing your account information."));
		sb.append(Webpage.endList());
		return sb.toString();
	}

	@Override
	protected String getPageFooter() {
		return PAGE_FOOTER;
	}

	@Override
	protected String getPageTitle() {
		return PAGE_TITLE;
	}
}
