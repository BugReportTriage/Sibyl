package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.state.State;
import weka.core.Instance;

public class BugReportStateGenerator implements StateGenerator {

    private Iterator<Instance> reportItr;
    private int count = 0;
    private Map<String, BugReportTerm> allTermStates = new HashMap<String, BugReportTerm>();
    private static Iterator<BugReportTerm> termItr;
    

    public BugReportStateGenerator(BugTriageDomain domain) {    
        this.reportItr = domain.getInstances().iterator();        
    }

    public static Iterator<BugReportTerm> getTermItr() {
        if (termItr == null)
            throw new RuntimeException("No term iterator created!");
        return termItr;
    }

    public boolean reportTriaged() {
        return termItr.hasNext() == false;
    }
    
    @Override
    public State generateState() {
        if (reportItr.hasNext()) {
            Instance nextReport = reportItr.next();
            count++;
            //System.out.println("Processing report #" + count);
            List<BugReportTerm> termStates = new ArrayList<BugReportTerm>();
            //System.out.print("Terms [" + termStates.size() + "]: ");
            for (int i = 0; i < nextReport.numAttributes(); i++) {
                if (i == nextReport.classIndex())
                    continue;
                if (nextReport.value(i) > 0) {
                    String term = nextReport.attribute(i).name();
               //     System.out.print(term + " ");
                    BugReportTerm termState = this.allTermStates.get(term);
                    if (termState == null) {
                        termState = new BugReportTerm(term, nextReport);
                        this.allTermStates.put(term, termState);
                    } else {
                        termState.set(BugReportTerm.INSTANCE, nextReport);
                    }
                    termStates.add(termState);
                }
            }
            //System.out.println();
            termItr = termStates.iterator();
            return termItr.next();
        }
        return null;
    }
}
