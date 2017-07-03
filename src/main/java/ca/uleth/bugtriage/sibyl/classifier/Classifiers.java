package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Profiles;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public enum Classifiers {

	ECLIPSE("platform_cb.classifier"), FIREFOX("firefox_cb.classifier"), KDE_PLASMA(
					"kde_plasma.classifier"), LIBREOFFICE("libreoffice.classifier");

	private final String developerClassifierName;	
	private TriageClassifier developerClassifier;
	private Date developerClassifierVersion;

	
	private static final Object syncLock = new Object();

	private Classifiers(String developerClassifierName) {
		this.developerClassifierName = developerClassifierName;		
	}

	public static TriageClassifier load(String classifierFilename) throws FileNotFoundException {
		File classifierFile = new File(classifierFilename);
		TriageClassifier classifier = MLClassifier.load(classifierFile);
		return classifier;
	}

	public TriageClassifier getDeveloperClassifier() throws FileNotFoundException {

		synchronized (syncLock) {
			Date currentClassifierVersion = getClassifierVersion(this.developerClassifierName);
			if (this.developerClassifier == null || this.developerClassifierVersion.before(currentClassifierVersion)) {
				Date now = new Date(System.currentTimeMillis());
				System.out.println("\n(" + now + ") Loading developer classifier: " + this.developerClassifierName);
				this.developerClassifier = load(Environment.getClassifierDir() + this.developerClassifierName);
				this.developerClassifierVersion = currentClassifierVersion;
			}
		}
		return this.developerClassifier;
	}

	private Date getClassifierVersion(String classifierName) {
		File classifierFile = new File(Environment.getClassifierDir() + classifierName);
		return new Date(classifierFile.lastModified());
	}

	public static String getClassifierFilename(Project project) {
		return project.product.toLowerCase();
	}
}
