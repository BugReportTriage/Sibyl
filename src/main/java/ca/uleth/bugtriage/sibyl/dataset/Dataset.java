package ca.uleth.bugtriage.sibyl.dataset;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaDataset;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public abstract class Dataset {

	// Use a 90/10 split for training/testing
	private static final double TRAINING_PERCENTAGE = 0.9;

	protected final Project project;
	protected final Set<BugReport> reports;

	public Dataset(Project p) {
		this.project = p;
		this.reports = new TreeSet<BugReport>();
	}

	/**
	 * Get bug report data for a project.
	 * 
	 * @return The report data in JSON format.
	 */
	public abstract String getReportsJSON();

	/**
	 * Get all bug report data from a remote issue tracking system
	 * 	
	 * @return A collection of BugReport objects.
	 */
	public abstract Set<BugReport> getData();

	/**
	 * Get all bug report data
	 * 
	 * @return A collection of BugReport objects.
	 */
	public Set<BugReport> getReports() {
		if(this.reports == null || this.reports.isEmpty())
			System.err.println("Reports not imported or retrieved yet.");
		return this.reports;
	}

	public Set<BugReport> getTrainingReports() {
		double trainingSize = Math.round(this.reports.size() * TRAINING_PERCENTAGE);
		List<BugReport> trainingReports = new ArrayList<BugReport>(this.reports);

		return new TreeSet<BugReport>(trainingReports.subList(0, (int) trainingSize));
	}

	public Set<BugReport> getTestingReports() {
		int lastTrainingIndex = (int) Math.round(this.reports.size() * TRAINING_PERCENTAGE);
		List<BugReport> reports = new ArrayList<BugReport>(this.reports);
		List<BugReport> testingReports = reports.subList(lastTrainingIndex, reports.size());
		return new TreeSet<BugReport>(testingReports);
	}

	public Project getProject() {
		return this.project;
	}

	/**
	 * Export a collection of bug reports to a file in JSON format.
	 * 
	 * @param project
	 * @param reports
	 * @return
	 */
	public File exportReports(File dataFile) {
		try {

			File parent = new File(dataFile.getParent());
			if (parent.exists() == false) {
				parent.mkdir();
			}

			System.out.println("Writing to: " + dataFile.getName());

			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(dataFile, reports);

			System.out.println("Bugs written out: " + dataFile.getName());
			return dataFile;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;
	}

	public File exportReports() {
		return exportReports(project.getDatafile());
	}

	/**
	 * Import a JSON formated file created by {@link Dataset}.export().
	 * 
	 * @param input
	 *            The file containing the bug reports in JSON format.
	 * @return A collection of BugReport objects.
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	public Set<BugReport> importReports() {
		return importReports(project.getDatafile());
	}

	public Set<BugReport> importReports(File file) {

		ObjectMapper mapper = new ObjectMapper();

		Set<BugReport> reports = Collections.emptySet();
		try {
			// Use TreeSet to ensure numerical ordering of reports by id
			reports = mapper.readValue(file, new TypeReference<TreeSet<BugReport>>() {
			});
		} catch (IOException e) {
			System.err.println("Unable to import file " + file.toString());
			System.err.println(e.getMessage());
		}

		this.reports.addAll(reports);
		return reports;
	}

}
