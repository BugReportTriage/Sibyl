package ca.uleth.bugtriage.sibyl.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Email {

	public static final String INVALID = "INVALID";
	private static final Pattern EMAIL_SUFFIX = Pattern
			.compile("@(\\w+-?)+(\\.\\w+)+");

	private static final Pattern EMAIL = Pattern
			.compile("(\\w+-?\\+?\\.?)+@(\\w+-?)+(\\.\\w+)+");
	
	private static final Pattern BUGZILLA_EMAIL = Pattern.compile("&lt;(\\w|\\W)");

	private static String match(Pattern pattern, String string){
		String match = null;
		try {
			Matcher matcher = pattern.matcher(string);
			/* Take the last match */
			while (matcher.find()) {
				match = matcher.group();
			}
			if (match == null) {
				match = "INVALID";
				System.err.println("WARNING: (Match null)" + string);
			}
		} catch (Exception ex) {
			System.err.println("Error getting match from " + string);
			ex.printStackTrace();
		}
		return match;
	}
	
	public static String getAddress(String str) {

		if(str.length() == 0){
			System.err.println("Address empty");
			return Email.INVALID;
		}
		
		String address = null;
		try {
			Matcher matcher = EMAIL.matcher(str);
			/* Take the last match of an email address */
			while (matcher.find()) {
				address = matcher.group();
			}
			// System.out.println(address);
			if (address == null) {
				address = Email.INVALID;
				System.err.println("WARNING: (Address null)" + str);
			}
		} catch (Exception ex) {
			System.err.println("Error getting address from " + str);
			ex.printStackTrace();
		}
		return address;
	}
	
	public static String getSuffix(String str) {
		return match(EMAIL_SUFFIX, str);
	}
	
	public static void main(String[] args) {
		String string = "<input name=\"assigned_to\" size=\"32\"             onchange=\"if ((this.value != 'jkanvik\\x40gmail.com') &&";
		//string = HtmlStreamTokenizer.unescape(string);
		try {
			string = URLDecoder.decode(string, "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(string + " " + getAddress(string));
		
	}
}
