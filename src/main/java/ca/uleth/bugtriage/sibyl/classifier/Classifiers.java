package ca.uleth.bugtriage.sibyl.classifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.Project;
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
											"kde_plasma.classifier", "kde_plasma_component.classifier", ""), LIBREOFFICE(
											"libreoffice.classifier", "libreoffice_component.classifier", "");

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
			if (SIBYL.equals(this)) { // Special case
				return new SibylClassifier();
			}

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

	public TriageClassifier getComponentClassifier() throws FileNotFoundException {

		synchronized (syncLock) {
			if (SIBYL.equals(this)) { // Special case
				return new SibylComponentClassifier();
			}

			Date currentClassifierVersion = getClassifierVersion(this.componentClassifierName);
			if (this.componentClassifier == null || this.componentClassifierVersion.before(currentClassifierVersion)) {
				Date now = new Date(System.currentTimeMillis());
				System.out.println("\n(" + now + ") Loading component classifier: " + this.componentClassifierName);
				this.componentClassifier = load(Environment.getClassifierDir() + this.componentClassifierName);
				this.componentClassifierVersion = currentClassifierVersion;
			}
		}
		return this.componentClassifier;
	}

	private Date getClassifierVersion(String classifierName) {
		File classifierFile = new File(Environment.getClassifierDir() + classifierName);
		return new Date(classifierFile.lastModified());
	}	

	public static void update(Project project) {
		throw new UnsupportedOperationException();
	}

	public static String getClassifierFilename(Project project) {
		return project.product.toLowerCase();
	}

	public static TriageClassifier updateClassifier(Heuristic heuristic, ClassifierType type) {		
		
		Project project = null;
		Set<BugReport> dataFiles = Utils.dataFiles(new File(project.dataDir));

		Profiles profile = null;
		String classifierName = getClassifierFilename(project);
		String[] data;
		if (heuristic != Heuristic.COMPONENT && heuristic != Heuristic.SUBCOMPONENT && type != ClassifierType.CC) {
			String[] profileFiles = Utils.getDataset(dataFiles, NUM_PROFILE_DAYS);
			profile = Classifier.createDeveloperProfiles(profileFiles, project);
			data = Utils.getDataset(dataFiles, NUM_TRAINING_DAYS);
			if (type == ClassifierType.COMPONENT_BASED) {
				classifierName += "_cb";
			}
		} else if (heuristic == Heuristic.COMPONENT) {
			classifierName += "_component";
			data = Utils.getDataset(dataFiles, NUM_TRAINING_DAYS);
		} else if (heuristic == Heuristic.SUBCOMPONENT) {
			classifierName += "_subcomponent";
			data = Utils.getDataset(dataFiles, NUM_TRAINING_DAYS);
		} else {
			classifierName += "_cc";
			data = Utils.getDataset(dataFiles, 90);
		}

		TriageClassifier classifier = Classifier.create(type, data, null, null, heuristic, profile, true);
		Classifier.saveClassifier(classifierName, classifier);
		return classifier;
	}

	public TriageClassifier getSubcomponentClassifier() throws FileNotFoundException {
		synchronized (syncLock) {
			if (SIBYL.equals(this)) { // Special case
				throw new FileNotFoundException();
			}

			Date currentClassifierVersion = getClassifierVersion(this.subcomponentClassifierName);
			if (this.subcomponentClassifier == null
					|| this.subcomponentClassifierVersion.before(currentClassifierVersion)) {
				Date now = new Date(System.currentTimeMillis());
				System.out
						.println("\n(" + now + ") Loading subcomponent classifier: " + this.subcomponentClassifierName);
				this.subcomponentClassifier = load(Environment.getClassifierDir() + this.subcomponentClassifierName);
				this.subcomponentClassifierVersion = currentClassifierVersion;
			}
		}
		return this.subcomponentClassifier;
	}

	public TriageClassifier getCCClassifier() throws FileNotFoundException {
		synchronized (syncLock) {
			if (SIBYL.equals(this)) { // Special case
				return new SibylCCClassifier();
			}

			Date currentClassifierVersion = getClassifierVersion(this.ccClassifierName);
			if (this.ccClassifier == null || this.ccClassifierVersion.before(currentClassifierVersion)) {
				Date now = new Date(System.currentTimeMillis());
				System.out.println("\n(" + now + ") Loading CC classifier: " + this.ccClassifierName);
				this.ccClassifier = load(Environment.getClassifierDir() + this.ccClassifierName);
				this.ccClassifierVersion = currentClassifierVersion;
			}
		}
		return this.ccClassifier;
	}

}
