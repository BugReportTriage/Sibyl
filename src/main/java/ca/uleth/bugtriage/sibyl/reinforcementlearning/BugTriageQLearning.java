package ca.uleth.bugtriage.sibyl.reinforcementlearning;

import java.util.ArrayList;
import java.util.List;

import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.mdp.auxiliary.StateGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.dataset.Dataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class BugTriageQLearning {

    private static final int NUM_REPORTS = 20;
    private static final int TRIALS = 10;

    public static void main(String[] args) {

        Dataset dataset = new BugzillaDataset(Project.FIREFOX);
        List<BugReport> reports = new ArrayList<BugReport>(dataset.importReports()).subList(0,                NUM_REPORTS);

        BugTriageDomain triageDomain = new BugTriageDomain(Project.FIREFOX, reports);
        triageDomain.init();
        
        BugReportStateGenerator generator = new BugReportStateGenerator(triageDomain);
        BugTriageStateModel model = new BugTriageStateModel();
        RewardFunction rf = new HeuristicRewardFunction();
        TerminalFunction tf = new BugTriageTerminalFunction(generator);

        triageDomain.setModel(new FactoredModel(model, rf, tf));

        for (String term : triageDomain.getTerms()) {
            // System.out.println(term);
        }

        LearningAgent agent = new QLearning(triageDomain, 0.99, new SimpleHashableStateFactory(),
                0.0, 1.0);
               
        SimulatedEnvironment env = new SimulatedEnvironment(triageDomain, generator);

        for (int j = 0; j < TRIALS; j++) {
            Episode e = null;
            double totalCorrect = 0;
            for (int i = 0; i < triageDomain.getInstances().size(); i++) {
                // System.out.println("Round # " + j + " Learning Episode #" + i);
                e = agent.runLearningEpisode(env);

                //System.out.println(e.stateSequence);
                //System.out.println(e.rewardSequence);
                for (double reward : e.rewardSequence) {
                    totalCorrect += reward/(e.stateSequence.size()-1);
                }
                //System.out.println("Total Correct: " + totalCorrect);
                
                // reset environment for next learning episode
                env.resetEnvironment();

            }
            
            System.out.println(totalCorrect + "/" + triageDomain.getInstances().size() + " ("
                    + totalCorrect * 100 / triageDomain.getInstances().size() + "%)");

            FactoredModel fm = (FactoredModel) triageDomain.getModel();
            BugTriageStateModel brsm = (BugTriageStateModel) fm.getStateModel();
            
            env.setStateGenerator(new BugReportStateGenerator(triageDomain));
            env.resetEnvironment();
        }
    }
}
