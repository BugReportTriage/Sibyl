package ca.uleth.bugtriage.sibyl.classifier.fedora;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;

public class UpdateFedora {

	public static void main(String[] args) {
		Classifiers.update(Project.FEDORA);
	}
}
