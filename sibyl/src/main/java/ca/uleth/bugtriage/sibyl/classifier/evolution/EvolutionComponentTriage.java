package ca.uleth.bugtriage.sibyl.classifier.evolution;

import ca.uleth.bugtriage.sibyl.eval.ComponentTriage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EvolutionComponentTriage {

	private static final String CLASSIFIER = Environment.getClassifierDir() + 
	/*"platform_component.classifier";*/ "evolutionComponent8month.classifier";
	private static final String[] TEST_DATA = Utils.getTestingSet(EvolutionData.EVOLUTION_DIR, EvolutionData.TESTING_MONTH);
	
	public static void main(String[] args) {
		ComponentTriage.run(CLASSIFIER, TEST_DATA);
	}
}
