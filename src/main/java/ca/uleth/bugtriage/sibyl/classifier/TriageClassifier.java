package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.eval.DeveloperInfo;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.report.Comment;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.

		ChiSquaredAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SparseInstance;
import weka.core.converters.ArffSaver;
import weka.core.stopwords.Rainbow;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public abstract class TriageClassifier {

	private final static String DESCRIPTION = "Description";

	private final static String COMPONENT = "Component";

	private final static String OS = "OS";

	private final static String VERSION = "Version";

	private final static String HARDWARE = "Hardware";

	private final static String CLASS = "Class";

	private static final boolean USE_SUMMARY = true;

	private static final boolean USE_DESCRIPTION = true;

	private static final boolean USE_COMMENTS = false;

	private static final boolean USE_COMPONENT = false;

	private static final boolean USE_OS = false;

	private static final boolean USE_VERSION = false;

	private static final boolean USE_HARDWARE = false;

	private static final boolean USE_ATTRIBUTE_SELECTION = true;

	private static final boolean USE_INFO_GAIN = false;

	// Don't use PCA! Takes far too long! (Quit classifier-creation job after 16
	// hours)
	private static final boolean USE_PRINCIPAL_COMPONENTS = false;

	private static final boolean USE_CHI_SQUARED = true;

	protected final Filter filter;

	protected Instances trainingInstances;

	protected Instances filteredDataset;

	protected final FrequencyTable classFrequency;

	protected transient Profiles profile;

	private final List<Integer> trainingIds;

	private final AttributeSelection selector;

	private ASSearch searcher;

	private ASEvaluation evaluator;

	private Instances testingInstances;

	public TriageClassifier() {
		super();
		this.classFrequency = new FrequencyTable();
		this.profile = null;
		this.trainingIds = new ArrayList<Integer>();

		// Set up filter
		this.filter = new StringToWordVector();
		((StringToWordVector) this.filter).setStopwordsHandler(new Rainbow());
		((StringToWordVector) this.filter).setLowerCaseTokens(true);

		// System.err.println("Using IDF");
		((StringToWordVector) this.filter).setIDFTransform(true);
		((StringToWordVector) this.filter).setTFTransform(true);
		/*
		 * Normalizing via document length appears to have the most effect,
		 * likley because of the low information content of most fetures
		 */
		((StringToWordVector) this.filter).setNormalizeDocLength(
				new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));

		/*
		 * TODO Use n-gram tokenizer NGramTokenizer t = new NGramTokenizer();
		 * t.setNGramMaxSize(maxGrams); t.setNGramMinSize(minGrams);
		 * filter.setTokenizer(t);
		 */

		// System.err.println("Using alphebetic tokens");
		// ((StringToWordVector) this.filter).setOnlyAlphabeticTokens(true);

		this.selector = new AttributeSelection();		
		if (USE_INFO_GAIN)
			this.evaluator = new InfoGainAttributeEval();
		if (USE_PRINCIPAL_COMPONENTS)
			this.evaluator = new PrincipalComponents();
		if (USE_CHI_SQUARED)
			this.evaluator = new ChiSquaredAttributeEval();

		this.searcher = new Ranker();
		((Ranker) this.searcher).setThreshold(0);

	}

	public abstract String getName();

	protected abstract void build(Instances dataset) throws Exception;

	protected abstract List<Classification> classify(Instance instance) throws Exception;

	public void setProfile(Profiles profile) {
		this.profile = profile;
	}

	/*
	 * @ return list of predictions with probablilites
	 */
	public List<Classification> classify(BugReport report) {
		{
			try {
				Instance bugInstance = this.createInstance(report, this.trainingInstances);

				this.filter.input(bugInstance);
				bugInstance = this.filter.output();

				if (USE_ATTRIBUTE_SELECTION)
					bugInstance = this.selector.reduceDimensionality(bugInstance);

				List<Classification> classifications = this.classify(bugInstance);
				Collections.sort(classifications, Collections.reverseOrder());
				return classifications;
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}
	}

	private Instances filterInformationContent(Instances dataset) {

		Instances filteredData = dataset;
		try {
			System.out.println("Attributes: " + dataset.numAttributes());

			this.selector.setEvaluator(this.evaluator);
			this.selector.setSearch(this.searcher);

			this.selector.SelectAttributes(dataset);
			System.out.println("Attributes selected: " + this.selector.numberAttributesSelected());

			filteredData = this.selector.reduceDimensionality(dataset);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return filteredData;
	}

	protected Instances filterTrainingData(Instances data) {

		try {
			//System.out.println("Filtering dataset");
			this.filter.setInputFormat(data);
			Instances filteredData = Filter.useFilter(data, this.filter);
			filteredData.compactify();
			return filteredData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null; // never gets here
	}

	private void train() throws Exception {
		// Filter instances
		//System.out.println("Instances: " + this.trainingInstances.numInstances());
		//System.out.println("Classes: " + this.trainingInstances.numClasses());
		//this.printClassFrequencies();

		System.out.print("Building Classifier...");
		this.build(this.filteredDataset);
		System.out.println("Built");

		/*
		 * Attribute attribute; System.out.println("Attribute Weights:"); for
		 * (int index = 0; index < this.filteredDataset.numAttributes();
		 * index++) { attribute = this.filteredDataset.attribute(index);
		 * System.out.println(attribute.name());
		 * System.out.println(this.filteredDataset.attributeStats(index));
		 * System.out.println("Weight: " + attribute.weight());
		 * System.out.println(); }
		 */
	}

	protected Set<BugReport> getTrainingBugs(Set<BugReport> bugs, Heuristic heuristic) {

		if (heuristic == null) {
			return bugs;
		}

		String className;
		Set<BugReport> trainingBugs = new HashSet<BugReport>();

		System.out.println("Profile Filtering...");
		Classifier h_classifier = heuristic.getClassifier();

		int cantclassify = 0;
		Classification classification;
		FrequencyTable reasons = new FrequencyTable();
		int count = 0;
		for (BugReport report : bugs) {

			if ((count++) % 100 == 0)
				System.out.print(".");
			// System.out.println((++count) + ": Classifying " +
			// report.getId());
			classification = h_classifier.classify(report);
			className = classification.getClassification();
			/*
			 * if(className.equals("pinskia@gcc.gnu.org")) continue;
			 */
			if (this.profile != null) {
				if (this.profile.contains(className) == false) {
					// System.err.println("Removing: " + className);
					continue;
				}
			}

			// Remove any Eclipse Platform default names that might get
			// through
			if (className.toLowerCase().startsWith("platform-")) {
				// System.err.println("Removing: " + className);
				continue;
			}

			if (className.equals(HeuristicClassifier.CANNOT_CLASSIFY)) {
				reasons.add(classification.getReason());
				cantclassify++;
				continue;
			}

			trainingBugs.add(report);
			this.classFrequency.add(className);

		}
/*
		System.err.println("Reports In: " + bugs.size());
		System.err.println("Reports Out: " + trainingBugs.size());
		System.err.println("Cant Classify: " + cantclassify);
		System.err.println(reasons);
		*/
		return trainingBugs;
	}

	public List<String> getClasses() {
		List<String> classList = new ArrayList<String>();
		Enumeration<Object> classes = this.trainingInstances.classAttribute().enumerateValues();
		while (classes.hasMoreElements()) {
			classList.add(classes.nextElement().toString());
		}
		return classList;
	}

	protected FastVector getClassNames(Set<BugReport> reports, Heuristic heuristic) {

		String className;
		FastVector<String> classNames = new FastVector<String>();

		for (BugReport report : reports) {
			className = heuristic.getClassifier().classify(report).getClassification();
			if (classNames.contains(className) == false) {
				classNames.addElement(className);
			}
		}

		// System.err.println("Removed: " + removed);
		return classNames;
	}

	protected Instances createTrainingDataset(String datasetName, Set<BugReport> trainingBugs,
			Set<BugReport> testingBugs, Heuristic heuristic) {

		if (trainingBugs.equals(testingBugs) == false) {
			// Remove testing reports from the training set
			//System.out.println("Test set size: " + testingBugs.size());
			//System.out.println("Training Set before removing test reports: " + trainingBugs.size());

			/*
			if (!trainingBugs.removeAll(testingBugs)) {
				System.err.println("Training set unchanged - Manual processing");
				Set<BugReport> toRemove = new HashSet<BugReport>();
				for (BugReport r : testingBugs) {
					for (BugReport x : trainingBugs) {
						if (x.equals(r)) {
							toRemove.add(x);
						}
					}
				}
				trainingBugs.removeAll(toRemove);
			}
			*/

			//System.out.println("Training Set after removing test reports: " + trainingBugs.size());
		}
		trainingBugs = this.getTrainingBugs(trainingBugs, heuristic);

		this.saveTrainingIds(trainingBugs);

		//System.out.println("Getting class names...");
		FastVector classNames = this.getClassNames(trainingBugs, heuristic);

		Instances dataset = this.initDataset(datasetName, classNames);

		//System.out.println("Creating instances...");

		this.addInstances(trainingBugs, heuristic, dataset);
		//System.out.println("Instances: " + dataset.numInstances());
		//System.out.println("Dataset created.");
		return dataset;
	}

	private void saveTrainingIds(Set<BugReport> trainingBugs) {
		for (BugReport report : trainingBugs) {
			this.trainingIds.add(new Integer(report.getId()));
		}

	}

	protected Instances initDataset(String datasetName, FastVector classNames) {
		FastVector nullVector = null;
		FastVector attributes = new FastVector();

		attributes.addElement(new Attribute(DESCRIPTION, nullVector));

		if (USE_COMPONENT) {
			System.out.println("--> Using Component");
			attributes.addElement(new Attribute(COMPONENT, nullVector));
		}

		if (USE_OS) {
			System.out.println("--> Using OS");
			attributes.addElement(new Attribute(OS, nullVector));
		}

		if (USE_VERSION) {
			System.out.println("--> Using Version");
			attributes.addElement(new Attribute(VERSION, nullVector));
		}

		if (USE_HARDWARE) {
			System.out.println("--> Using Hardware");
			attributes.addElement(new Attribute(HARDWARE, nullVector));
		}

		attributes.addElement(new Attribute(CLASS, classNames));
		Instances dataset = new Instances(datasetName, attributes, 0);
		dataset.setClassIndex(attributes.size() - 1);

		return dataset;
	}

	protected void addInstances(Set<BugReport> bugs, Heuristic heuristic, Instances dataset) {
		String h_classification;
		Instance trainingInstance;
		for (BugReport report : bugs) {
			trainingInstance = this.makeTrainingInstance(report, dataset);

			// Set class
			h_classification = heuristic.getClassifier().classify(report).getClassification();
			trainingInstance.setClassValue(h_classification);
			dataset.add(trainingInstance);

		}
		dataset.compactify();
	}

	protected Instance makeTrainingInstance(BugReport report, Instances dataset) {

		Instance instance = this.createInstance(report, dataset);
		Attribute attribute = dataset.attribute(DESCRIPTION);
		String description = instance.stringValue(attribute);

		if (USE_COMMENTS) {
			/* Add comments for features */
			StringBuffer comments = new StringBuffer(description + "\n");
			for (Comment comment : report.getComments()) {
				comments.append(comment.getText() + "\n");
			}
			instance.setValue(attribute, comments.toString());
		}

		return instance;
	}

	public Instance createInstance(BugReport report, Instances dataset) {

		int numAttributes = dataset.numAttributes();

		Instance instance = new SparseInstance(numAttributes);
		instance.setDataset(dataset);

		for (int index = 0; index < numAttributes; index++) {
			instance.setValue(index, 0);
		}

		Attribute attribute;
		// Set attribute values
		String description = new String();
		if (USE_SUMMARY)
			description += report.getSummary().toLowerCase();

		if (USE_DESCRIPTION)
			description += report.getDescription().toLowerCase();

		attribute = dataset.attribute(DESCRIPTION);
		instance.setValue(attribute, description);

		if (USE_COMPONENT) {
			attribute = dataset.attribute(COMPONENT);
			instance.setValue(attribute, report.getComponent().toLowerCase());
		}

		if (USE_OS) {
			attribute = dataset.attribute(OS);
			instance.setValue(attribute, report.getOperatingSystem().toLowerCase());
		}

		if (USE_HARDWARE) {
			attribute = dataset.attribute(HARDWARE);
			instance.setValue(attribute, report.getHardware().toLowerCase());
		}

		return instance;

	}

	public void train(Set<BugReport> trainingReports, Set<BugReport> testingReports, Heuristic heuristic)
			throws Exception {
		System.out.println("Training " + this.getName()); //
		this.trainingInstances = createTrainingDataset("TrainingDataset", trainingReports, testingReports, heuristic);
		this.filteredDataset = this.filterTrainingData(this.trainingInstances);

		if (USE_ATTRIBUTE_SELECTION) {
			System.out.println("Using " + this.evaluator.getClass().getSimpleName());
			this.filteredDataset = this.filterInformationContent(this.filteredDataset);
		}
		
		this.testingInstances = createTestingDataset(testingReports);
		this.train();
	}

	private Instances createTestingDataset(Set<BugReport> testingReports) {

		Instances testing = new Instances(this.filteredDataset,testingReports.size());
		for (BugReport report : testingReports) {
			try {
				Instance bugInstance = this.createInstance(report, this.trainingInstances);

				this.filter.input(bugInstance);
				bugInstance = this.filter.output();

				if (USE_ATTRIBUTE_SELECTION)
					bugInstance = this.selector.reduceDimensionality(bugInstance);

				testing.add(bugInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return testing;
	}

	public void train(Set<BugReport> trainingReports, Set<BugReport> testingReports, String developerInfoFile,
			Heuristic heuristic) throws Exception {

		System.out.println("Training set Size: " + trainingReports.size());
		if (developerInfoFile != null) {
			DeveloperInfo developerInfo = new DeveloperInfo(developerInfoFile);
			testingReports = developerInfo.getTestingSet(testingReports);
		}
		train(trainingReports, testingReports, heuristic);
	}

	public File save(Project project) {
		try {
			ArffSaver saver = new ArffSaver();
			saver.setInstances(filteredDataset);
			saver.setFile(project.getClassifierDatafile());
			saver.writeBatch();
			return project.getClassifierDatafile();
			/*
			 * System.out.println("Writing out classifier: " +
			 * saveFile.getName()); ObjectOutputStream out = new
			 * ObjectOutputStream(new FileOutputStream(saveFile));
			 * out.writeObject(this); out.flush(); out.close();
			 * System.out.println("Classifier written out");
			 */
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void printClassFrequencies() {
		if (this.classFrequency.getKeys().isEmpty())
			return;
		System.out.println(this.classFrequency);
		DescriptiveStatistics stats = this.classFrequency.getStatistics();
		System.out.println("------------------------");
		System.out.println("Max: " + stats.getMax());
		System.out.println("Min: " + stats.getMin());
		System.out.println("Average: " + stats.getMean());
		System.out.println("------------------------");
	}

	public int numClasses() {
		return this.trainingInstances.numClasses();
	}

	public boolean inTrainingSet(BugReport report) {
		return this.trainingIds.contains(new Integer(report.getId()));
	}

	public Instances getTrainingInstances() {
		return filteredDataset;
	}

	public Instances getTestingInstances() {
		return testingInstances;
	}
}
