package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import ca.uleth.bugtriage.sibyl.Classification;
import weka.classifiers.Classifier;
import weka.core.Instance;

public class BugReportAssignment implements ActionType {

    private BugTriageDomain domain;

    public BugReportAssignment(BugTriageDomain triageDomain) {
        this.domain = triageDomain;

    }

    @Override
    public List<Action> allApplicableActions(State s) {
        List<Action> triageActions = new ArrayList<Action>();
        BugReportTerm bts = (BugReportTerm) s;
        String term = (String) bts.get(BugReportTerm.TERM);

        try {
            List<Action> actions = developers(term);
            //System.out.println("Actions:" + actions);
            return actions;
            // return allDevelopers();
            // return useClassifier(report);
        } catch (Exception e) {
            System.err.println("Something bad happened in " + this.getClass().getName());
            e.printStackTrace();
        }
        return triageActions;
    }

    private List<Action> developers(String term) {
        List<Action> triageActions = new ArrayList<Action>();
        Map<String, Integer> developers2Freq = this.domain.getTerm2Dev().get(term);

        if (developers2Freq != null) {
            for (String dev : developers2Freq.keySet())
                triageActions.add(new BugTriageAction(dev));
        } else {
            triageActions.add(new BugTriageAction(""));
        }
        // System.out.println(term + " - Actions: " + triageActions.size() + triageActions);

        return triageActions;
    }

    private List<Action> allDevelopers() {
        List<Action> triageActions = new ArrayList<Action>();
        for (String developer : BugTriageStateModel.developerFrequency.keySet()) {
            triageActions.add(new BugTriageAction(developer));
        }
        return triageActions;
    }

    private List<Action> useClassifier(Instance report) throws Exception {

        List<Action> triageActions = new ArrayList<Action>();
        Classifier classifier = domain.getClassifier();

        double[] probs = classifier.distributionForInstance(report);
        String className;
        Set<Classification> predictions = new TreeSet<Classification>();
        for (int index = 0; index < report.dataset().numClasses(); index++) {
            className = report.dataset().classAttribute().value(index);
            predictions.add(new Classification(className, "Prediction", probs[index]));
        }

        // System.out.print("Predicting: ");
        Iterator<Classification> predictionItr = predictions.iterator();
        for (int i = 0; i < 3 && predictionItr.hasNext(); i++) {
            String dev = predictionItr.next().getClassification();
            triageActions.add(new BugTriageAction(dev));
            // System.out.print(dev + " ");
        }
        // System.out.println();
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
