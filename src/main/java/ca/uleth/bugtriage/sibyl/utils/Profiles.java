package ca.uleth.bugtriage.sibyl.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import org.apache.commons.math.stat.descriptive.SummaryStatistics;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class Profiles {

	private final Set<BugReport> reports;

	private final Heuristic heuristic;

	private final Map<String, Integer> profiles;

	private static final int FACTOR = 10;

	public Profiles(Set<BugReport> reports, Heuristic heuristic) {
		this.reports = reports;
		this.heuristic = heuristic;
		this.profiles = new TreeMap<String, Integer>();
		new ArrayList<String>();

		this.heuristic.getClassifier().useDuplicateResolver(true);
	}

	public Map<String, Integer> getProfiles() {
		return this.profiles;
	}

	public int size() {
		return this.profiles.size();
	}

	public boolean contains(String key) {
		return this.profiles.containsKey(key);
	}

	public void create() {
		String name;
		Classification prediction;
		for (BugReport report : reports) {
			prediction = this.heuristic.getClassifier().classify(report);
			name = prediction.getClassification();
			if (name.equals(HeuristicClassifier.CANNOT_CLASSIFY))
				continue;

			if (this.profiles.containsKey(name) == false) {
				this.profiles.put(name, new Integer(1));
			}

			this.profiles.put(name, this.profiles.get(name) + 1);
		}
	}

	public void pruneTotal(double low, double high) {
		System.err.println("Pruning (Total): " + low + "-" + high);
		Integer fixedTotal;
		Set<String> keys = new HashSet<String>(this.profiles.keySet());
		for (String name : keys) {
			fixedTotal = this.profiles.get(name);
			if (fixedTotal < low || fixedTotal > high) {
				this.profiles.remove(name);
			}
		}
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();

		int scaledFrequency, count;
		for (Entry<String, Integer> entry : Utils.entriesSortedByValues(this.profiles)) {
			sb.append(entry.getKey() + ": ");
			scaledFrequency = entry.getValue() / FACTOR;
			for (count = 0; count < scaledFrequency; count++) {
				sb.append("*");
			}
			sb.append(" (" + entry.getValue() + ")\n");
		}

		return sb.toString();
	}

	// Doesn't work due to long tail
	public void pruneStdDev() {
		SummaryStatistics stats = new SummaryStatistics();

		for (Integer fixed : this.profiles.values()) {
			stats.addValue(fixed.doubleValue());
		}

		double mean = stats.getMean();
		double stdDev = stats.getStandardDeviation();
		System.err.println("Mean: " + mean + " stdDecv: " + stdDev);

		Set<String> keys = new HashSet<String>(this.profiles.keySet());
		for (String name : keys) {
			int fixedTotal = this.profiles.get(name);
			if (fixedTotal < mean - (2 * stdDev) || fixedTotal > mean + (2 * stdDev)) {
				this.profiles.remove(name);
			}
		}
	}

	// Prune by percentages
	public void prunePercentage(double topPercent, double bottomPercent) {

		int total = this.profiles.size();
		int top = (int) Math.floor(total * topPercent);
		int bottom = (int) Math.floor(total * bottomPercent);

		SortedSet<Entry<String, Integer>> sortedByFixed = Utils.entriesSortedByValues(this.profiles);
		Iterator<Entry<String, Integer>> entryItr = sortedByFixed.iterator();

		// Determine the top cutoff value
		for (int i = 0; i < top; i++)
			entryItr.next();

		int topCutoff = entryItr.next().getValue();

		// Remove top percentage
		for (Entry<String, Integer> entry : sortedByFixed) {
			if (entry.getValue() > topCutoff)
				this.profiles.remove(entry.getKey());
		}
		// Determine the bottom cutoff value
		for (int i = 0; i < sortedByFixed.size() - (top + bottom); i++)
			entryItr.next();

		int bottomCutoff = entryItr.next().getValue();

		// Remove bottom percentage
		for (Entry<String, Integer> entry : sortedByFixed) {
			if (entry.getValue() < bottomCutoff)
				this.profiles.remove(entry.getKey());
		}

	}
}
