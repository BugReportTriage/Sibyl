package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageStateModel implements FullStateModel {
    
    public static final Map<String, Integer> developerFrequency = new TreeMap<String, Integer>();
    
    public static final Random rand = new Random(1234);
    
    private Iterator<BugReport> rItr;
    
    private List<BugReport> reports;
    
    public BugTriageStateModel(List<BugReport> rs) {
	reports = rs;
	init();
	for(BugReport r : rs)
	    updateFixes(r.getAssigned());
	
    }
    
    public State sample(State state, Action action) {

	System.out.print("Sampling... ");
	
	BugTriageState nextState = (BugTriageState) state.copy();
	
	BugReport nextReport = rItr.next();
	nextState.set(BugTriageState.NEW_REPORT, nextReport);
	System.out.print(nextReport + "> ");
	
	// Randomly choose a developer with equal probability
	List<String> devNames = new ArrayList<String>(BugTriageStateModel.developerFrequency.keySet());
	nextState.set(BugTriageState.FIXER, devNames.get(rand.nextInt(devNames.size()))); 
	
	System.out.println(nextState.get(BugTriageState.FIXER));
	
	return nextState;
    }

    public List<StateTransitionProb> stateTransitions(State state, Action action) {
	System.err.println("Determining stateTransitions...");
	List<StateTransitionProb> transisitonProbs = new ArrayList<StateTransitionProb>(BugTriageStateModel.developerFrequency.size());

	int totalFixes = 0;
	for (Integer fixes : BugTriageStateModel.developerFrequency.values())
	    totalFixes += fixes.intValue();

	for (Entry<String, Integer> entry : BugTriageStateModel.developerFrequency.entrySet()) {
	    BugTriageState nextState = (BugTriageState) state.copy();
	    nextState.set(BugTriageState.FIXER, entry.getKey());
	    transisitonProbs.add(new StateTransitionProb(nextState, entry.getValue().intValue() / totalFixes));
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
	rItr = reports.iterator();
    }

}
