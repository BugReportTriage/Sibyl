package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class BackgroundForm {

	private static final String PAGE_TITLE = Sibyl.NAME
			+ " Project Background Survey";

	private static final String PAGE_FOOTER = "Version: May 30, 2006";

	private static final String BACKGROUND_SERVLET = Environment
			.getServletUrl()
			+ "background";

	private static final String SUBMIT_LABEL = "Submit Information";

	private static final String FORM_NAME = "background";

	private static final String[] jobFunctions = { "Application Developer",
			"QA/Testing", "Program Director", "CIO/CTO", "VP Development",
			"System Integrator", "Application Architect", "Project Manager",
			"Student", "Faculty", "Business Analyst", "Database Administrator",
			"Other" };

	public static String get(String userId) {
		StringBuffer page = new StringBuffer();
		page.append(Webpage.startPage(PAGE_TITLE, "", scripts()));

		page.append(disclaimer());

		page.append(Webpage.startForm(BACKGROUND_SERVLET, FORM_NAME,
				"return checkFields(this)"));
		page.append(Webpage.embedId(userId));
		page.append(questions());
		page.append(Webpage.endForm(SUBMIT_LABEL, FORM_NAME));

		page.append(Webpage.endPage(PAGE_FOOTER));

		return page.toString();
	}

	private static String testField(String form, String fieldName) {
		return form + "." + fieldName + ".value != \"\"";
	}

	private static String scripts() {
		return testFields() + testJob() + testExperience("Project", 4)
				+ testExperience("Programming", 2) + testIsDeveloper()
				+ testExperience("Triage", 5) + testWorkload()
				+ testTriagers("Project", 7) + testTriagers("Component", 8)
				+ testReasoning();
	}

	private static String testFields() {

		return "\n\nfunction checkFields(form) {\n" + "return "
				+ "checkJob(form)" + " && \n\t"
				+ "checkProjectExperience(form)" + " && \n"
				+ "checkProgrammingExperience(form)" + " && \n"
				+ "checkIsDeveloper(form)" + " && \n"
				+ "checkTriageExperience(form)" + " && \n"
				+ "checkWorkload(form)" + " && \n"
				+ "checkProjectTriagers(form)" + " && \n"
				+ "checkComponentTriagers(form)" + " && \n"
				+ "checkReasoning(form)" + ";}\n";
	}

	private static String testReasoning() {
		return "function checkReasoning(form){\n"
				+ "if("
				+ testField("form", "reasoning")
				+ " == false){\n"
				+ "alert(\"Please provide information about your reasoning when you make an assignment. (Question #9).\");\n"
				+ "return false;\n" + "}\n" + "return true;\n" + "}\n";
	}

	private static String testWorkload() {
		return "function checkWorkload(form){\n"
				+ "if("
				+ testField("form", "workload")
				+ " == false){\n"
				+ "alert(\"Please provide information about how much time you spend triaging (Question #6).\");\n"
				+ "return false;\n" + "}\n" + "return true;\n" + "}\n";
	}

	private static String testIsDeveloper() {
		return "function checkIsDeveloper(form){\n"
				+ "if(form.developer[0].checked == false && form.developer[1].checked == false){\n"
				+ "alert(\"Please indicate whether or not you are a developer for this project. (Question #3).\");\n"
				+ "return false;\n" + "}\n " + "return true;\n" + "}\n";
	}

	private static String testJob() {
		return "function checkJob(form){\n"
				+ "if("
				+ testField("form", "job")
				+ " == false){\n"
				+ "alert(\"Please provide information about your job function (Question #1).\");\n"
				+ "return false;\n" + "}\n" + "return true;\n" + "}\n";
	}

	private static String testExperience(String type, int questionNum) {
		return "function check" + type + "Experience(form){\n" + "if("
				+ testField("form", type.toLowerCase() + "Experience")
				+ " == false){\n" + "alert(\"Please provide information on "
				+ type.toLowerCase() + " experience (Question #" + questionNum
				+ ").\");\n" + "return false;\n" + "}\n" + "return true;\n"
				+ "}\n";
	}

	private static String testTriagers(String type, int questionNum) {
		return "function check" + type + "Triagers(form){\n" + "if(("
				+ testField("form", type.toLowerCase() + "Triagers")
				+ " == false) && form.num" + type
				+ "TriagersNotKnown.checked == false){"
				+ "alert(\"Please provide information on the number of "
				+ type.toLowerCase() + " triagers (Question #" + questionNum
				+ ")\");\n" + "return false;\n" + "}\n" + "return true;\n"
				+ "}";
	}

	private static String reasoning() {
		return "When assigning bug reports, what criteria do you use? <br>"
				+ "Why is a report assigned to a particular person?<br>"
				+ "<textarea name=\"reasoning\" rows=\"15\" cols=\"60\">"
				+ "</textarea>";
	}

	private static String projectTriagers() {
		return "How many triagers are there for your project?<br>"
				+ "<input name=\"projectTriagers\" size=\"3\" type=\"text\">"
				+ "<input name=\"numProjectTriagersNotKnown\" value=\"yes\" type=\"checkbox\">I don't know.</input>";
	}

	private static String componentTriagers() {
		return "How many triagers are there for your component?<br>"
				+ "<input name=\"componentTriagers\" size=\"3\" type=\"text\">"
				+ "<input name=\"numComponentTriagersNotKnown\" value=\"yes\" type=\"checkbox\">I don't know.</input>";
	}

	private static String workload() {
		return "How many hours a week do you spend triaging?<br>"
				+ "<input name=\"workload\" size=\"3\" type=\"text\">";
	}

	private static String triageExperience() {
		return "How many years of triage experience do you have?<br>"
				+ "<input name=\"triageExperience\" size=\"3\" type=\"text\"> ";

	}

	private static String projectDeveloper() {
		return "Are you a developer for the project?<br>"
				+ "<input name=\"developer\" value=\"yes\" type=\"radio\"> Yes "
				+ "<input name=\"developer\" value=\"no\" type=\"radio\"> No ";
	}

	private static String programmingExperience() {
		return "How many years of programming experience do you have?<br>"
				+ "<input name=\"programmingExperience\" size=\"3\" type=\"text\">";
	}

	private static String projectExperience() {
		return "How many years have you worked with this project?<br>"
				+ "<input name=\"projectExperience\" size=\"3\" type=\"text\">";
	}

	private static String questions() {
		StringBuffer questions = new StringBuffer();
		questions.append(Webpage.startList());
		questions.append(Webpage.paragraph(Webpage.listItem(jobFunction())));
		questions.append(Webpage.paragraph(Webpage
				.listItem(programmingExperience())));
		questions.append(Webpage
				.paragraph(Webpage.listItem(projectDeveloper())));
		questions.append(Webpage.paragraph(Webpage
				.listItem(projectExperience())));
		questions.append(Webpage
				.paragraph(Webpage.listItem(triageExperience())));
		questions.append(Webpage.paragraph(Webpage.listItem(workload())));
		questions
				.append(Webpage.paragraph(Webpage.listItem(projectTriagers())));
		questions.append(Webpage.paragraph(Webpage
				.listItem(componentTriagers())));
		questions.append(Webpage.paragraph(Webpage.listItem(reasoning())));
		questions.append(Webpage.endList());
		return questions.toString();
	}

	private static String jobFunction() {
		StringBuffer sb = new StringBuffer();
		sb.append("What is your job function?\n");
		sb.append("<br>" + jobList());
		return sb.toString();
	}

	private static String jobList() {
		StringBuffer sb = new StringBuffer();
		sb.append("<select name=\"job\">");
		sb.append("<option value=\"\" selected></option>");
		for (String job : jobFunctions) {
			sb.append("<option value=\"" + job + "\">" + job + "</option>");
		}
		sb.append("</select>");
		return sb.toString();
	}

	private static String disclaimer() {
		return "<H4>The submission of these answers is anonymous.<br>\n "
				+ "Please do not put any identifying information into the text areas.</H4>"
				+ "<br>";
	}
}
