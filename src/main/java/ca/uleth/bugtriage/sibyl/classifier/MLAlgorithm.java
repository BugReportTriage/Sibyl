package ca.uleth.bugtriage.sibyl.classifier;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayesMultinomial;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.rules.ConjunctiveRule;
import weka.classifiers.trees.J48;

public enum MLAlgorithm {
	SVM(new SMO()), C45(new J48()), NAIVE_BAYES(new NaiveBayesMultinomial()), NN(new IBk()), RULES(new ConjunctiveRule());

	private Classifier classifier;

	private MLAlgorithm(Classifier classifier) {
		this.classifier = classifier;
	}

	public Classifier getClassifier() {
		return this.classifier;
	}

}
