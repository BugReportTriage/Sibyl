package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageTerminalFunction implements TerminalFunction {

    private BugReport report;

    public BugTriageTerminalFunction(BugReport r) {
	this.report = r;
	
    }
    
    @Override
    public boolean isTerminal(State s) {
	
	BugTriageState btState = (BugTriageState)s;
	
	return btState.getReport().getId() == report.getId();
    }

}
