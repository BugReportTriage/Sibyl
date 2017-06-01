package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.eval.ComponentTriage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxComponentTriage {

	private static final String CLASSIFIER = Environment.getClassifierDir() + "firefoxComponent8month.classifier";
	private static final String[] TEST_DATA = Utils.getTestingSet(FirefoxData.FIREFOX_DIR, FirefoxData.TESTING_MONTH);
	
	public static void main(String[] args) {
		ComponentTriage.run(CLASSIFIER, TEST_DATA);
	}
}
