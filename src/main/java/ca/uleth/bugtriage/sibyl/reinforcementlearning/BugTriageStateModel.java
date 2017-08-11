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

    public State sample(State state, Action action) {

        //System.out.println("Sampling... [State: " + state + "] [Action: " + action + "]");

        Iterator<BugReportTerm> termItr = BugReportStateGenerator.getTermItr();

        if (!termItr.hasNext()) {
           // System.err.println("Report has no terms?");
            return state;
        }
        return termItr.next();
    }

    public List<StateTransitionProb> stateTransitions(State state, Action action) {

        throw new UnsupportedOperationException(
                "stateTransitions() not supported, as never called");
    }
}
