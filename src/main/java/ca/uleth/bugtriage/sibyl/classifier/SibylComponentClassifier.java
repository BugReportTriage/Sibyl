package ca.uleth.bugtriage.sibyl.classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import weka.core.Instance;
import weka.core.Instances;

public class SibylComponentClassifier extends TriageClassifier {

	private static final String[]  sibylComponents = {"Web Service", "Developer Assignment", "Component Assignment"};
	
	private static final Random rand = new Random();
	
	@Override
	protected void build(Instances dataset) throws Exception {
		throw new UnsupportedOperationException();
	}

	
	
	@Override
	public List<Classification> classify(BugReport report) {
		List<Classification> classifications = new ArrayList<Classification>();
		for(String component : SibylComponentClassifier.sibylComponents){
			classifications.add(new Classification(component, "Sibyl Components", rand.nextDouble()));
		}
		return classifications;
	}



	@Override
	protected List<Classification> classify(Instance instance) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "Sibyl Component Classsifier";
	}

}
