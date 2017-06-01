package ca.uleth.bugtriage.sibyl.classifier.evolution;

import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EvolutionComponentClassifier {

	public static void create(int numMonths) {

		Heuristic heuristic = Heuristic.COMPONENT;
		String[] trainingSet = Utils.getTrainingSet(EvolutionData.EVOLUTION_DIR, numMonths, EvolutionData.LAST_TRAINING_MONTH);
		String[] testingSet = Utils.getTestingSet(EvolutionData.EVOLUTION_DIR, EvolutionData.TESTING_MONTH);
		
		TriageClassifier classifier = Classifier.create(ClassifierType.SVM, trainingSet,
				testingSet, EvolutionData.DEVELOPER_INFO, heuristic,
				null, false);
		Classifier.saveClassifier("evolutionComponent" + numMonths + "month", classifier);
		
	}
}
