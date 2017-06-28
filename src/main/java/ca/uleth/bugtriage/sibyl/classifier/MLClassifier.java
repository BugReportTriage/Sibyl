package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class MLClassifier extends TriageClassifier implements Serializable {

	private static final long serialVersionUID = 3258693199936631348L;

	private static final int NN = Integer.MAX_VALUE;

	protected final Classifier classifier;

	private String singleClass;

	public MLClassifier(MLAlgorithm algorithm) {
		this(algorithm.getClassifier());
	}

	protected MLClassifier(Classifier classifier) {
		this.classifier = classifier;
		this.singleClass = null;
		if (classifier instanceof IBk) {
			System.out.println("Setting NN to " + NN);
			((IBk) classifier).setKNN(NN);
		}
	}

	public String getName() {
		return this.classifier.getClass().getSimpleName();
	}

	protected void build(Instances dataset) throws Exception {
		if (dataset.numClasses() < 2) {
			if (dataset.numClasses() == 0) {
				throw new RuntimeException("Zero classes!");
			}
			System.err.println("Danger Will Robinson! This classifier only has a single class!");
			Enumeration classes = dataset.classAttribute().enumerateValues();
			this.singleClass = (String) classes.nextElement();
		} else {
			this.classifier.buildClassifier(dataset);
		}
	}

	protected List<Classification> classify(Instance instance) throws Exception {
		List<Classification> classifications = new ArrayList<Classification>();

		// Check if there is only one class
		if (this.singleClass != null) {
			classifications.add(new Classification(this.singleClass, "Single Class Prediction", 1.0));

		} else {
			double[] probs = this.classifier.distributionForInstance(instance);
			String className;
			for (int index = 0; index < this.filteredDataset.numClasses(); index++) {
				className = this.filteredDataset.classAttribute().value(index);
				classifications.add(new Classification(className, "Prediction", probs[index]));
			}
		}
		return classifications;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public static MLClassifier load(File file) throws FileNotFoundException {
		try {

			DataSource source = new DataSource(file.getAbsolutePath());
			Classifier classifier = new SMO();
			Instances trainingDataset = source.getDataSet();			
			if (trainingDataset.classIndex() == -1)
				trainingDataset.setClassIndex(trainingDataset.numAttributes() - 1);
			classifier.buildClassifier(trainingDataset);
			/*
			 * System.out.println("Reading in classifier (" + file.getPath() +
			 * ")"); ObjectInputStream in = new ObjectInputStream(new
			 * FileInputStream( file)); MLClassifier mlc = (MLClassifier)
			 * in.readObject(); System.out.println("Classifier retrieved");
			 * in.close();
			 */
			return new MLClassifier(classifier);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Should never happen
		return null;
	}

	public boolean evaluate(BugReport report, Heuristic heuristic) {
		try {
			Instance instance = this.createInstance(report, this.trainingInstances);
			this.filter.input(instance);
			instance = this.filter.output();
			Evaluation eval = new Evaluation(this.filteredDataset);
			double prediction = eval.evaluateModelOnce(this.classifier, instance);
			String className = this.filteredDataset.classAttribute().value((int) prediction);
			ca.uleth.bugtriage.sibyl.classifier.Classifier h_classifier = heuristic.getClassifier();
			String h_prediction = h_classifier.classify(report).getClassification();
			if (h_prediction.equals(className))
				System.out.print("+++>");
			else
				System.out.print("--->");
			System.out.println("c_predict: " + className + "\th_predict: " + h_prediction);
			return h_prediction.equals(className);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	public void crossValidate(int folds) {
		try {
			// Instances testInstances = this.createDataset("Cross Validate
			// Set", Utils
			// .getReports(trainingSet), new HashSet<TriageBugReport>(),
			// heuristic);
			// Instances filteredTestInstances = Filter.useFilter(testInstances,
			// this.filter);

			Evaluation evaluation = /*
									 * new CrossValidate(this.filteredDataset);
									 */new Evaluation(this.filteredDataset);
			// evaluation.evaluateModel(this.classifier, this.filteredDataset);
			System.out.println("Cross validating (" + folds + " folds)...");
			System.err.println("Nominal data: " + this.filteredDataset.classAttribute().isNominal());

			int iterations = 10;
			double accuracy = 0;
			for (int iter = 1; iter <= iterations; iter++) {
				evaluation.crossValidateModel(this.classifier, this.filteredDataset, folds, new Random());

				accuracy += evaluation.pctCorrect();
				// System.out.println(evaluation.toSummaryString(true));
				System.out.println("Accuracy: " + evaluation.pctCorrect());

			}
			System.out.println("Average accuracy: " + (accuracy / iterations));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void leaveOneOut() {
		try {
			Evaluation evaluation = new Evaluation(this.filteredDataset);
			System.out.println("Leave-one-out cross validating...");
			// System.err.println("Nominal data: " +
			// this.filteredDataset.classAttribute().isNominal());
			for (int index = 0; index < this.filteredDataset.numInstances(); index++) {
				Instances instances = new Instances(this.filteredDataset);
				instances.delete(index);
			}

			System.out.println(evaluation.toSummaryString(true));

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
