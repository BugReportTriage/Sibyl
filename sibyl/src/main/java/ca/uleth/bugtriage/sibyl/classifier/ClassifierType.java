package ca.uleth.bugtriage.sibyl.classifier;

public enum ClassifierType {
	SVM(new MLClassifier(MLAlgorithm.SVM)), 
	NAIVE_BAYES(new MLClassifier(MLAlgorithm.NAIVE_BAYES)), 
	C45(new MLClassifier(MLAlgorithm.C45)),
	NN(new MLClassifier(MLAlgorithm.NN)), 
	RULES(new MLClassifier(MLAlgorithm.RULES)),
	COMPONENT_BASED(new ComponentClassifier(MLAlgorithm.SVM)), 
	NAIVE(new NaiveTriageClassifier()), 
	CC(new CCClassifier(MLAlgorithm.SVM));

	private TriageClassifier classifier;

	private ClassifierType(TriageClassifier classifier) {
		this.classifier = classifier;
	}

	public TriageClassifier getClassifier() {
		return this.classifier;
	}
}
