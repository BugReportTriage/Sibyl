package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugReportAssignment implements ActionType {
    
    @Override
    public List<Action> allApplicableActions(State s) {
	List<Action> triageActions = new ArrayList<Action>();
		
	for (String developer : BugTriageStateModel.developerFrequency.keySet()) {
	    triageActions.add(new BugTriageAction(developer));
	    //System.out.println("Action: " + developer);
	}
	return triageActions;
    }

    @Override
    public Action associatedAction(String name) {	
	throw new UnsupportedOperationException();
    }

    @Override
    public String typeName() {	
	return "Bug Report Assignment";
    }

}
