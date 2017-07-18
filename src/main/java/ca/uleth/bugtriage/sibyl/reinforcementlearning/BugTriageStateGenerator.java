package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.state.State;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageStateGenerator implements StateGenerator {

    private List<BugReport> reports = new LinkedList<BugReport>();    

    public BugTriageStateGenerator(List<BugReport> r) {
	this.reports.addAll(r);
    }
    
    @Override
    public State generateState() {
	System.out.println("Generating state");
	BugTriageState newState = new BugTriageState();
	if (reports.isEmpty() == false){
	    newState.set(BugTriageState.NEW_REPORT, reports.get(0));
	    newState.set(BugTriageState.FIXER, "");
	}
	
	return newState;
    }

    public void addReport(BugReport report) {
	reports.add(report);
    }

    
    
}
