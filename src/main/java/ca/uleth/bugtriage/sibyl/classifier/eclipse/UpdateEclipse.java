package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;

public class UpdateEclipse {

	public static void main(String[] args) {
		Classifiers.update(Project.PLATFORM);
	}
}
