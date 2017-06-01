package ca.uleth.bugtriage.sibyl.dataset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Dates;
import ca.uleth.bugtriage.sibyl.utils.Utils;


public class FileDataset extends Dataset {

	public static final DateFormat FILE_DATE_FORMATTER = new SimpleDateFormat("yyyy_MM_dd");
	
	private static final long serialVersionUID = 1L;

	private final File file;

	public FileDataset(File file) {
		this.file = file;
	}

	@Override
	public List<BugReport> getData() {

		try {
			//System.out.println("Reading in bug list (" + file.getName() + ")");
			System.out.print(".");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					this.file));
			this.reports.addAll((List<BugReport>) in.readObject());
			//System.out.println("Bug list retrieved (" + this.reports.size()
					//+ " reports)");
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}

		return this.reports;
	}

	public static String constructFilename(User user, Date start, Date end) {

		Project project = user.getProject();
		String projectName = project.getProduct().toLowerCase();
		String startDate = FileDataset.FILE_DATE_FORMATTER.format(start);
		long numDays = Utils.numDays(start, end);
		return project.getDataDir() + projectName + "-" + startDate + "-" + numDays +"days.bugs";

	}
	
	public static void updateData(User user) {
		Date today = new Date(System.currentTimeMillis());
		// System.out.println(today);
		Date yesterday = Dates.getYesterday(today);
		// System.out.println(yesterday);

		CreateDataset dataset = new CreateDataset(user);
		dataset.run(yesterday, today);
	}
}
