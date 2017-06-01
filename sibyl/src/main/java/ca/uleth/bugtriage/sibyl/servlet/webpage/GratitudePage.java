package ca.uleth.bugtriage.sibyl.servlet.webpage;

import java.util.Random;

import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;

public class GratitudePage {

	private static final String[] THANKS_IMAGES = { "racoon_thanks.jpg",
			"tophat_thanks.jpg", "dog_thanks.gif", /*"guy_girl_dog_thanks.jpg",*/
			"scribble_thanks.png", /*"simple_thanks.jpg"*/ };

	private static final String GRATITUDE_HEADER = "Thank you.";

	private static final String GRATITUDE_FOOTER = "";

	private final static Random rand = new Random();

	public static String get(String message) {
		StringBuffer gratitude = new StringBuffer();
		gratitude.append(Webpage.startPage(GRATITUDE_HEADER));

		// Flip a coin to choose gratitude image
		String imageFile = THANKS_IMAGES[rand.nextInt(THANKS_IMAGES.length)];
		
		gratitude.append("<center><img alt=\"Thank You\" src=\"" + imageFile
				+ "\"></center>");

		gratitude.append(Webpage.paragraph(Webpage.message("<center>"
				+ message + "</center>")));
		gratitude.append(Webpage.endPage(GRATITUDE_FOOTER));
		return gratitude.toString();
	}
}
