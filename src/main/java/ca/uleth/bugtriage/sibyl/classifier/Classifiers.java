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

    ECLIPSE("platform_cb.classifier", "platform_component.classifier", "platform_subcomponent.classifier",
	    "platform_cc.classifier"), MYLAR("mylar_cb.classifier", "mylar_component.classifier", ""), SIBYL("", "",
		    ""), GCC("", "", ""), FIREFOX("firefox_cb.classifier", "firefox_component.classifier",
			    ""), EVOLUTION("", "", ""), FEDORA("", "", ""), BUGZILLA("bugzilla_cb.classifier",
				    "bugzilla_component.classifier",
				    ""), ANT("ant_cb.classifier", "ant_component.classifier", ""), KDE_PLASMA(
					    "kde_plasma.classifier", "kde_plasma_component.classifier",
					    ""), LIBREOFFICE("libreoffice.classifier",
						    "libreoffice_component.classifier", "");

    private static final int NUM_PROFILE_DAYS = (Classifier.NUM_PROFILE_MONTHS * 30) + 1; // 3

    // months
    // + 1
    // to

    // componsate for months
    // with 31 days

    private static final int NUM_TRAINING_DAYS = 240 + 4; // 8 months --> 4 to

    // componsate for
    // months with 31
    // days

    private final String developerClassifierName;

    private final String componentClassifierName;

    private final String subcomponentClassifierName;

    private final String ccClassifierName;

    private TriageClassifier developerClassifier;

    private TriageClassifier componentClassifier;

    private TriageClassifier subcomponentClassifier;

    private TriageClassifier ccClassifier;

    private Date developerClassifierVersion;

    private Date componentClassifierVersion;

    private Date subcomponentClassifierVersion;

    private Date ccClassifierVersion;

    private static final Object syncLock = new Object();

    private Classifiers(String developerClassifierName, String componentClassifierName, String ccClassifierName) {
	this(developerClassifierName, componentClassifierName, "", ccClassifierName);
    }

    private Classifiers(String developerClassifierName, String componentClassifierName,
	    String subcomponentClassifierName, String ccClassifierName) {
	this.developerClassifierName = developerClassifierName;
	this.componentClassifierName = componentClassifierName;
	this.subcomponentClassifierName = subcomponentClassifierName;
	this.ccClassifierName = ccClassifierName;
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
