package ca.uleth.bugtriage.sibyl.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTML.Tag;

import org.eclipse.mylar.internal.bugzilla.core.HtmlTag;

import com.sun.org.apache.xpath.internal.axes.SubContextList;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.mylar.HtmlStreamTokenizer;
import ca.uleth.bugtriage.sibyl.mylar.HtmlStreamTokenizer.Token;
import ca.uleth.bugtriage.sibyl.servlet.util.Links;
import ca.uleth.bugtriage.sibyl.servlet.util.PerformanceData;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;

public class BugzillaPage {

	public static final String REPORT_SCRIPT_BUGZILLA = "show_bug.cgi";

	public static final String PROCESS_SCRIPT_BUGZILLA = "process_bug.cgi";

	public static final String BUG_LIST_SCRIPT_BUGZILLA = "buglist.cgi";

	private static final String PROCESS_BUG_SERVLET = Environment
			.getServletUrl()
			+ "processBug?";

	public static final String COMPONENT_KEY = "component";

	public static final String REPOSITORY_URL_KEY = "repositoryUrl";

	public static final String ASSIGNMENT_RECOMMENDATION_KEY = "recommendation";

	public static final String SUBCOMPONENT_RECOMMENDATION_KEY = "subcomponent_recommendation";

	public static final String OTHER_DEVELOPER = "other";

	public static final String NO_CHOICE = "none";

	public static final String ASSIGN_TO = "assigned_to";

	public static final String LOGGED_DEVELOPER_RECOMMENDATION_KEY = "logged_dev_recommendation";

	public static final String LOGGED_COMPONENT_RECOMMENDATION_KEY = "logged_comp_recommendation";

	public static final String LOGGED_SUBCOMPONENT_RECOMMENDATION_KEY = "logged_subcomp_recommendation";

	public static final String LOGGED_CC_RECOMMENDATION_KEY = "logged_cc_recommendation";

	private static final String ASSIGNMENT_RECOMMENDATIONS_ID = "assignmentRecommendations";

	private static final String SUBCOMPONENT_RECOMMENDATIONS_ID = "subcomponentRecommendations";

	public static final String CC_RECOMMENDATION_KEY = "cc_suggest";

	private StringBuffer page;

	public BugzillaPage() {
		this.page = new StringBuffer();
	}

	private static void insertHiddenData(StringBuffer page, User user,
			List<Classification> developerRecommendations,
			List<Classification> componentRecommendations,
			List<Classification> subcomponentRecommendations,
			List<Classification> ccRecommendations) {

		/* Hidden Data */
		Map<String, String> hiddenData = new TreeMap<String, String>();
		hiddenData.put(BugzillaPage.REPOSITORY_URL_KEY, user.getRepository());
		hiddenData.put(SibylUser.USER_ID_KEY, user.getId());

		if (developerRecommendations != null) {
			for (int i = 0; i < Sibyl.LOGGED_DEVELOPER_RECOMMENDATIONS
					&& i < developerRecommendations.size(); i++) {
				hiddenData.put(BugzillaPage.LOGGED_DEVELOPER_RECOMMENDATION_KEY
						+ "_" + (i + 1), developerRecommendations.get(i)
						.getClassification());
			}
		}

		if (componentRecommendations != null) {
			for (int i = 0; i < Sibyl.LOGGED_COMPONENT_RECOMMENDATIONS
					&& i < componentRecommendations.size(); i++) {
				hiddenData.put(BugzillaPage.LOGGED_COMPONENT_RECOMMENDATION_KEY
						+ "_" + (i + 1), componentRecommendations.get(i)
						.getClassification());
			}
		}

		if (subcomponentRecommendations != null) {
			for (int i = 0; i < Sibyl.LOGGED_SUBCOMPONENT_RECOMMENDATIONS
					&& i < subcomponentRecommendations.size(); i++) {
				hiddenData.put(
						BugzillaPage.LOGGED_SUBCOMPONENT_RECOMMENDATION_KEY
								+ "_" + (i + 1), subcomponentRecommendations
								.get(i).getClassification());
			}
		}

		if (ccRecommendations != null) {
			for (int i = 0; i < Sibyl.LOGGED_CC_RECOMMENDATIONS
					&& i < ccRecommendations.size(); i++) {
				hiddenData
						.put(BugzillaPage.LOGGED_CC_RECOMMENDATION_KEY + "_"
								+ (i + 1), ccRecommendations.get(i)
								.getClassification());
			}
		}

		// this.messages.add("Embedding hidden data");
		page.append("\n<!-- Start of sibyl hidden data -->\n");
		for (String key : hiddenData.keySet()) {
			page.append(Webpage.hiddenData(key, hiddenData.get(key)) + "\n");
		}
		page.append("\n<!-- End of sibyl hidden data -->\n");
	}

	public void update(User user) {
		this.update(user, null, null, null, null, null, null);
	}

	public void update(User user, String component,
			List<Classification> developers, List<Classification> components,
			List<Classification> subcomponents, List<Classification> ccs,
			PerformanceData performance) {

		String tagName;
		StringBuffer newPage = new StringBuffer();
		Reader reader = new StringReader(this.page.toString());
		HtmlStreamTokenizer tokenizer = new HtmlStreamTokenizer(reader, null);
		tokenizer.escapeTagAttributes(false);
		boolean componentTagFound = false;
		boolean setAssignmentButton = false;
		boolean insertCCs = false;

		try {
			for (Token token = tokenizer.nextToken(); token.getType() != Token.EOF; token = tokenizer
					.nextToken()) {
				if (token.getType() == Token.TAG) {
					HtmlTag tag = (HtmlTag) token.getValue();

					// Insert scripts to update recommendation
					if ((tag.getTagType() == Tag.HEAD) && (tag.isEndTag())) {
						newPage.append(scripts());
					}

					// Add trigger for when the component changes
					if ((tag.getTagType() == Tag.SELECT)
							&& (tag.isEndTag() == false)) {
						tagName = tag.getAttribute("name");
						if (tagName.equals("component")) {
							tag.setAttribute("onchange",
							/* "updateAssignmentRecommendations()"); */
							"reloadPage()");
						}
					}

					// Special case to get Eclipse banner looking right
					if ((user.getProject() == Project.PLATFORM)
							|| (user.getProject() == Project.MYLAR)) {
						if ((tag.getTagType() == Tag.TR)
								&& (tag.isEndTag() == false)) {
							String style = tag.getAttribute("style");
							if (style != null
									&& style
											.contains("url(/bugs/header_bg.gif)")) {
								tag.setAttribute("style",
										"background-image: url("
												+ user.getRepository()
												+ "/header_bg.gif);");
							}
						}
					}

					// Hyperlink
					if (((tag.getTagType() == Tag.A) || tag.getTagType() == Tag.LINK)
							&& (tag.isEndTag() == false)) {
						String href = tag.getAttribute("href");
						if (href != null) {
							if (href.startsWith("http") == false
									&& href.startsWith("mailto:") == false) {
								tag.setAttribute("href", user.getRepository()
										+ href);
							}
						}
					}

					// Image
					if (tag.getTagType() == Tag.IMG) {
						String imageSource = tag.getAttribute("src");
						if (imageSource != null) {
							if (imageSource.startsWith("http") == false) {
								URL url = new URL(user.getRepository());
								String address = url.getProtocol() + "://"
										+ url.getHost();
								tag.setAttribute("src", address + imageSource);
							}
						}
					}

					// Links to other parts of the system
					if ((tag.getTagType() == Tag.BODY)
							&& (tag.isEndTag() == false)) {
						newPage.append(token);

						// View/submit log
						newPage.append(Links.viewLog(user.getId(), true));

						// Report bug
						newPage.append(Links.bugReport(true));

						// Sibyl login
						newPage.append(Links.sibylLogin(true));

						// Performance
						if (performance != null) {
							newPage.append("<br>(Get Page: "
									+ performance.getReportTime()
									+ " sec. - Get Recommendations: "
									+ performance.getRecommendationsTime()
									+ " sec.)");
						}
						HtmlTag newTag = new HtmlTag("HR");
						newPage.append(newTag);

						continue;

					}

					if ((tag.getTagType() == Tag.FORM)
							&& (tag.isEndTag() == false)) {

						// Redirect bug report processing
						String formScript = tag.getAttribute("action");
						if (formScript.equals(PROCESS_SCRIPT_BUGZILLA)) {
							tag.setAttribute("action", PROCESS_BUG_SERVLET
									/*
									 * PROCESS_SCRIPT_BUGZILLA is necessary here
									 * for inserting hidden data. Don't remove!
									 * I did once and it nearly killed me ;) -
									 * John
									 */
									+ PROCESS_SCRIPT_BUGZILLA // 
									+ "?" + SibylUser.USER_ID_KEY + "="
									+ user.getId());
						}

						// Make search field url absolute
						if (formScript.equals(REPORT_SCRIPT_BUGZILLA)) {
							tag.setAttribute("action", user.getRepository()
									+ REPORT_SCRIPT_BUGZILLA);
						}

						// Insert hidden data
						String formName = tag.getAttribute("name");
						if (formName != null && formName.equals("changeform")) {
							newPage.append(token);
							insertHiddenData(newPage, user, developers,
									components, subcomponents, ccs);
							continue;
						}

					}

					// Developer recommendation
					if (developers != null) {
						if ((tag.getTagType() == Tag.INPUT)
								&& (tag.isEndTag() == false)) {
							tagName = tag.getAttribute("name");
							if (tagName != null) {
								if (tagName.equals("assigned_to")) {
									int knobNumber = getKnobNumber(tag);
									insertDeveloperRecommendations(developers,
											knobNumber, newPage);
								}
							}
						}
					}

					// Component recommendation
					if (components != null) {
						if ((tag.getTagType() == Tag.SELECT)
								&& (tag.isEndTag() == false)) {
							tagName = tag.getAttribute("name");
							if (tagName != null) {
								if (tag.getAttribute("name")
										.equals("component")) {
									componentTagFound = true; // Signal for
									// adding
									// subcomponent
									// recommendations
									newPage.append(token);
									insertComponentRecommendations(components,
											newPage);
									if (component != null) {
										setSelectedComponent(newPage,
												tokenizer, component);
										setAssignmentButton = true; // See Sibyl
										// bug
										// #22
									}
									continue;
								}
							}
						}
					}

					// Subcomponent recommendation
					/* Place after component */
					if (subcomponents != null && componentTagFound) {
						if ((tag.getTagType() == Tag.TD) && tag.isEndTag()) {

							insertSubcomponentRecommendations(subcomponents,
									newPage);
							newPage.append(token);
							componentTagFound = false;
							continue;
						}
					}

					// CC recommendation
					if ((tag.getTagType() == Tag.INPUT)
							&& tag.isEndTag() == false) {
						tagName = tag.getAttribute("name");
						if (tagName != null) {
							if (tagName.equals("newcc")) {
								insertCCs = true;
							}
						}
					}
					if (ccs != null && insertCCs) {
						if ((tag.getTagType() == Tag.TR) && tag.isEndTag()) {
							newPage.append(token);
							if (ccs.isEmpty() == false) {
								insertCCRecommendations(ccs, newPage);
							}
							insertCCs = false;
							continue;
						}
					}

					if (setAssignmentButton) {
						if ((tag.getTagType() == Tag.INPUT)
								&& tag.isEndTag() == false) {
							tagName = tag.getAttribute("id");
							if (tagName != null) {
								if (tag.getAttribute("id").equals(
										"knob-reassign")) {
									tag.setAttribute("checked", "checked");
								}
							}
						}
					}

					// for(int i = 0; i < tabLevel; i++){
					// newPage.append(" ");
					// }
					/*
					 * if (tag.isEndTag() || tag.isSelfTerminating()) {
					 * newPage.append(tag + "\n"); tabLevel--; } else {
					 * newPage.append("\n" + tag); tabLevel++; }
					 */
					newPage.append(token);
				} else {
					newPage.append(token);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			return;
		} catch (ParseException e) {
			e.printStackTrace();
			return;
		}

		this.page = newPage;
	}

	private void setSelectedComponent(StringBuffer newPage,
			HtmlStreamTokenizer tokenizer, String component) {
		// Set the component to match the component specified in the
		// query
		try {
			Token token;
			for (token = tokenizer.nextToken();; token = tokenizer.nextToken()) {
				if (token.getType() == Token.TAG) {
					HtmlTag tag = (HtmlTag) token.getValue();
					if (tag.getTagType() == Tag.SELECT && tag.isEndTag())
						break;
					if ((tag.getTagType() == Tag.OPTION)
							&& (tag.isEndTag() == false)) {
						String value = tag.getAttribute("value");
						if (value.equals(component)) {
							newPage.append("\n<OPTION value=\"" + value
									+ "\" selected>");
						} else {
							newPage
									.append("\n<OPTION value=\"" + value
											+ "\">");
						}
						continue;
					}
				}
				newPage.append(token.toString());
			}
			newPage.append(token.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String scripts() {
		StringBuffer scripts = new StringBuffer();
		scripts
				.append("\n\n<script language=\"JavaScript\" type=\"text/javascript\">"
						+ reloadPage() + insertSubcomponent() + "</script>");
		return scripts.toString();

	}

	private String insertSubcomponent() {
		return "\n\nfunction insertSubcomponent(){\n"
				+ "var summaryElement = document.getElementsByName(\"short_desc\")[0];\n"
				+ "var subcomponentElement = document.getElementById(\""
				+ SUBCOMPONENT_RECOMMENDATIONS_ID
				+ "\");\n"
				+ "var selectedIndex = subcomponentElement.selectedIndex;\n"
				+ "var subcomponent = subcomponentElement.options[selectedIndex].value;\n"
				+ "var summary = summaryElement.value;\n"
				+ "var prefixText = \"\";\n"
				+ "var subcomponentStartBracketIndex = summary.indexOf(\"[\");\n"
				+ "if(subcomponentStartBracketIndex != -1){\n"
				+ "prefixText = summary.substring(0, subcomponentStartBracketIndex);\n"
				+ "}\n"
				+ "var subcomponentEndBracketIndex = summary.indexOf(\"]\");\n"
				+ "if(subcomponentEndBracketIndex != -1){\n"
				+ "var summaryText = summary.substring(subcomponentEndBracketIndex + 1);\n"
				+ "summaryElement.value = prefixText + \"[\" + subcomponent + \"]\" + summaryText;\n"
				+ "}\n"
				+ "else\n"
				+ "{\n"
				+ "summaryElement.value = prefixText + \"[\" + subcomponent + \"] \" + summary;\n"
				+ "}\n" + "}\n";
	}

	private String reloadPage() {
		return "\n\nfunction reloadPage(){\n"
				+ "var componentElement = document.getElementById(\"component\");\n"
				+ "var selectedIndex = componentElement.selectedIndex;\n"
				+ "var component = componentElement.options[selectedIndex].value;\n"
				+ "var head = location.href.match(/.*"
				+ SibylUser.USER_ID_KEY
				+ "=\\d+/);\n"
				+ "var tail = location.href.match(/&http.*/);\n"
				+
				// "alert(head);" +
				// "alert(tail);\n" +
				"var newUrl = encodeURI(head + \"&"
				+ BugzillaPage.COMPONENT_KEY + "=\" + component + tail);\n" +
				// "alert(newUrl);\n" +
				"location.href = newUrl;\n" + "}\n";
	}

	private String scripts(List<Classification> assignmentRecommendations) {
		StringBuffer scripts = new StringBuffer();
		scripts
				.append("<script language=\"JavaScript\" type=\"text/javascript\">"
						+ recommendations(assignmentRecommendations)
						+ recommendationObject()
						+ insertAssignmentRecommendations()
						+ getAssignmentRecommendations()
						+ newRecommendations()
						+ "</script>");
		return scripts.toString();

	}

	private String recommendations(List<Classification> recommendations) {
		StringBuffer recommendationsVar = new StringBuffer();
		recommendationsVar.append("\n\n var allRecommendations = new Array(\n");
		for (Classification recommendation : recommendations) {
			recommendationsVar.append("new recommendation(" + "\""
					+ recommendation.getClassification() + "\"" + "," + "\""
					+ recommendation.getReason() + "\"" + ","
					+ recommendation.getProbability() + "),\n");
		}
		recommendationsVar = new StringBuffer(recommendationsVar.subSequence(0,
				recommendationsVar.length() - 2));
		recommendationsVar.append(")\n");
		return recommendationsVar.toString();
	}

	private String recommendationObject() {
		return "\n\n function recommendation(person, component, probability){\n"
				+ "this.developer = person;\n"
				+ "this.component = component;\n"
				+ "this.probability = probability;\n" + "}\n";
	}

	private String getAssignmentRecommendations() {
		return "\n\nfunction getRecommendations(component){\n"
				+ "var componentRecommendations = new Array();\n"
				+ "for(index in allRecommendations){\n"
				+ "if(allRecommendations[index].component == component){\n"
				+ "componentRecommendations.push(allRecommendations[index]);\n"
				+ "}\n" + "}\n" + "return componentRecommendations;" + "}\n";
	}

	private String insertAssignmentRecommendations() {
		return "\n\nfunction updateAssignmentRecommendations(){\n"
				+ "var r = new recommendation(\"john\",\"web\",1);\n"
				+ "var component = document.getElementById(\"component\").value;\n"
				+ "var newRecommendations = getRecommendations(component);\n"
				+ "insertRecommendations(newRecommendations);\n" + "}\n";
	}

	private String newRecommendations() {
		return "\n\nfunction insertRecommendations(recommendations){\n"
				+ "var recommendationList = document.getElementById(\""
				+ ASSIGNMENT_RECOMMENDATIONS_ID
				+ "\");\n"
				+ "for(index in recommendationList.childNodes){\n"
				+ "alert(\"Removing: \" + recommendationList[index].nodeName);"
				+ "//recommendationList.remove(recommendationList[index]);\n"
				+ "}\n"
				+ "for(index in recommendations){\n"
				+ "var recommendation = document.createElement(\"option\");\n"
				+ "recommendation.setAttribute(\"value\",recommendations[index].developer);\n"
				+ "recommendationList.appendChild(recommendation);\n" + "}\n"
				+ "}";
	}

	private int getKnobNumber(HtmlTag tag) {
		String javascript = tag.getAttribute("onchange");
		if (javascript != null) {
			Pattern knobPattern = Pattern.compile("knob\\[(\\d+)\\]");
			Matcher matcher = knobPattern.matcher(javascript);
			if (matcher.find()) {
				int knob = Integer.parseInt(matcher.group(1));
				return knob;
			}
		}

		System.err.println("Couldn't determine knob number, defaulting to 3");
		return 3;
	}

	private void insertComponentRecommendations(
			List<Classification> recommendations, StringBuffer newPage) {
		String component;
		HtmlTag componentTag, componentTagEnd;

		try {
			newPage.append("\n");

			componentTag = new HtmlTag("OPTION");
			componentTagEnd = new HtmlTag("/OPTION");

			for (int i = 0; i < Sibyl.NUM_COMPONENT_RECOMMENDATIONS
					&& i < recommendations.size(); i++) {
				Classification recommendation = recommendations.get(i);
				component = recommendation.getClassification();
				if (recommendation.equals(Sibyl.CLASSIFICATION_UNAVAILABLE)
						|| recommendation.equals(Sibyl.CONTROL_CLASSIFICATION)) {
					componentTag.setAttribute("value", BugzillaPage.NO_CHOICE);
				} else {
					componentTag.setAttribute("value", component);
				}
				newPage.append(componentTag);
				newPage.append(component);
				newPage.append(componentTagEnd);
			}

			if (recommendations.isEmpty() == false) {
				componentTag.setAttribute("value", BugzillaPage.NO_CHOICE);
				newPage.append(componentTag);
				newPage.append("------------");
				newPage.append(componentTagEnd);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insertSubcomponentRecommendations(
			List<Classification> recommendations, StringBuffer newPage)
			throws ParseException {

		if (recommendations.isEmpty())
			return;

		if (recommendations.size() == 1) {
			Classification prediction = recommendations.get(0);
			if (prediction.getClassification().equals(
					HeuristicClassifier.CANNOT_CLASSIFY))
				return;
		}

		Classification classification;
		HtmlTag choice, choiceEnd, selectionStart, selectionEnd, dataStart, dataEnd;

		dataStart = new HtmlTag("TD");
		dataEnd = new HtmlTag("/TD");

		// newPage.append(dataEnd);
		// newPage.append(dataStart);
		// newPage.append("<b>Sub-component</b>: ");
		// newPage.append(dataEnd);
		// newPage.append(dataStart);
		selectionStart = new HtmlTag("SELECT");
		selectionStart.setAttribute("name",
				BugzillaPage.SUBCOMPONENT_RECOMMENDATION_KEY);
		selectionStart.setAttribute("id",
				BugzillaPage.SUBCOMPONENT_RECOMMENDATIONS_ID);
		selectionStart.setAttribute("onchange", "insertSubcomponent()");
		newPage.append(selectionStart);

		choice = new HtmlTag("OPTION");
		choice.setAttribute("value", BugzillaPage.NO_CHOICE);
		newPage.append(choice);
		newPage.append("(Choose Subcomponent)");
		choiceEnd = new HtmlTag("/OPTION");
		newPage.append(choiceEnd);

		for (int i = 0; i < Sibyl.NUM_SUBCOMPONENT_RECOMMENDATIONS
				&& i < recommendations.size(); i++) {
			classification = recommendations.get(i);
			choice = new HtmlTag("OPTION");
			choice.setAttribute("value", classification.getClassification());
			newPage.append(choice);
			newPage.append(classification.getClassification());
			newPage.append(choiceEnd);
		}

		selectionEnd = new HtmlTag("/SELECT");
		newPage.append(selectionEnd);
	}

	private void insertDeveloperRecommendations(
			List<Classification> recommendations, int knob, StringBuffer newPage)
			throws ParseException {
		Classification classification;
		String name;
		HtmlTag choice, choiceEnd, selectionStart, selectionEnd;

		selectionStart = new HtmlTag("SELECT");
		selectionStart.setAttribute("name",
				BugzillaPage.ASSIGNMENT_RECOMMENDATION_KEY);
		selectionStart.setAttribute("id",
				BugzillaPage.ASSIGNMENT_RECOMMENDATIONS_ID);
		selectionStart.setAttribute("onchange", "document.changeform.knob["
				+ knob + "].checked=true;");
		newPage.append(selectionStart);

		choice = new HtmlTag("OPTION");
		choice.setAttribute("value", BugzillaPage.NO_CHOICE);
		newPage.append(choice);
		newPage.append("(Choose Developer)");
		choiceEnd = new HtmlTag("/OPTION");
		newPage.append(choiceEnd);

		for (int i = 0; i < Sibyl.NUM_DEVELOPER_RECOMMENDATIONS
				&& i < recommendations.size(); i++) {
			classification = recommendations.get(i);
			choice = new HtmlTag("OPTION");
			choice.setAttribute("value", classification.getClassification());
			newPage.append(choice);
			newPage.append(classification.getClassification());
			newPage.append(choiceEnd);
		}

		choice = new HtmlTag("OPTION");
		choice.setAttribute("value", BugzillaPage.OTHER_DEVELOPER);
		newPage.append(choice);
		newPage.append("Other");
		newPage.append(choiceEnd);

		selectionEnd = new HtmlTag("/SELECT");
		newPage.append(selectionEnd);
	}

	private void insertCCRecommendations(List<Classification> recommendations,
			StringBuffer newPage) throws ParseException {
		Classification classification;
		HtmlTag choice, choiceEnd, selectionStart, selectionEnd, rowStart, rowEnd, dataStart, dataEnd;

		rowStart = new HtmlTag("TR");
		rowEnd = new HtmlTag("/TR");
		dataStart = new HtmlTag("TD");
		dataEnd = new HtmlTag("/TD");

		newPage.append(rowStart);

		dataStart.setAttribute("align", "right");
		dataStart.setAttribute("valign", "top");
		newPage.append(dataStart);
		newPage.append("<b>Recommended:</b>: ");
		newPage.append(dataEnd);

		dataStart = new HtmlTag("TD");
		dataStart.setAttribute("valign", "top");
		newPage.append(dataStart);
		selectionStart = new HtmlTag("SELECT");
		selectionStart.setAttribute("name", BugzillaPage.CC_RECOMMENDATION_KEY);
		selectionStart.setAttribute("multiple", "multiple");
		selectionStart.setAttribute("size", String
				.valueOf(Sibyl.NUM_CC_RECOMMENDATIONS));
		// selectionStart.setAttribute("onchange", "insertSubcomponent()");
		newPage.append(selectionStart);

		choiceEnd = new HtmlTag("/OPTION");
		for (int i = 0; i < Sibyl.NUM_CC_RECOMMENDATIONS
				&& i < recommendations.size(); i++) {
			classification = recommendations.get(i);
			choice = new HtmlTag("OPTION");
			choice.setAttribute("value", classification.getClassification());
			newPage.append(choice);
			newPage.append(classification.getClassification());
			newPage.append(choiceEnd);
		}

		selectionEnd = new HtmlTag("/SELECT");
		newPage.append(selectionEnd);

		newPage.append(dataEnd);
		newPage.append(rowEnd);
	}

	public void get(InputStream inputStream, Messages msgs) {
		URL bugReportURL = null;
		BufferedReader in = null;
		try {
			this.page = new StringBuffer();
			in = new BufferedReader(new InputStreamReader(inputStream));
			for (String line = new String(); line != null; line = in.readLine()) {
				this.page.append(line + "\n");
			}
		} catch (MalformedURLException e) {
			msgs.add("Sorry, the URL " + bugReportURL + " is malformed");
		} catch (FileNotFoundException e) {
			msgs.add("Sorry, the URL " + bugReportURL
					+ " could not be accessed");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					msgs
							.add("BugzillaPage.get(): Problem closing input stream.");
					e.printStackTrace();
				}
		}
	}

	@Override
	public String toString() {
		return this.page.toString();
	}

}
