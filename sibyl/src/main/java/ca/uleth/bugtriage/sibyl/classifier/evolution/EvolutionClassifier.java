package ca.uleth.bugtriage.sibyl.classifier.evolution;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifier;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EvolutionClassifier {

	public static void create(int numMonths) {

		ClassifierType classifierType = ClassifierType.SVM;
		// ClassifierType classifierType = ClassifierType.COMPONENT_BASED;

		Heuristic heuristic = Heuristic.EVOLUTION;
		String dupSet = EvolutionData.DUPLICATES;
		heuristic.getClassifier().setDataset(dupSet);

		String[] trainingSet = Utils.getTrainingSet(
				EvolutionData.EVOLUTION_DIR, numMonths,
				EvolutionData.LAST_TRAINING_MONTH);
		Profiles profile = Classifier.createDeveloperProfiles(trainingSet,
				Project.EVOLUTION);

		TriageClassifier classifier = Classifier.create(classifierType,
				trainingSet, Utils.getTestingSet(EvolutionData.EVOLUTION_DIR,
						EvolutionData.TESTING_MONTH),
				EvolutionData.DEVELOPER_INFO, heuristic, profile, false);
		heuristic.getClassifier().writeDupsToDownload(dupSet);
		Classifier
				.saveClassifier("evolution" + numMonths + "month", classifier);
	}
}
