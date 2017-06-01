package ca.uleth.bugtriage.sibyl.servlet.util;

import java.text.ParseException;

import javax.swing.text.html.HTML.Tag;

import org.eclipse.mylar.internal.bugzilla.core.HtmlTag;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;

public class Webpage {

	public static final String METHOD_POST = "POST";

	public static final String ENCODE_FORMAT = "UTF-8";

	public static final String URL_ENCODING = "application/x-www-form-urlencoded";

	public static final String TITLE_TAG = "<title>";

	private static final String BANNER_SIZE = "H2";

	private static final String HEADING_SIZE = "H3";

	private static final String MESSAGE_SIZE = "H4";

	public static final String DIVIDER = "<HR>";

	public static final String HREF = "href";

	public static final String HTTP = "http";

	public static final String IMG = "img";

	public static String startPage(String title) {
		return startPage(title, "", "");
	}

	public static String startPage(String title, String meta, String scripts) {
		return "<html>\n"
				+ "<head>\n"
				+ meta
				+ "<title>"
				+ title
				+ "</title>\n"
				+ "<script language=\"JavaScript\" type=\"text/javascript\">"
				+ scripts
				+ "</script>"
				+ "</head>\n"
				+ "<body>\n"
				+ "<table class=\"MsoNormalTable\""
				+ "style=\"width: 100%; border-collapse: collapse;\" border=\"0\""
				+ "cellpadding=\"0\" cellspacing=\"0\" width=\"100%\">\n"
				+ "<tbody>\n"
				+ "<tr style=\"\">\n"
				+ "<td style=\"padding: 0in;\">\n"
				+ "<img alt=\"UBC Logo\" src=\"logo.jpg\""
				+ "style=\"width: 80px; height: 107px;\"><br>\n"
				+ "</td>\n"
				+ "<td style=\"padding: 0in;\">\n"
				+ "<p style=\"text-align: right;\" align=\"right\">\n"
				+ "UBC Department of Computer Science<br>\n"
				+ "201-2366 Main Mall, Vancouver, B.C., V6T 1Z4<br>\n"
				+ "<a href=\"http://www.cs.ubc.ca\">http://www.cs.ubc.ca</a><br>\n"
				+ "Tel: (604) 822-3061 Fax: (604) 822-5485</p>\n" + "</td>\n"
				+ "</tr>\n" + "</table>\n"
				+ "<h1 style=\"text-align: center;\">" + title + "</h1>\n";
	}

	public static String endPage(String footer) {
		return "<hr align=\"center\" size=\"2\" width=\"100%\">\n" + "<p><i>"
				+ footer + "</i></p>\n" + "</body>\n" + "</html>\n";

	}

	public static String startForm(String formHandlerURL, String name,
			String formValidation) {
		return "<form " + "name=\"" + name + "\"action=\"" + formHandlerURL
				+ "\"" + " method=\"" + Webpage.METHOD_POST + "\" enctype=\""
				+ Webpage.ENCODE_FORMAT + "\" onSubmit = \"" + formValidation
				+ "\">\n";
	}

	public static String endForm(String label, String name) {
		return "<p>\n" + "<input value=\"" + label + "\" name=\"" + name
				+ "\" type=\"submit\">\n</p>\n</form>\n";
	}

	/* Example: <input type="hidden" name="delta_ts" value="20060505104639"> */
	public static String hiddenData(String name, String value) {
		return "<input type=\"hidden\" name=\"" + name + "\" value=\"" + value
				+ "\">";
	}

	public static String embedId(String id) {
		return hiddenData(SibylUser.USER_ID_KEY, id);
	}

	public static String sectionHeader(String header) {
		return "<h2>" + header + "</h2>";
	}

	public static String paragraph(String text) {
		return paragraph(text, false);
	}
	
	public static String paragraph(String text, boolean centered) {
		StringBuffer sb = new StringBuffer();
		if(centered){
			sb.append("<center>");
		}
		sb.append("<p>" + text + "</p>");
		if(centered){
			sb.append("</center>");
		}
		return sb.toString();
	}

	public static String startList() {
		return "<ol>\n";
	}

	public static String endList() {
		return "</ol>\n";
	}

	public static String listItem(String text) {
		return "<li>" + text + "</li>\n";
	}

	public static String link(String link, String linkText) {
		return link(link, linkText, null, true);
	}

	public static String link(String link, String linkText, String icon,
			boolean bold) {

		HtmlTag newTag;
		StringBuffer sb = new StringBuffer();

		try {
			if (icon != null) {
				newTag = new HtmlTag(Tag.IMG.toString());
				newTag.setAttribute("src", icon);
				newTag.setAttribute("height", "20");
				newTag.setAttribute("width", "20");
				// newTag.setAttribute("align", "middle");
				newTag.setAttribute("hspace", "5");
				sb.append(newTag);
			}

			if (bold) {
				newTag = new HtmlTag("strong");
				sb.append(newTag);
			}
			newTag = new HtmlTag("A");

			newTag.setAttribute("href", link);
			sb.append(newTag);

			sb.append(linkText);

			if (bold) {
				newTag = new HtmlTag("/strong");
				sb.append(newTag);
			}
			newTag = new HtmlTag("/A");
			sb.append(newTag);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sb.toString();
	}

	public static String heading(String text) {
		return "<" + HEADING_SIZE + ">" + text +"</" + HEADING_SIZE + ">\n";
	}
	
	public static String banner(String text) {
		return "<" + BANNER_SIZE + ">" + text +"</" + BANNER_SIZE + ">\n";
	}
	
	public static String message(String text) {
		return "<" + MESSAGE_SIZE + ">" + text +"</" + MESSAGE_SIZE + ">\n";
	}
}
