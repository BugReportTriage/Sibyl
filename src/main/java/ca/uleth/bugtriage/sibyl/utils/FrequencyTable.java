package ca.uleth.bugtriage.sibyl.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.math.stat.descriptive.DescriptiveStatistics;

public class FrequencyTable implements Serializable {

	private Map<String, Integer> table = new HashMap<String, Integer>();

	/**
	 * Generated serial id
	 */
	private static final long serialVersionUID = 1L;

	public void add(String key) {
		if (this.table.containsKey(key)) {
			Integer value = this.table.get(key);
			value = new Integer(value.intValue() + 1);
			this.table.put(key, value);
		} else {
			this.table.put(key, new Integer(1));
		}
	}

	public boolean remove(String key) {
		return this.table.remove(key) != null;
	}

	/**
	 * @return keys in descending frequency sorted order
	 */
	public List<String> getKeys() {

		List<String> keys = new ArrayList<String>();
		List<FrequencyEntry> entries = sortByFrequency();
		for (FrequencyEntry entry : entries) {
			keys.add(entry.getKey());
		}
		return keys;
	}

	public int getFrequency(String key) {
		if (this.table.get(key) != null) {
			return (this.table.get(key)).intValue();
		}
		// System.err.println(key + " not found in frequency table");
		return 0;
	}

	public DescriptiveStatistics getStatistics() {
		DescriptiveStatistics stats = new DescriptiveStatistics();
		for (Integer value : this.table.values()) {
			stats.addValue(value.doubleValue());
		}

		return stats;
	}

	private List<FrequencyEntry> sortByFrequency() {

		List<FrequencyEntry> sortedEntries = new ArrayList<FrequencyEntry>();
		for (Map.Entry<String, Integer> entry : this.table.entrySet()) {
			sortedEntries.add(new FrequencyEntry(this, entry.getKey(), entry
					.getValue().intValue()));
		}

		Collections.sort(sortedEntries, Collections.reverseOrder());
		return sortedEntries;
	}

	public List<String> getTopX(int x) {
		List<FrequencyEntry> entries = sortByFrequency();

		FrequencyEntry entry;
		List<String> topEntries = new ArrayList<String>(x);
		for (int index = 0; index < x; index++) {
			entry = entries.get(index);
			topEntries.add(entry.getKey());
		}
		return topEntries;
	}

	@Override
	public String toString() {

		List<FrequencyEntry> entries = sortByFrequency();

		StringBuffer sb = new StringBuffer();
		for (FrequencyEntry entry : entries) {
			sb.append(entry + "\n");
		}
		return sb.toString();
	}

	class FrequencyEntry implements Comparable<FrequencyEntry> {

		private final String key;

		private final int frequency;

		public FrequencyEntry(FrequencyTable frequencyTable, String key,
				int frequency) {
			this.key = key;
			this.frequency = frequency;
		}

		public int getFrequency() {
			return frequency;
		}

		public String getKey() {
			return key;
		}

		@Override
		public String toString() {
			return this.key + "\t" + this.frequency;
		}

		public int compareTo(FrequencyEntry entry) {
			if (this.frequency < entry.frequency) {
				return -1;
			} else if (this.frequency == entry.frequency) {
				return 0;
			} else {
				return 1;
			}
		}

	}
}
