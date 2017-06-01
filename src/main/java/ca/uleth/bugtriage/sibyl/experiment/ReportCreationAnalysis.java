package ca.uleth.bugtriage.sibyl.experiment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;

import org.apache.commons.math.stat.Frequency;

import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;
import sun.java2d.pipe.SpanShapeRenderer.Simple;

public class ReportCreationAnalysis {

	private static final DateFormat formatter = new SimpleDateFormat(
	"yyyy-MM-dd");
	
	public static void main(String[] args) {
		String[] files = Utils.getTrainingSet(EclipseData.ECLIPSE_DIR, 8, EclipseData.TESTING_MONTH);
		java.util.Set<BugReport> reports = Utils.getReports(files);
		
		Frequency freq = new Frequency();
		for (BugReport report : reports) {
			String date = formatter.format(report.getCreated());
			freq.addValue(date);
		}
		
		Iterator<Comparable<?>> itr = freq.valuesIterator();
		while(itr.hasNext()){
			String value = itr.next().toString(); 
			System.out.println(value + ": " + freq.getCount(value));
		}
	}
}
