package ca.uleth.bugtriage.sibyl;

import java.io.File;
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
import ca.uleth.bugtriage.sibyl.utils.Environment;

public enum Project {
	UNKNOWN("Unknown Project", "Unknown URL", "Unknown Product", "", "", "", null, null, null,
			Sibyl.DEFAULT_THRESHOLD, Sibyl.DEFAULT_THRESHOLD), 
	PLATFORM("Eclipse-Platform", "https://bugs.eclipse.org/bugs/", "Platform",
					"2017-05-15", "2017-06-15", EclipseData.ECLIPSE_DIR, EclipseData.DUPLICATES, Classifiers.ECLIPSE,
					Heuristic.ECLIPSE, 9, 100), 
	FIREFOX("Firefox", "https://bugzilla.mozilla.org/", "Firefox", "2015-01-01",
							"2016-01-01", FirefoxData.FIREFOX_DIR, FirefoxData.DUPLICATES, Classifiers.FIREFOX,
							Heuristic.MOZILLA, 10, 70), 
	KDE_PLASMA("KDE-Plasma 5", "https://bugs.kde.org/", "plasmashell",
									"2017-05-15", "2017-05-15", KDEData.KDE_PLASMA_DIR, KDEData.DUPLICATES, Classifiers.KDE_PLASMA,
									Heuristic.KDE_PLASMA, 9, 100), 
	LIBREOFFICE(LibreOfficeData.PROJECT, LibreOfficeData.URL,
											LibreOfficeData.PRODUCT, "2017-05-15", "2017-05-15", LibreOfficeData.LIBRE_OFFICE_DIR,
											LibreOfficeData.DUPLICATES, Classifiers.LIBREOFFICE, Heuristic.LIBREOFFICE,
											9,100),;

	public static final String PROJECT_ID_TAG = "project";

	public final String url;

	public final String name;

	public final String product;

	public final String dataDir;

	public final Heuristic heuristic;

	public final Classifiers classifiers;

	public final String duplicateReportsFile;

	public double thresholdLow;
	public double thresholdHigh;

	public String startDate;

	public String endDate;

	// FIX: Too many parameters. Use data classes.
	private Project(String name, String url, String product, String startDate, String endDate, String dataDir,
			String dupFile, Classifiers classifiers, Heuristic heuristic, double thresholdLow, double thresholdHigh) {
		this.name = name;
		this.product = product;
		this.url = url;
		this.dataDir = dataDir;
		this.classifiers = classifiers;
		this.heuristic = heuristic;
		this.duplicateReportsFile = dupFile;
		this.thresholdLow = thresholdLow;
		this.thresholdHigh = thresholdHigh;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public TriageClassifier getDeveloperClassifier() throws ClassifierNotFoundException {
		try {
			return this.classifiers.getDeveloperClassifier();
		} catch (FileNotFoundException e) {
			throw new ClassifierNotFoundException("A developer classifier for the " + name + " was not found.");
		}
	}

	public static Project getProject(String project) {
		if (project != null) {
			for (Project knownProject : Project.values()) {
				if (project.toLowerCase().equals(
						// Make sure that the same method to get the name is
						// used in AccountSetupForm.repository()
						knownProject.product.toLowerCase())) {
					return knownProject;
				}
			}
		}
		return UNKNOWN;
	}
	
	public File getDatafile(){
	    return new File(dataDir + "/" + name + "_" + startDate + "_" + endDate + ".json");
	}

	public File getClassifierDatafile() {		
		return new File(Environment.getClassifierDir() + name + ".arff");
	}
}
