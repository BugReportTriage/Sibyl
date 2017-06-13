package ca.uleth.bugtriage.sibyl.classifier.firefox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusType;
import ca.uleth.bugtriage.sibyl.heuristic.Heuristic;
import ca.uleth.bugtriage.sibyl.heuristic.HeuristicClassifier;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class Resolver2FixerMap {

	public static final String MAP_FILENAME = FirefoxData.FIREFOX_DIR
			+ "resolverFixer.map";

	public static final int NUM_MONTHS = 8;

	private static Map<String, Set<String>> createMapping(int numMonths) {

		String[] dataFiles = Utils.getTrainingSet(FirefoxData.FIREFOX_DIR,
				NUM_MONTHS, FirefoxData.LAST_TRAINING_MONTH);
		Set<BugReport> reports = Utils.getReports(dataFiles);
		HeuristicClassifier heuristic = Heuristic.MOZILLA.getClassifier();

		String fixer, resolver;
		StatusType status;
		ResolutionType resolution;
		Map<String, Set<String>> resolverFixerMap = new HashMap<String, Set<String>>();
		Set<String> fixers;
		for (BugReport report : reports) {
			status = report.getStatus();
			resolution = report.getResolution();
			if (status.equals(StatusType.RESOLVED)
					&& resolution.equals(ResolutionType.FIXED)) {
				fixer = heuristic.classify(report).getClassification();
				resolver = report.getActivity().resolution().getName(); // TODO : Remove the middle man
				if (resolverFixerMap.containsKey(resolver) == false) {
					resolverFixerMap.put(resolver, new HashSet<String>());
				}
				fixers = resolverFixerMap.get(resolver);
				fixers.add(fixer);
			}
		}

		return resolverFixerMap;
	}

	private static void write(Map<String, Set<String>> resolverFixerMap, String filename){
		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(filename));
			System.out.println("Writing to: " + filename);
			out.writeObject(resolverFixerMap);
			out.close();
			System.out.println("Bug list written out: " + filename);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
	
	private static void print(Map<String, Set<String>> resolverFixerMap){
		StringBuffer sb;
		for (String resolver : resolverFixerMap.keySet()) {
			sb = new StringBuffer();
			sb.append("\"" + resolver + "=");
			for(String name : resolverFixerMap.get(resolver)){
				sb.append(name + ",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("\",");
			
			System.out.println(sb);
		}	
	}
	
	public static Map<String, Set<String>> read(String filename){
		
		try {
			System.out.println("Reading in bug list (" + filename + ")");
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(
					filename));
			Map<String, Set<String>> map = (Map<String, Set<String>>)in.readObject();
			return map;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			
			e.printStackTrace();
		}

		return Collections.emptyMap();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		Map<String, Set<String>> map; 
		map = createMapping(NUM_MONTHS);
		write(map, MAP_FILENAME);
		map = read(MAP_FILENAME);
		print(map);
	}

}
