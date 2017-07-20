package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;

public class ReportTerm extends PropositionalFunction {

    public ReportTerm() {
	super("hasTerm", new String[]{BugReportText.BR_TEXT, BugReportTerm.TERM});
    }
    
    @Override
    public boolean isTrue(OOState s, String... params) {
	ObjectInstance report = s.object(params[0]);
	ObjectInstance reportTerm = s.object(params[1]);
	
	String reportText = (String)report.get(BugReportText.TEXT);
	String term = (String)reportTerm.get(BugReportTerm.TERM);	
	
	return reportText.contains(term);
    }

}
