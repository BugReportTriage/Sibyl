package ca.uleth.bugtriage.sibyl.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import ca.uleth.bugtriage.sibyl.Classification;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;

public class Profiles {

    private final Set<BugReport> reports;

    private final Heuristic heuristic;

    private final Map<String, Integer> profiles;

    private final List<String> monthNames;

    private static final int FACTOR = 1;

    public Profiles(Set<BugReport> reports, Heuristic heuristic) {
	this.reports = reports;
	this.heuristic = heuristic;
	this.profiles = new TreeMap<String, Integer>();
	this.monthNames = new ArrayList<String>();

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

    public void pruneTotal(double cutoff) {
	System.err.println("Pruning (Total): " + cutoff);
	Integer fixedTotal;
	Set<String> keys = new HashSet<String>(this.profiles.keySet());
	for (String name : keys) {
	    fixedTotal = this.profiles.get(name);
	    if (fixedTotal < cutoff) {
		this.profiles.remove(name);
	    }
	}
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();

	int scaledFrequency, count;
	for (String name : this.profiles.keySet()) {
	    sb.append(name + ": ");
	    scaledFrequency = this.profiles.get(name) / FACTOR;
	    for (count = 0; count < scaledFrequency; count++) {
		sb.append("*");
	    }
	    sb.append("\n");
	}

	return sb.toString();
    }
}
