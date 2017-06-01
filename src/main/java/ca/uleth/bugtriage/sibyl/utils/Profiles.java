package ca.uleth.bugtriage.sibyl.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class Profiles implements Serializable {

	/* Old version */
	// private static final Pattern monthYear = Pattern
	// .compile("\\w\\w\\w\\d\\d\\d\\d");
	private static final Pattern monthYear = Pattern
			.compile("-(\\d+_\\d+)_\\d+-");

	private final String[] dataFiles;

	private final Heuristic heuristic;

	private final Map<String, FrequencyTable> profiles;

	private final List<String> monthNames;

	private static final int FACTOR = 1;

	public Profiles(String[] dataFiles, Heuristic heuristic) {
		this.dataFiles = dataFiles;
		this.heuristic = heuristic;
		this.profiles = new HashMap<String, FrequencyTable>();
		this.monthNames = new ArrayList<String>();

		this.heuristic.getClassifier().useDuplicateResolver(true);
	}

	public Map<String, FrequencyTable> getProfiles() {
		return this.profiles;
	}

	public int size() {
		return this.profiles.size();
	}

	public boolean contains(String key) {
		return this.profiles.containsKey(key);
	}

	public void createProfile(String month, Set<BugReport> reports) {
		String name;
		Classification prediction;
		FrequencyTable profile;
		for (BugReport report : reports) {
			prediction = this.heuristic.getClassifier().classify(report);
			name = prediction.getClassification();
			if (name.equals(HeuristicClassifier.CANNOT_CLASSIFY))
				continue;

			if (this.profiles.containsKey(name) == false) {
				this.profiles.put(name, new FrequencyTable());
			}

			profile = this.profiles.get(name);
			profile.add(month);
		}
	}

	public void createProfiles(int numProfileMonths) {
		String month = null;
		Matcher matcher;
		for (int index = 0; index < this.dataFiles.length; index++) {
			String dataFile = this.dataFiles[index];
			matcher = monthYear.matcher(dataFile);

			/* Take the last match */
			while (matcher.find()) {
				month = matcher.group(1);
			}
			
			//System.out.println("Month: " + month);
			
			if (month == null) {
				System.err.println("WARNING: (Month null)" + dataFile);
			}

			Set<BugReport> reports = Utils.getReports(dataFile);
			//System.out.println(dataFile);
			if (this.monthNames.contains(month) == false)
				this.monthNames.add(month);
			createProfile(month, reports);
		}
	}

	/*
	 * Correct statistics to include months not contained in the profile
	 */
	private DescriptiveStatistics getStatistics(FrequencyTable profile) {
		DescriptiveStatistics stats = profile.getStatistics();
		for (String month : this.monthNames) {
			if (profile.getFrequency(month) == 0) {
				stats.addValue(0);
			}
		}
		return stats;
	}

	public void pruneAverage(double cutoff) {
		System.err.println("Pruning (Average): " + cutoff);
		FrequencyTable profile;
		Set<String> keys = new HashSet<String>(this.profiles.keySet());
		for (String name : keys) {
			profile = this.profiles.get(name);
			double mean = this.getStatistics(profile).getMean();
			// System.err.println("(" + name + ") Mean: " + mean);
			if (mean < cutoff) {
				this.profiles.remove(name);
			}
		}
	}

	public void pruneTotal(double cutoff) {
		System.err.println("Pruning (Total): " + cutoff);
		FrequencyTable profile;
		Set<String> keys = new HashSet<String>(this.profiles.keySet());
		for (String name : keys) {
			profile = this.profiles.get(name);
			double total = this.getStatistics(profile).getSum();
			if (name.equals("daniel_megert@ch.ibm.com")) {
				System.out.println("Total: " + total);
			}
			if (total < cutoff) {
				this.profiles.remove(name);
			}
		}
	}

	public void prunePercentage(double percentage) {
		FrequencyTable profile;
		List<FixStat> fixes = new ArrayList<FixStat>();
		int total, totalFixes = 0;
		Set<String> keys = new HashSet<String>(this.profiles.keySet());
		for (String name : keys) {
			profile = this.profiles.get(name);
			total = (int) profile.getStatistics().getSum();
			totalFixes += total;
			fixes.add(new FixStat(name, total));
		}

		Collections.sort(fixes);
		double fixesSum = 0;
		double percent = 0;
		while (percent < percentage) {
			FixStat fixStat = fixes.remove(fixes.size() - 1);
			fixesSum += fixStat.fixes;
			percent = (fixesSum / totalFixes);
		}
		for (FixStat fixStat : fixes) {
			this.profiles.remove(fixStat.name);
		}
	}

	public void pruneEntries(List entriesToKeep) {
		List<String> namesToRemove = new ArrayList<String>();
		for (String name : this.profiles.keySet()) {
			if (entriesToKeep.contains(name) == false) {
				namesToRemove.add(name);
			}
		}

		for (String name : namesToRemove) {
			this.profiles.remove(name);
		}
	}

	/*
	 * Write the profiles out to a file in CSV format
	 */
	public void writeToFile(File file) {

		try {
			PrintWriter writer = new PrintWriter(file);

			for (String month : this.monthNames) {
				writer.print("," + month);
			}
			writer.println();
			FrequencyTable profile;
			int frequency;
			int numEntries = 0;
			for (String name : this.profiles.keySet()) {
				profile = this.profiles.get(name);
				writer.print(name);
				for (String month : this.monthNames) {
					frequency = profile.getFrequency(month);
					writer.print("," + frequency);
				}
				writer.println();
				numEntries++;
			}
			writer.close();
			System.out.println("Entries Written: " + numEntries);
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + file.getName());
			System.err.println(e);
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		FrequencyTable profile;
		int scaledFrequency, count;
		for (String name : this.profiles.keySet()) {
			profile = this.profiles.get(name);
			sb.append(name + ":\n");
			for (String month : this.monthNames) {
				sb.append("\t" + month + ": ");
				scaledFrequency = this.profiles.get(name).getFrequency(month)
						/ FACTOR;
				for (count = 0; count < scaledFrequency; count++) {
					sb.append("*");
				}
				sb.append("\n");
			}
			sb.append("\n");
		}

		return sb.toString();
	}

	private class FixStat implements Comparable<FixStat> {
		public final String name;

		public final int fixes;

		public FixStat(String name, int fixes) {
			this.name = name;
			this.fixes = fixes;
		}

		public int compareTo(FixStat arg) {
			if (this.fixes < arg.fixes) {
				return -1;
			}

			if (this.fixes > arg.fixes) {
				return 1;
			}

			return 0;
		}

	}
}
