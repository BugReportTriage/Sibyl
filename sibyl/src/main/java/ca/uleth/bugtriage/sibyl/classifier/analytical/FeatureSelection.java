package ca.uleth.bugtriage.sibyl.classifier.analytical;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.ClassifierType;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;
import ca.uleth.bugtriage.sibyl.classifier.MLClassifier;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import weka.attributeSelection.AttributeEvaluator;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.core.Attribute;
import weka.core.Instances;

public class FeatureSelection {

	public static void main(String[] args) {
		try {
			Project project = Project.MYLAR;
			//String classifierName = "mylar.classifier";
			String classifierName = "platform.classifier";
			//updateClassifier(project);

			TriageClassifier classifier = Classifiers.load(Environment
					.getClassifierDir()
					+ classifierName);
			InfoGainAttributeEval evaluator = new InfoGainAttributeEval();
			Instances data = classifier.getData();
			System.out.println("Building evaluator");
			long start = System.currentTimeMillis();
			evaluator.buildEvaluator(data);
			long end = System.currentTimeMillis();
			long diff = (end - start) / 60000;
			System.out.println("Evaluator built: " + diff);
			double infoGain;
			String name;
			List<AttributeInfo> infoGains = new ArrayList<AttributeInfo>(data.numAttributes());
			for (int attributeIndex = 0; attributeIndex < data.numAttributes(); attributeIndex++) {
				name = data.attribute(attributeIndex).name();
				infoGain = evaluator.evaluateAttribute(attributeIndex);
				infoGains.add(new AttributeInfo(name, infoGain));
			}
			Collections.sort(infoGains, Collections.reverseOrder());
			System.out.println("Size: " + infoGains.size());
			
			List<AttributeInfo> purged = new ArrayList<AttributeInfo>(data.numAttributes());
			purged.addAll(infoGains);
			for(AttributeInfo info : infoGains){
				if(info.infoContent == 0){
					purged.remove(info);
				}
			}
			System.out.println("Removed: " + (infoGains.size() - purged.size()));
			
			for(AttributeInfo info : purged){
				//System.out.println(info.name + ": " + info.infoContent);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static void updateClassifier(Project project) {
		User user = new User(User.UNKNOWN_USER_ID, Login.USER, Login.PASSWORD,
				project);
		Classifiers.updateClassifier(user, project.getHeuristic(),
				ClassifierType.SVM);
	}

}

class AttributeInfo implements Comparable<AttributeInfo> {
	public final String name;

	public final double infoContent;

	public AttributeInfo(String name, double infoContent) {
		this.name = name;
		this.infoContent = infoContent;
	}

	public int compareTo(AttributeInfo attrInfo) {
		if (this.infoContent < attrInfo.infoContent) {
			return -1;
		}
		if (this.infoContent > attrInfo.infoContent) {
			return 1;
		}
		return 0;
	}

}