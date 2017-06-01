package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.eval.CCTriage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseCCTriage {

	private static final String CLASSIFIER = Environment.getClassifierDir() + "eclipseCC.classifier";
	
	public static void main(String[] args) {
		CCTriage.run(CLASSIFIER, Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH));
	}
}
