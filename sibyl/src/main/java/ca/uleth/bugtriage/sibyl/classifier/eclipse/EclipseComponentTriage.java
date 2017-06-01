package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.eval.ComponentTriage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseComponentTriage {

	private static final String CLASSIFIER = Environment.getClassifierDir() + 
	/*"platform_component.classifier";*/ "eclipseComponent8month.classifier";
	private static final String[] TEST_DATA = Utils.getTestingSet(EclipseData.ECLIPSE_DIR, EclipseData.TESTING_MONTH);
	
	public static void main(String[] args) {
		ComponentTriage.run(CLASSIFIER, TEST_DATA);
	}
}
