package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import weka.core.Instance;

public class ReportTerm extends PropositionalFunction {

	public ReportTerm() {
		super("hasTerm", new String[] { BugReportTerm.TERM });
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		ObjectInstance reportTerm = s.object(params[0]);

		String term = (String) reportTerm.get(BugReportTerm.TERM);
		Instance instance = (Instance) reportTerm.get(BugReportTerm.INSTANCE);

		for (int i = 0; i < instance.numAttributes(); i++) {
			if (instance.attribute(i).name().equals(term) && instance.value(i) > 0) {
				return true;
			}
		}
		
		return false;
	}

}
