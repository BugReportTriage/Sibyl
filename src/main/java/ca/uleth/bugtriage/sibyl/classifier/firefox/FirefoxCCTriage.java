package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.eval.CCTriage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class FirefoxCCTriage {

	private static final String CLASSIFIER = Environment.getClassifierDir() + "firefoxCC.classifier";
	
	public static void main(String[] args) {
		CCTriage.run(CLASSIFIER, Utils.getTestingSet(FirefoxData.FIREFOX_DIR, FirefoxData.TESTING_MONTH));
	}
}
