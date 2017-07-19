package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageTerminalFunction implements TerminalFunction {
    
    private BugTriageStateModel stateModel;

    public BugTriageTerminalFunction(BugTriageStateModel sm) {
	this.stateModel = sm;	
    }
    
    @Override
    public boolean isTerminal(State s) {
	return stateModel.allTriaged();
    }

}
