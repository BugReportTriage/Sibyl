package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import java.util.Random;
import java.util.TreeMap;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.generic.GenericOOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

public class BugTriageStateModel implements FullStateModel {

    public static final Map<String, Integer> developerFrequency = new TreeMap<String, Integer>();

    public static final Random rand = new Random(1234);

    private Iterator<Instance> reportItr;
    private Iterator<BugReportTerm> termItr;
    private Map<String, BugReportTerm> allTermStates;
    private int count = 0;

    private BugTriageDomain bugTriageDomain;

    public BugTriageStateModel(BugTriageDomain btd) {
        this.bugTriageDomain = btd;
        this.allTermStates = new HashMap<String, BugReportTerm>();
        init();
    }

    public State sample(State state, Action action) {

        // System.out.println("Sampling... ");

        Instance nextReport;
        if (termItr == null || termItr.hasNext() == false) {
            if (reportItr.hasNext()) {
                nextReport = reportItr.next();
                count++;
                //System.out.println("Processing report #" + count);
                List<BugReportTerm> termStates = new ArrayList<BugReportTerm>();
                // System.out.print("Terms: ");
                for (int i = 0; i < nextReport.numAttributes(); i++) {
                    if (i == nextReport.classIndex())
                        continue;
                    if (nextReport.value(i) > 0) {
                        String term = nextReport.attribute(i).name();
                        // System.out.print(term + " ");
                        BugReportTerm termState = this.allTermStates.get(term);
                        if (termState == null) {
                            termState = new BugReportTerm(term, nextReport);
                            this.allTermStates.put(term, termState);
                        } else {
                            termState.set(BugReportTerm.INSTANCE, nextReport);
                        }
                        termStates.add(termState);
                    }
                }
                // System.out.println();
                termItr = termStates.iterator();
                if (!termItr.hasNext()) {
                    System.err.println("Report has no terms?");
                }
            }
        }
        if (termItr.hasNext()) {
            State s = termItr.next();
            // System.out.println("Object hashcode: " + s.hashCode());
            return s;
        } else {
            System.err.println("No terms!");
            return null;
        }
    }

    public List<StateTransitionProb> stateTransitions(State state, Action action) {
        System.out.println("Determining stateTransitions...");
        List<StateTransitionProb> transisitonProbs = new ArrayList<StateTransitionProb>();

        String term = (String) state.get(BugReportTerm.TERM);

        // Calculate term frequency (TF)
        double totalTermFreq = 0;
        Map<String, Integer> dev2tf = this.bugTriageDomain.term2Dev.get(term);
        for (String developer : dev2tf.keySet())
            totalTermFreq += dev2tf.get(developer);

        // Calculate developer term frequency ratio
        for (String developer : dev2tf.keySet()) {
            double freq = dev2tf.get(developer) / totalTermFreq;
            transisitonProbs.add(new StateTransitionProb(state, freq));
        }

        return transisitonProbs;
    }

    public void init() {
        // this.reports.randomize(rand);
        Instances reports = bugTriageDomain.getInstances();        
        reportItr = reports.iterator();

    }

    public boolean allTriaged() {
        return reportItr.hasNext() == false;
    }
}
