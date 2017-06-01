package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class RecommenderForm {

	private static final String PAGE_TITLE = "Assignment/Component/CC Recommendation Survey";

	private static final String PAGE_FOOTER = "Version: July 11, 2006";

	private static final String RECOMMENDER_SERVLET = Environment
			.getServletUrl()
			+ "recommender";

	private static final String SUBMIT_LABEL = "Submit Information";

	private static final int TEXT_COLUMNS = 80;

	private static final int TEXT_ROWS = 2;

	private static final String FORM_NAME = "recommenderSurvey";

	private static final String[] recommenders = { "Assignment", "Component", "Sub-component", "CC"};

	public static String get(String userId) {
		StringBuffer page = new StringBuffer();
		page.append(Webpage.startPage(PAGE_TITLE, "", scripts()));
		page.append(disclaimer());
		page.append(Webpage.DIVIDER);
		page.append(Webpage.startForm(RECOMMENDER_SERVLET + "?"
				+ SibylUser.USER_ID_KEY + "=" + userId, FORM_NAME,
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
		sb.append(checkComponent());
		sb.append(checkAssignment());
		for (String recommender : recommenders) {
			sb.append(checkUseful(recommender));
			sb.append(checkDifferences(recommender));
		}
		sb.append(checkOthers());
		sb.append(checkFaster());
		return sb.toString();
	}

	private static String checkUseful(String recommender) {
		return "\n\nfunction check" + recommender + "Useful(form){"
				+ "if(form." + recommender.toLowerCase()
				+ "Useful.value == \"\"){"
				+ "alert(\"Please indicate the usefulness of the "
				+ recommender.toLowerCase() + " recommender. (" + recommender + " Recommender: Question #1)\");"
				+ "return false;}" + "return true;" + "}";

	}

	private static String checkDifferences(String recommender) {
		return "\n\nfunction check" + recommender + "Difference(form){"
				+ "if(form." + recommender.toLowerCase()
				+ "Expectations[0].checked){" + "" + "if(form."
				+ recommender.toLowerCase() + "Reasoning.value == \"\"){"
				+ "alert(\"Please elaborate about your expectations for the "
				+ recommender.toLowerCase() + " recommender.\");"
				+ "return false;" + "} return true;" + "} else if(form."
				+ recommender.toLowerCase() + "Expectations[1].checked){"
				+ "return true;} else{ "
				+ "alert(\"Please answer about your expectations for the "
				+ recommender.toLowerCase() + " recommender. (" + recommender + " Recommender: Question #"
				+ (recommender.equals(recommenders[0]) ? 2 : 4) + ")\");"
				+ "return false;}" + "}";
	}

	private static String checkComponent(){
		return "\n\nfunction checkComponent(form){\n" +
				"if(form.componentNotUsed.checked == false){" +
				"return checkComponentUseful(form) && "
				+ "checkComponentDifference(form);\n}else{return true;}\n}\n";
	}
	
	private static String checkAssignment(){
		return "\n\nfunction checkAssignment(form){\n" +
				"if(form.assignmentNotUsed.checked == false){" +
				"return checkAssignmentUseful(form) && "
				+ "checkFaster(form) && checkConsiderOthers(form) && "
				+ "checkAssignmentDifference(form);\n}else{return true;}\n}\n";
	}
	
	private static String checkFields() {
		return "\n\nfunction checkFields(form) {\n"
				+ "if(checkComponent(form) && checkAssignment(form))"
				+ "{" + "return true;"
				+ "}else{ " // + "alert(\"Please fill in the survey.\"); "
				+ "return false;" + "}}";
	}

	private static String checkOthers() {
		return "\n\nfunction checkConsiderOthers(form){"
				+ "if(form.considerOthers[0].checked == false && form.considerOthers[1].checked == false){"
				+ "alert(\"Please answer the question about considering other developers. (Assignment Recommender: Question #3)\");"
				+ "return false;" + "}" + "return true;" + "}";
	}

	private static String checkFaster() {
		return "\n\nfunction checkFaster(form){"
				+ "if(form.faster[0].checked == false && form.faster[1].checked == false){"
				+ "alert(\"Please answer the question about the assignment recommender making assignment faster. (Assignment Recommender: Question #2)\");"
				+ "return false;" + "}" + "return true;" + "}";
	}

	private static String questions() {
		StringBuffer questions = new StringBuffer();
		
		questions.append(Webpage.heading("Assignment Recommender"));
		questions.append(Webpage
				.paragraph(recommendationsNotUsed("Assignment")));
		questions.append(Webpage.startList());
		questions.append(Webpage.paragraph(Webpage
				.listItem(usefulRecommendations("assignment"))));
		questions.append(Webpage.paragraph(Webpage.listItem(makeFaster())));
		questions.append(Webpage.paragraph(Webpage.listItem(considerOther())));
		questions.append(Webpage.paragraph(Webpage
				.listItem(expectations("assignment"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(comments("assignment"))));
		questions.append(Webpage.endList());
		
		questions.append(Webpage.DIVIDER + "\n");
		
		questions.append(Webpage.heading("Component Recommender"));
		questions
				.append(Webpage.paragraph(recommendationsNotUsed("Component")));
		questions.append(Webpage.startList());
		questions.append(Webpage.paragraph(Webpage
				.listItem(usefulRecommendations("component"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(expectations("component"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(comments("component"))));
		questions.append(Webpage.endList());

		questions.append(Webpage.DIVIDER + "\n");
		
		questions.append(Webpage.heading("Sub-component Recommender"));
		questions
				.append(Webpage.paragraph(recommendationsNotUsed("Sub-component")));
		questions.append(Webpage.startList());
		questions.append(Webpage.paragraph(Webpage
				.listItem(usefulRecommendations("sub-component"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(expectations("sub-component"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(comments("sub-component"))));
		questions.append(Webpage.endList());
		
		questions.append(Webpage.DIVIDER + "\n");
		
		questions.append(Webpage.heading("CC Recommender"));
		questions
				.append(Webpage.paragraph(recommendationsNotUsed("CC")));
		questions.append(Webpage.startList());
		questions.append(Webpage.paragraph(Webpage
				.listItem(usefulRecommendations("CC"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(expectations("CC"))));
		questions.append(Webpage.paragraph(Webpage
				.listItem(comments("CC"))));
		questions.append(Webpage.endList());
		return questions.toString();
	}

	private static String comments(String recommenderType) {
		return "Do you have any additional comments about using the "
				+ recommenderType + " recommender?<br>" + "<textarea name=\""
				+ recommenderType + "Comments\" rows=\"" + TEXT_ROWS
				+ "\" cols=\"" + TEXT_COLUMNS + "\">" + "</textarea>";
	}

	private static String explanation(String recommenderType) {
		return "If \"Yes\", please elaborate. <br>" + "<textarea name=\""
				+ recommenderType + "Reasoning\" rows=\"" + TEXT_ROWS
				+ "\" cols=\"" + TEXT_COLUMNS + "\">" + "</textarea>";
	}

	private static String expectations(String recommenderType) {
		StringBuffer sb = new StringBuffer();
		sb
				.append("Are there any systematic differences between your expectations and what was recommended by the "
						+ recommenderType
						+ " recommender?"
						+ "<input name=\""
						+ recommenderType
						+ "Expectations\" value=\"yes\" type=\"radio\"> Yes "
						+ "<input name=\""
						+ recommenderType
						+ "Expectations\" value=\"no\" type=\"radio\"> No ");
		sb.append(Webpage.startList());
		sb.append(explanation(recommenderType));
		sb.append(Webpage.endList());
		return sb.toString();
	}

	private static String considerOther() {
		return "Does the assignment recommender make you consider a relevant developer you might not have previously considered?"
				+ "<input name=\"considerOthers\" value=\"yes\" type=\"radio\"> Yes "
				+ "<input name=\"considerOthers\" value=\"no\" type=\"radio\"> No ";
	}

	private static String makeFaster() {
		return "Does the assignment recommender make it faster to do assignment?"
				+ "<input name=\"faster\" value=\"yes\" type=\"radio\"> Yes "
				+ "<input name=\"faster\" value=\"no\" type=\"radio\"> No ";
	}

	private static String usefulRecommendations(String recommender) {
		return "How useful are the " + recommender + " recommendations?\n"
				+ "<select name=\"" + recommender + "Useful\">"
				+ "<option value=\"\"></option>"
				+ "<option value=\"veryUseful\">Very Useful</option>"
				+ "<option value=\"useful\">Reasonably Useful</option>"
				+ "<option value=\"notUseful\">Not Useful</option>"
				+ "</select>";
	}

	private static String disclaimer() {
		return Webpage.message("The submission of these answers is anonymous.<br> "
				+ "Please do not put any identifying information into the text areas.");
	}

	private static String recommendationsNotUsed(String recommenderType) {
		return "<input name=\"" + recommenderType.toLowerCase()
				+ "NotUsed\" value=\"yes\" type=\"checkbox\"> "
				+ recommenderType + " recommender not used.</input>\n";
	}
	
	public static void main(String[] args) {
		System.out.println(get("1"));
	}
}
