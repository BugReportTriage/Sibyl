package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageTerminalFunction implements TerminalFunction {

    private BugReportStateGenerator stateGenerator;

    public BugTriageTerminalFunction(BugReportStateGenerator sg) {
        this.stateGenerator = sg;
    }

    @Override
    public boolean isTerminal(State s) {
        return stateGenerator.reportTriaged();
        //return stateModel.allTriaged();
    }

}
