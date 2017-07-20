package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Arrays;
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
import weka.core.Instance;
import weka.core.Instances;

public class BugTriageStateModel implements FullStateModel {

    public static final Map<String, Integer> developerFrequency = new TreeMap<String, Integer>();

    public static final Random rand = new Random(1234);

    private Iterator<BugReportText> reportItr;
    private Iterator<BugReportTerm> termItr;

    private BugTriageDomain bugTriageDomain;

    public BugTriageStateModel(BugTriageDomain btd) {
	this.bugTriageDomain = btd;
    }

    public State sample(State state, Action action) {

	 System.out.print("Sampling... ");

	 if()
	Instance nextReport = rItr.next();
	BugReportText nextState = new BugReportText(nextReport);
	// System.out.println(nextState.get(BugTriageState.INSTANCE) + "> ");

	return nextState;
    }

    public List<StateTransitionProb> stateTransitions(State state, Action action) {
	System.out.println("Determining stateTransitions...");
	List<StateTransitionProb> transisitonProbs = new ArrayList<StateTransitionProb>();

	String term = (String) state.get(BugReportTerm.BR_TERM);

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

    private static void updateFixes(String devName) {
	Integer fixed = developerFrequency.get(devName);
	if (fixed == null) {
	    developerFrequency.put(devName, new Integer(1));
	} else {
	    developerFrequency.put(devName, new Integer(fixed.intValue() + 1));
	}
    }

    public void init() {
	// this.reports.randomize(rand);
	Instances reports = bugTriageDomain.getInstances();
	reportItr = reports.iterator();
	
    }

    public boolean allTriaged() {
	return rItr.hasNext() == false;
    }

}
