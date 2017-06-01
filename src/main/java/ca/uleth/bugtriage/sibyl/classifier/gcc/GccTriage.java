package ca.uleth.bugtriage.sibyl.classifier.gcc;

import java.io.File;
import java.io.FileNotFoundException;

import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.eval.Triage;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class GccTriage {
	
	public static void run(TriageClassifier classifier) {
		new Triage().evaluate(classifier, Utils.getTestingSet(GccData.GCC_DIR, GccData.TESTING_MONTH), GccData.DEVELOPER_INFO);
	}
	
	public static void main(String[] args) {
		try {
			String classifierDir = Environment.getClassifierDir();
			String classifierName = classifierDir + "gcc8month.classifier";
			TriageClassifier clasifier = MLClassifier.load(new File(classifierName));
			run(clasifier);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
