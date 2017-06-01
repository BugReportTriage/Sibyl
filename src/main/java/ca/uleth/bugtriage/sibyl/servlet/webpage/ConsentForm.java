package ca.uleth.bugtriage.sibyl.servlet.webpage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import ca.uleth.bugtriage.sibyl.ValidateEmail;
import ca.uleth.bugtriage.sibyl.servlet.ConsentServlet;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.utils.Environment;

public class ConsentForm {

	private static final String PAGE_TITLE = Sibyl.NAME + " Project Consent Form";

	private static final String PAGE_FOOTER = "Version: April 20, 2006";

	private static final String CONSENT_SERVLET = Environment.getServletUrl()
			+ "consent";

	private static final String SUBMIT_LABEL = "I Agree";

	private static final String FORM_NAME = "consent";

	public static String get() {
		StringBuffer sb = new StringBuffer();

		sb.append(Webpage.startPage(PAGE_TITLE, "", scripts()));
		sb.append(Webpage.paragraph(invitation()));
		sb.append(Webpage.paragraph(procedures()));
		sb.append(Webpage.paragraph(identity()));
		sb.append(Webpage.paragraph(contact()));

		sb.append(Webpage.DIVIDER);

		sb.append(Webpage.startForm(CONSENT_SERVLET, FORM_NAME,
				"return emailCheck(this." + ConsentServlet.EMAIL_KEY
						+ ".value)"));
		sb.append(Webpage.paragraph(consent()));
		sb.append(name());
		sb.append(email());
		sb.append(date());
		sb.append(Webpage.endForm(SUBMIT_LABEL, FORM_NAME));

		sb.append(Webpage.endPage(PAGE_FOOTER));

		return sb.toString();

	}

	private static String scripts() {
		return ValidateEmail.get();
	}

	private static String consent() {
		return "By clicking \"I Agree\" below you acknowledge that you have received this consent form, "
				+ "and are consenting to participate in the study.  "
				+ "To participate please fill in your name and email address.  "
				+ "Incomplete submissions will be discarded.  "
				+ "Note that your name and email address will not be associated with the study ID "
				+ "that you are given.";
	}

	private static String name() {
		return Webpage
				.paragraph("Your <strong>full</strong> name: <input name=\""
						+ ConsentServlet.NAME_KEY + "\">");
	}

	private static String email() {
		return Webpage.paragraph("Email address: <input name=\""
				+ ConsentServlet.EMAIL_KEY + "\">");
	}

	private static String date() {
		DateFormat formatter = new SimpleDateFormat("MMMM dd, yyyy");
		String today = formatter.format(new Date(System.currentTimeMillis()));
		return Webpage.paragraph("Date: " + today
				+ Webpage.hiddenData(ConsentServlet.DATE_KEY, today));
	}

	private static String contact() {
		return "You are free to stop participating in the study at any time without penalty. "
				+ "If you have any questions about the data being collected or about the study in general "
				+ "please contact John Anvik at <a href=\"mailto:%28janvik@cs.ubc.ca\">janvik@cs.ubc.ca</a> or (604-822-0193). "
				+ "If you have any concerns about your treatment or rights as a research subject, "
				+ "you may contact the Research Subject Information Line in the UBC Office of Research Services "
				+ "at the University of British Columbia, at 604-822-8598.  This study is funded by NSERC and IBM.";
	}

	private static String identity() {
		return "Your identity will remain confidential, "
				+ "known only to the investigators at UBC. "
				+ "In any publications that arise from this study, you will be identified anonymously. "
				+ "All data will be kept digitally on password protected computer systems. "
				+ "After the study, the collected data will be stripped of all information identifying "
				+ "the participants, and we will use this data to determine the effectiveness of the tool "
				+ "in assigning bug reports.  This data will be anonymous and not contain any identification "
				+ "information.";
	}

	private static String procedures() {
		return "This study is being conducted by John Anvik <a href=\"mailto:%28janvik@cs.ubc.ca\">(janvik@cs.ubc.ca</a>) as a part of his Ph.D. "
				+ "thesis and is supervised by Dr. Gail Murphy (<a href=\"mailto:murphy@cs.ubc.ca\">murphy@cs.ubc.ca</a>).  "
				+ "The study involves using a tool that recommends bug report assignments. "
				+ "The bug reports that you view and your assignment choices will be recorded in a "
				+ "file on a server. You will be asked periodically to submit this file to the study. "
				+ "You may view and delete this file from the server at any time.  "
				+ "No identifying information about you or about the computer on which you are working "
				+ "is recorded or transmitted. Occasionally, you will be prompted to answer a very short "
				+ "questionnaire about your assignment choice for a specific bug report, and can choose "
				+ "whether or not to do so. When asked to submit data to the study, you will also be "
				+ "prompted to answer a longer questionnaire about your experience using the tool, "
				+ "and again can chose whether or not to do so. We will use this data to determine the "
				+ "effectiveness of the tool in assigning bug reports.  "
				+ "This data will be anonymous and will not contain any identification information. "
				+ "Your name and email will be kept confidential and will only be used for the purposes "
				+ "of reimbursement. "
				+ "There are no known risks associated with this study. ";
	}

	private static String invitation() {
		return "You have been invited to participate in this study because "
				+ "you are a triager for a software project who assigns bug reports to developers. "
				+ "The purpose of the study is to determine the effectiveness of our bug assignment "
				+ "recommender. The use of the tool occurs during your normal triage activities "
				 + "over the course of three months. "
				//+ "over a period of time up to three months. "
				+ "You will be reimbursed with a $20 e-gift card from "
				+ "Amazon for your participation. "
				+ "Those who withdraw from the study but have made 100 assignments using the tool will be reimbursed with a $10 e-gift card.";
	}

	public static void main(String[] args) {
		System.out.println(ConsentForm.get());
	}
}
