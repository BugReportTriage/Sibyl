package ca.uleth.bugtriage.sibyl;

import java.io.FileNotFoundException;

import ca.uleth.bugtriage.sibyl.classifier.ClassifierNotFoundException;
import ca.uleth.bugtriage.sibyl.classifier.Classifiers;
import ca.uleth.bugtriage.sibyl.classifier.TriageClassifier;
import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxData;
import ca.uleth.bugtriage.sibyl.classifier.kdeplasma.KDEData;
import ca.uleth.bugtriage.sibyl.classifier.libreoffice.LibreOfficeData;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;

public enum Project {
	UNKNOWN("Unknown Project", "Unknown URL", "Unknown Product", "", "", null, null, null, Sibyl.DEFAULT_THRESHOLD), 
	PLATFORM("Eclipse-Platform", "https://bugs.eclipse.org/bugs/", "Platform", "2017-05-15", EclipseData.ECLIPSE_DIR, EclipseData.DUPLICATES, Classifiers.ECLIPSE, Heuristic.ECLIPSE, 9), 
	FIREFOX("Firefox", "https://bugzilla.mozilla.org/", "Firefox", "2017-05-15", FirefoxData.FIREFOX_DIR, FirefoxData.DUPLICATES, Classifiers.FIREFOX, Heuristic.MOZILLA, 9),
	KDE_PLASMA("KDE-Plasma 5", "https://bugs.kde.org/", "plasmashell", "2017-05-15", KDEData.KDE_PLASMA_DIR, KDEData.DUPLICATES, Classifiers.KDE_PLASMA, Heuristic.KDE_PLASMA, 9),
	LIBREOFFICE(LibreOfficeData.PROJECT, LibreOfficeData.URL, LibreOfficeData.PRODUCT, "2017-05-15", LibreOfficeData.LIBRE_OFFICE_DIR, LibreOfficeData.DUPLICATES, Classifiers.LIBREOFFICE, Heuristic.LIBREOFFICE, 9),
	;

	public static final String PROJECT_ID_TAG = "project";

	private final String url;

	private final String name;

	private final String product;

	private final String dataDir;

	private final Heuristic heuristic;	

	private final Classifiers classifiers;

	private final String duplicateReportsFile;

	private final double threshold;
	
	private final String startDate;

	private Project(String name, String url, String product, String date, String dataDir, String dupFile, 
			Classifiers classifiers, Heuristic heuristic, double threshold) {
		this.name = name;
		this.product = product;
		this.url = url;
		this.dataDir = dataDir;
		this.classifiers = classifiers;
		this.heuristic = heuristic;
		this.duplicateReportsFile = dupFile;
		this.threshold = threshold;
		this.startDate = date;
	}

	public String getName() {
		return this.name;
	}

	public String getURL() {
		return this.url;
	}

	public String getProduct() {
		return this.product;
	}

	public String getDataDir() {
		return this.dataDir;
	}

	public Heuristic getHeuristic() {
		return this.heuristic;
	}

	public TriageClassifier getDeveloperClassifier()
			throws ClassifierNotFoundException {
		try {
			return this.classifiers.getDeveloperClassifier();
		} catch (FileNotFoundException e) {
			throw new ClassifierNotFoundException(
					"A developer classifier for the " + this.getName() + " was not found.");
		}
	}

	public TriageClassifier getComponentClassifier()
			throws ClassifierNotFoundException {
		try {
			return this.classifiers.getComponentClassifier();
		} catch (FileNotFoundException e) {
			throw new ClassifierNotFoundException(
					"A component classifier for " + this.getName() + " was not found.");
		}
	}

	public static Project getProject(String project) {
		if (project != null) {
			for (Project knownProject : Project.values()) {
				if (project.toLowerCase().equals(
						// Make sure that the same method to get the name is used in AccountSetupForm.repository()
						knownProject.getProduct().toLowerCase())) {
					return knownProject;
				}
			}
		}
		return UNKNOWN;
	}
	
	public static void main(String[] args) {
		System.out.println(getProject("Mylar"));
	}

	public String getDupsFile() {
		return this.duplicateReportsFile;
	}

	public TriageClassifier getSubcomponentClassifier() throws ClassifierNotFoundException {
		try {
			return this.classifiers.getSubcomponentClassifier();
		} catch (FileNotFoundException e) {
			throw new ClassifierNotFoundException(
					"A subcomponent classifier for " + this.getName() + " was not found.");
		}
	}

	public double getThreshold() {
		return threshold;
	}

	public TriageClassifier getCCClassifier() throws ClassifierNotFoundException {
		try {
			return this.classifiers.getCCClassifier();
		} catch (FileNotFoundException e) {
			throw new ClassifierNotFoundException(
					"A subcomponent classifier for " + this.getName() + " was not found.");
		}
	}	

	public String getStartDate() {		
		return startDate;
	}
}
