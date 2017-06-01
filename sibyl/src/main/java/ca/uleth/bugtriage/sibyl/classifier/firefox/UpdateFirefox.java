package ca.uleth.bugtriage.sibyl.classifier.firefox;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;

public class UpdateFirefox {

	public static void main(String[] args) {
		Classifiers.update(Project.FIREFOX);
	}
}
