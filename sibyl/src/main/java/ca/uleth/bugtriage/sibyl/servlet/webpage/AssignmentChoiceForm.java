package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class AssignmentChoiceForm {

	private static final String PAGE_TITLE = "Component/Assignment Choice Survey";

	private static final String PAGE_FOOTER = "Version: July 10, 2006";

	private static final String ASSIGNMENT_SERVLET = Environment
			.getServletUrl()
			+ "assignment";

	private static final String SUBMIT_LABEL = "Submit Information";

	private static final String FORM_NAME = "assignmentSurvey";

	private static final int TEXT_COLUMNS = 80;

	private static final int TEXT_ROWS = 2;

	private static final String[] recommenders = { "Assignment", "Component", "Sub-component"};

	public static final String NOT_APPLICABLE_LABEL = "surveyNotApplicable";

	public static String get(String userId) {
		StringBuffer page = new StringBuffer();
		page.append(Webpage.startPage(PAGE_TITLE, "", scripts()));
		page.append(disclaimer());
		page.append(Webpage.startForm(ASSIGNMENT_SERVLET, FORM_NAME,
				"return checkFields(this)"));
		page.append(Webpage.embedId(userId));
		page.append(questions());
		page.append(Webpage.endForm(SUBMIT_LABEL, FORM_NAME));

		page.append(Webpage.endPage(PAGE_FOOTER));

		return page.toString();
	}

	private static String scripts() {
		StringBuffer sb = new StringBuffer();
		sb.append(checkFields());
		for (String recommender : recommenders) {
			sb.append(checkRecommender(recommender));
			sb.append(checkRecommenderMultiple(recommender));

		}
		return sb.toString();
	}

	private static String checkRecommender(String recommender) {
		return "\n\nfunction check" + recommender + "(form){" + "if(form."
				+ recommender.toLowerCase() + "NotUsed.checked == false){ "
				+ "if(form." + recommender.toLowerCase()
				+ "NumRecommendations.value == \"\"){"
				+ "alert(\"Were the number of " + recommender.toLowerCase()
				+ " recommendations too few, reasonable, or too many ("
				+ recommender + " Recommender: Question #1)?.\");"
				+ "return false;}else{" + "return checkMultiple" + recommender
				+ "(form);" + "}" + "}else{return true;}}";
	}

	private static String checkRecommenderMultiple(String recommender) {
		return "\n\nfunction checkMultiple"
				+ recommender
				+ "(form){"
				+ "if(form."
				+ recommender.toLowerCase()
				+ "Multiple[0].checked){"
				// + "alert(\"Multiple components were applicable.\");"
				+ "if(form."
				+ recommender.toLowerCase()
				+ "Difficulty.value == \"\" || form."
				+ recommender.toLowerCase()
				+ "Reasoning.value == \"\"){"
				+ "alert(\"Please provide information about multiple applicable "
				+ recommender.toLowerCase() + " recommendations ("
				+ recommender + " Recommender: Question #2.1 and #2.2).\");"
				+ "return false;" + "} return true;" + "}else if(form."
				+ recommender.toLowerCase() + "Multiple[1].checked){"
				+ "return true;" + "} else {" + "alert(\"Were multiple "
				+ recommender.toLowerCase() + " recommendations applicable ("
				+ recommender + " Recommender: Question #2)?\");"
				+ "return false;" + "}}";
	}

	private static String checkFields() {
		return "\n\nfunction checkFields(form) {\n"
				+ "if(form." + NOT_APPLICABLE_LABEL + ".checked == false){"
				+ "if(checkComponent(form) && checkAssignment(form)){"
				+ "return true;" + "} else { "
				// + "alert(\"Please check your responses for the two
				// recommenders.\");"
				+ "return false;" + "}" + "} else {" + "return true;}" + "}";

	}

	private static String questions() {
		StringBuffer questions = new StringBuffer();
		questions.append(surveyNotApplicable());
		for (String recommender : recommenders) {
			questions.append(Webpage.DIVIDER);
			questions.append(Webpage.heading(recommender + " Recommender"));
			questions.append(recommendationsNotUsed(recommender));
			questions.append(Webpage.startList());
			questions.append(Webpage.listItem(numRecommendations(recommender)));
			questions.append(Webpage.listItem(multilpleApplicable(recommender)));
			questions.append(Webpage.endList());
		}

		return questions.toString();
	}

	private static String recommendationsNotUsed(String recommenderType) {
		return "<input name=\"" + recommenderType.toLowerCase()
				+ "NotUsed\" value=\"yes\" type=\"checkbox\"> "
				+ recommenderType + " recommender not used.</input>\n";
	}

	private static String surveyNotApplicable() {
		return "<input name=\"" + NOT_APPLICABLE_LABEL + "\" value=\"yes\" type=\"checkbox\"> Survey not applicable for change(s) made.</input>\n";
	}

	private static String multilpleApplicable(String recommenderType) {
		StringBuffer sb = new StringBuffer();

		sb.append("Were multiple "
				+ recommenderType.toLowerCase()
				+ " recommendations appropriate?\n" + "<input name=\""
				+ recommenderType.toLowerCase()
				+ "Multiple\" value=\"yes\" type=\"radio\"> Yes</input>\n"
				+ "<input name=\"" + recommenderType.toLowerCase()
				+ "Multiple\" value=\"no\" type=\"radio\"> No </input>");
		sb.append(Webpage.startList());
		sb.append(Webpage.listItem(choiceDifficulty(recommenderType)));
		sb.append(Webpage.listItem(explanation(recommenderType)));
		sb.append(Webpage.endList());
		return sb.toString();
	}

	private static String explanation(String recommenderType) {
		StringBuffer sb = new StringBuffer();

		sb.append("Please explain why. <br>\n"
				+ "<textarea name=\"" + recommenderType.toLowerCase()
				+ "Reasoning\" rows=\"" + TEXT_ROWS + "\" cols=\""
				+ TEXT_COLUMNS + "\"></textarea>");
		return sb.toString();
	}

	private static String choiceDifficulty(String recommenderType) {
		return "If \"Yes\", was choosing between them easy or difficult?\n"
						+ "<select name=\""
						+ recommenderType.toLowerCase()
						+ "Difficulty\">\n"
						+ "<option value=\"\"></option>\n"
						+ "<option value=\"veryEasy\">Very Easy</option>\n"
						+ "<option value=\"easy\">Reasonably Easy</option>\n"
						+ "<option value=\"difficult\">Reasonably Difficult</option>\n"
						+ "<option value=\"veryDifficult\">Very Difficult</option>\n"
						+ "</select>";
	}

	private static String numRecommendations(String recommenderType) {
		return "The number of "
				+ recommenderType.toLowerCase()
				+ " recommendations given were:\n" + "\n<select name=\""
				+ recommenderType.toLowerCase() + "NumRecommendations\">\n"
				+ "<option value=\"\"></option>\n"
				+ "<option value=\"many\">Too many</option>\n"
				+ "<option value=\"ok\">Reasonable</option>\n"
				+ "<option value=\"few\">Too few</option>\n" + "</select>";
	}

	private static String disclaimer() {
		return Webpage
				.message("The submission of these answers is anonymous. <br>"
						+ "Please do not put any identifying information into the text areas.");
	}
	
	public static void main(String[] args) {
		System.out.println(get("1"));
	}
}
