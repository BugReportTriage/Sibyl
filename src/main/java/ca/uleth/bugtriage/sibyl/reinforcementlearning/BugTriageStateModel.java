package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import weka.core.Instance;
import weka.core.Instances;

public class BugTriageStateModel implements FullStateModel {
    
    public static final Map<String, Integer> developerFrequency = new TreeMap<String, Integer>();
    
    public static final Random rand = new Random(1234);
    
    private Iterator<Instance> rItr;
    
    private Instances reports;
    
    public BugTriageStateModel(Instances rs) {
	reports = rs;
	init();
	for(Instance r : rs){
	    String fixer = r.toString(r.classAttribute());
	    updateFixes(fixer);
	}
	
    }
    
    public State sample(State state, Action action) {

	//System.out.print("Sampling... ");
	
	Instance nextReport = rItr.next();
	BugTriageState nextState = new BugTriageState(nextReport);	
	//System.out.println(nextState.get(BugTriageState.INSTANCE) + "> ");
	
	return nextState;
    }

    public List<StateTransitionProb> stateTransitions(State state, Action action) {
	System.err.println("Determining stateTransitions...");
	List<StateTransitionProb> transisitonProbs = new ArrayList<StateTransitionProb>(BugTriageStateModel.developerFrequency.size());

	int totalFixes = 0;
	for (Integer fixes : BugTriageStateModel.developerFrequency.values())
	    totalFixes += fixes.intValue();

	for (Entry<String, Integer> entry : BugTriageStateModel.developerFrequency.entrySet()) {	    	    
	    transisitonProbs.add(new StateTransitionProb(state, entry.getValue().intValue() / totalFixes));
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
	//this.reports.randomize(rand);
	rItr = reports.iterator();
    }

    public boolean allTriaged() {	
	return rItr.hasNext() == false;
    }

}
