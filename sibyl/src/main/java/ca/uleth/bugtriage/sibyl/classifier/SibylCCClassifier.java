package ca.uleth.bugtriage.sibyl.classifier;

import java.util.ArrayList;
import java.util.List;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import weka.core.Instance;
import weka.core.Instances;

public class SibylCCClassifier extends TriageClassifier {

	private static final String[]  sibylCCs = {"janvik@cs.ubc.ca", "jkanvik@gmail.com", "john@anvik.ca"};
	
	@Override
	protected void build(Instances dataset) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	protected List<Classification> classify(Instance instance) throws Exception {
		throw new NotImplementedException();
	}

	@Override
	public List<Classification> classify(BugReport report) {
		List<Classification> classifications = new ArrayList<Classification>();
		for(String component : SibylCCClassifier.sibylCCs){
			classifications.add(new Classification(component, "Sibyl CCs", 1));
		}
		return classifications;
	}
	
	@Override
	public String getName() {
		return "Sibyl CC Classsifier";
	}

}
