package ca.uleth.bugtriage.sibyl.classifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import weka.core.Instance;
import weka.core.Instances;

public class SibylClassifier extends TriageClassifier {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 7083008649861435259L;

	//private static final String[]  sibylDevelopers = {"janvik@cs.ubc.ca", "jkanvik@gmail.com", "john@anvik.ca"};
	//private static final String[]  sibylDevelopers = {, "janvik@cs.ubc.ca", "frodo@lotr.com", "asmith@matrix.net", "vader@empire.com"};
	
	private static final Random rand = new Random();
	
	@Override
	protected void build(Instances dataset) throws Exception {
		throw new UnsupportedOperationException();
	}

	
	
	@Override
	public List<Classification> classify(BugReport report) {
		List<Classification> classifications = new ArrayList<Classification>();
		
		classifications.add(new Classification("jkanvik@gmail.com", "Web Service", 1));
		classifications.add(new Classification("frodo@lotr.com", "Developer Assignment", 1));
		classifications.add(new Classification("asmith@matrix.net", "Component Assignment", 1));
		//for(String developer : SibylClassifier.sibylDevelopers){
			//classifications.add(new Classification(developer, , rand.nextDouble()));
		//}
		return classifications;
	}



	@Override
	protected List<Classification> classify(Instance instance) throws Exception {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getName() {
		return "Sibyl Classsifier";
	}

}
