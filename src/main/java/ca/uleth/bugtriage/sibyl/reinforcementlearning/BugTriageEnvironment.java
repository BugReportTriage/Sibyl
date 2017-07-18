package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.environment.Environment;
import burlap.mdp.singleagent.environment.EnvironmentOutcome;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageEnvironment extends SimulatedEnvironment {
    
    public BugTriageEnvironment(SADomain domain, StateGenerator stateGenerator) {
	super(domain, stateGenerator);	
    }
    
    public void addReport(BugReport report){
	BugTriageStateGenerator btsg = (BugTriageStateGenerator)this.stateGenerator;
	btsg.addReport(report);
    }

    
}
