package ca.uleth.bugtriage.sibyl.misc;

public class KnobFinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String javascript = "document.changeform.knob[3].checked=true;";
		java.util.regex.Pattern knobPattern = java.util.regex.Pattern
				.compile("knob\\[(\\d+)\\]");
		int knob = -1;
		java.util.regex.Matcher matcher = knobPattern.matcher(javascript);
		if (matcher.find()) {

			knob = Integer.parseInt(matcher.group(1));
		}
		System.out.println(knob);

	}

}
