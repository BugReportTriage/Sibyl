package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import weka.attributeSelection.ASEvaluation;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.ChiSquaredAttributeEval;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.PrincipalComponents;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.lazy.IBk;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.SparseInstance;
import weka.core.stemmers.SnowballStemmer;
import weka.core.stopwords.Rainbow;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class BugTriageDomain extends OOSADomain {

    private static enum ATTRIBUTE_SELECTOR {
        INFORMATION_GAIN, CHI2, PRINCIPAL_COMPONENTS
    }

    private static final String REPORT_TEXT = "Description";
    private static final String FIXER = "Fixer";
    private static final int NUM_ATTRIBUTES = 200;

    private List<BugReport> reports;
    private boolean initialized;
    private Instances dataset;
    private Project project;
    private Classifier classifier;

    // Term to dev to termFreq mapping
    public final Map<String, Map<String, Integer>> term2Dev;

    public BugTriageDomain(Project p, List<BugReport> r) {
        project = p;
        this.reports = r;
        this.initialized = false;
        term2Dev = new HashMap<String, Map<String, Integer>>();
    }

    public void init() {
        List<String> classes = getClassNames(true);
        System.out.println("# developers: " + classes.size());
        createDataset(classes, reports);

        // Create a term to developers mapping
        for (Instance i : dataset) {
            Enumeration<Attribute> attributes = dataset.enumerateAttributes();
            while (attributes.hasMoreElements()) {
                Attribute a = attributes.nextElement();
                if (i.value(a) > 0) {
                    String term = a.name();
                    Map<String, Integer> devs = term2Dev.get(term);
                    if (devs == null) {
                        devs = new HashMap<String, Integer>();
                        term2Dev.put(term, devs);
                    }
                    String devName = i.toString(i.classAttribute());
                    Integer termFreq = devs.get(devName);
                    if (termFreq == null) {
                        termFreq = new Integer(0);
                    }
                    termFreq = new Integer(Integer.sum(termFreq.intValue(), 1));
                    devs.put(devName, termFreq);
                }
            }

        }

        // Create domain states
        // this.addStateClass(BugReportText.BR_TEXT, BugReportText.class);
        this.addStateClass(BugReportTerm.BR_TERM, BugReportTerm.class);

        ActionType assignment = new BugReportAssignment(this);
        this.addActionType(assignment);

        OODomain.Helper.addPfsToDomain(this,
                Arrays.<PropositionalFunction>asList(new ReportTerm()));

        BugTriageStateModel model = new BugTriageStateModel(this);
        RewardFunction rf = new HeuristicRewardFunction();
        TerminalFunction tf = new BugTriageTerminalFunction(model);

        this.setModel(new FactoredModel(model, rf, tf));

        this.initialized = true;
    }

    public Map<String, Map<String, Integer>> getTerm2Dev() {
        return this.term2Dev;
    }

    public List<String> getTerms() {
        if (this.initialized == false)
            init();

        return getTerms(this.dataset);
    }

    private List<String> getTerms(Instances dataset) {
        List<String> attributeNames = new ArrayList<String>();
        Enumeration<Attribute> attributes = dataset.enumerateAttributes();
        while (attributes.hasMoreElements()) {
            attributeNames.add(attributes.nextElement().name());
        }
        return attributeNames;
    }

    private void createDataset(List<String> classes, List<BugReport> reports) {
        try {

            Instances rawDataset = initDataset(classes);
            for (BugReport r : reports) {
                String label = this.project.heuristic.getClassifier().classify(r)
                        .getClassification();
                if (classes.contains(label)) {
                    Instance instance = createInstance(r.getSummary() + " " + r.getDescription(),
                            rawDataset);
                    instance.setClassValue(
                            this.project.heuristic.getClassifier().classify(r).getClassification());
                    rawDataset.add(instance);
                }else{
                    //System.out.println("Report " + r.getId() + " not included.");
                }
            }

            Filter filter = createFilter();
            filter.setInputFormat(rawDataset);
            Instances filteredDataset = Filter.useFilter(rawDataset, filter);

            System.err.println("Attributes before: " + filteredDataset.numAttributes());
            // for(String term : getTerms(filteredDataset))
            // System.out.println(term);

            filteredDataset = removeNumericSymbols(filteredDataset);

            System.err.println("Attributes after numeric/symbol filtering: "
                    + filteredDataset.numAttributes());
            // for(String term : getTerms(filteredDataset))
            // System.out.println(term);

            AttributeSelection selector = createAttributeSelector(
                    ATTRIBUTE_SELECTOR.INFORMATION_GAIN);
            selector.SelectAttributes(filteredDataset);
            filteredDataset = selector.reduceDimensionality(filteredDataset);
            // this.dataset = filteredDataset;
            System.err.println("Attributes after: " + filteredDataset.numAttributes());
            // for(String term : getTerms(dataset))
            // System.out.println(term);

            this.dataset = filterNoData(filteredDataset);

        } catch (Exception e) {
            System.err.println("Something bad happened in " + this.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    private Instances removeNumericSymbols(Instances dataset) throws Exception {
        Set<Attribute> toRemove = new HashSet<Attribute>();
        Enumeration<Attribute> attributes = dataset.enumerateAttributes();
        char[] symbols = { '=', '@', '/', '-', '_', '+', '`', '[', ']', '#','*','<','>', '{', '}', '%' };
        while (attributes.hasMoreElements()) {
            Attribute a = attributes.nextElement();
            if (StringUtils.isNumeric(a.name()))
                toRemove.add(a);
            if (StringUtils.containsAny(a.name(), symbols))
                toRemove.add(a);
            if(StringUtils.contains(a.name(), "0x"))
                toRemove.add(a);
        }
        int[] removeIndices = new int[toRemove.size()];
        int index = 0;
        for (Attribute a : toRemove)
            removeIndices[index++] = a.index();
        Remove filter = new Remove();
        filter.setAttributeIndicesArray(removeIndices);
        filter.setInputFormat(dataset);
        return Filter.useFilter(dataset, filter);

    }

    private Instances initDataset(List<String> classes) {
        List<String> nullVector = Collections.EMPTY_LIST;
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();

        attributes.add(new Attribute(REPORT_TEXT, true));
        attributes.add(new Attribute(FIXER, classes));

        Instances dataset = new Instances("Reports", attributes, 0);
        dataset.setClassIndex(attributes.size() - 1);
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
        // attribute = dataset.attribute(FIXER);
        // instance.setValue(attribute, " ");
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

        filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL,
                StringToWordVector.TAGS_FILTER));

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

        Ranker ranker = new Ranker();
        ranker.setNumToSelect(NUM_ATTRIBUTES);
        ranker.setThreshold(0);

        selector.setSearch(ranker);

        return selector;
    }

    protected List<String> getClassNames(boolean useProfiles) {
        if (useProfiles) {
            Profiles devProfiles = new Profiles(new HashSet<BugReport>(this.reports),
                    this.project.heuristic);
            devProfiles.create();
            devProfiles.pruneTotal(this.project.thresholdLow, this.project.thresholdHigh);
            return new ArrayList<String>(devProfiles.getProfiles().keySet());
        } else {
            String className;
            List<String> classNames = new ArrayList<String>();

            for (BugReport report : this.reports) {
                className = this.project.heuristic.getClassifier().classify(report)
                        .getClassification();
                if (classNames.contains(className) == false) {
                    classNames.add(className);
                }
            }
            return classNames;
        }
    }

    public Instances getInstances() {
        if (this.dataset == null)
            init();
        return this.dataset;
    }

    public Classifier getClassifier() {
        if (classifier == null) {
            classifier = new IBk(10);
            // classifier = new SMO();
            try {
                classifier.buildClassifier(getInstances());
            } catch (Exception e) {
                System.err.println("Problem creating classifier");
                e.printStackTrace();
            }
        }
        return classifier;
    }

    private Instances filterNoData(Instances dataset) {
        System.err.println("Reports before no data filtering: " + reports.size());
        Instances filtered = new Instances(dataset, 0);
        Iterator<Instance> itr = dataset.iterator();
        while (itr.hasNext()) {
            Instance report = itr.next();
            for (int i = 0; i < report.numAttributes(); i++) {
                if (i == report.classIndex())
                    continue;
                if (report.value(i) > 0) {
                    filtered.add(report);
                    break;
                }
            }
        }
        System.err.println("Reports after: " + filtered.size());
        return filtered;
    }
}
