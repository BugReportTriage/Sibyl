package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.UnknownKeyException;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.ASSearch;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SparseInstance;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * The 'state' is the attributes (i.e. text) from the bug reports
 * 
 * @author John Anvik (2017)
 *
 */

public class BugTriageState implements MutableState {

   

    private static final List<String> descriptions = new ArrayList<String>();

    private static enum ATTRIBUTE_SELECTOR {
	INFORMATION_GAIN, CHI2, PRINCIPAL_COMPONENTS
    }

    private static final String PRIOR_REPORTS = "Report Text";

    private static final String REPORT_TEXT = "Description";

    public static final String NEW_REPORT = "New Report";

    public static final String FIXER = "Fixer";

    private static final String UNKNOWN = "Fixer Unknown";

    private BugReport report;

    private String whoFixed;

    private Instances dataset;

    public BugTriageState() {
    }

    public BugTriageState(BugReport r) {
	report = r;
	whoFixed = UNKNOWN;
    }

    public State copy() {
	BugTriageState stateCopy = new BugTriageState();
	if (dataset != null)
	    stateCopy.dataset = new Instances(dataset);
	stateCopy.report = report;
	stateCopy.whoFixed = whoFixed;
	return stateCopy;
    }

    public Object get(Object key) {

	switch ((String) key) {
	case NEW_REPORT:
	    return report;
	case FIXER:
	    return whoFixed;
	case PRIOR_REPORTS:
	    return dataset;
	default:
	    throw new UnknownKeyException(key);
	}
    }

    public List<Object> variableKeys() {
	List<Object> keys = new ArrayList<Object>();
	keys.add(NEW_REPORT);
	keys.add(FIXER);
	// keys.add(PRIOR_REPORTS);
	return keys;
    }

    // Adds a report to the set of instances
    public MutableState set(Object key, Object value) {
	switch ((String) key) {
	case NEW_REPORT:
	    report = (BugReport) value;	    
	    descriptions.add(report.getDescription());
	    break;
	case FIXER:
	    whoFixed = (String) value;
	    break;
	/*
	 * case PRIOR_REPORTS: // Not called? createDataset(); break;
	 */
	default:
	    throw new UnknownKeyException(key);
	}

	return this;
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();
	for (String desc : descriptions)
	    sb.append(desc + "\n");
	return sb.toString();
    }

    public BugReport getReport() {
	return report;
    }
    
    private void createDataset() {
	try {

	    Instances rawDataset = initDataset();
	    for (String text : descriptions) {
		Instance instance = createInstance(text, rawDataset);
		rawDataset.add(instance);
	    }

	    Filter filter = createFilter();
	    filter.setInputFormat(dataset);
	    Instances filteredDataset = Filter.useFilter(dataset, filter);

	    AttributeSelection selector = createAttributeSelector(ATTRIBUTE_SELECTOR.CHI2);
	    selector.SelectAttributes(filteredDataset);
	    this.dataset = selector.reduceDimensionality(filteredDataset);

	} catch (Exception e) {
	    System.err.println("Something bad happened in " + this.getClass().getSimpleName());
	}
    }

    private Instances initDataset() {
	FastVector nullVector = null;
	FastVector attributes = new FastVector();

	attributes.addElement(new Attribute(REPORT_TEXT, nullVector));

	Instances dataset = new Instances("Reports", attributes, 0);

	return dataset;
    }

    private Instance createInstance(String text, Instances dataset) {

	int numAttributes = dataset.numAttributes();

	Instance instance = new SparseInstance(numAttributes);
	instance.setDataset(dataset);

	for (int index = 0; index < numAttributes; index++) {
	    instance.setValue(index, 0);
	}

	Attribute attribute;
	attribute = dataset.attribute(REPORT_TEXT);
	instance.setValue(attribute, text);

	return instance;

    }

    private Filter createFilter() {
	StringToWordVector filter = new StringToWordVector();
	filter.setStopwordsHandler(new Rainbow());
	filter.setLowerCaseTokens(true);
	filter.setStemmer(new SnowballStemmer()); // Porter stemmer

	filter.setIDFTransform(true);
	filter.setTFTransform(true);

	filter.setNormalizeDocLength(
		new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));

	return filter;
    }

    private AttributeSelection createAttributeSelector(ATTRIBUTE_SELECTOR algorithm) {
	AttributeSelection selector = new AttributeSelection();
	ASEvaluation evaluator;

	switch (algorithm) {

	case PRINCIPAL_COMPONENTS:
	    evaluator = new PrincipalComponents();
	    break;
	case CHI2:
	    evaluator = new ChiSquaredAttributeEval();
	    break;
	case INFORMATION_GAIN:
	default:
	    evaluator = new InfoGainAttributeEval();
	    break;
	}
	selector.setEvaluator(evaluator);

	ASSearch searcher = new Ranker();
	((Ranker) searcher).setThreshold(0);

	selector.setSearch(searcher);

	return selector;
    }

  
}
