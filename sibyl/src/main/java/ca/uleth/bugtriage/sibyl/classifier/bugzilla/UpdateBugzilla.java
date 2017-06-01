package ca.uleth.bugtriage.sibyl.classifier.bugzilla;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;

public class UpdateBugzilla {

	public static void main(String[] args) {
		Classifiers.update(Project.BUGZILLA);
	}
}
