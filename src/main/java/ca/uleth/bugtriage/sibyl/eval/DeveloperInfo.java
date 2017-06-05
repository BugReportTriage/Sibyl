package ca.uleth.bugtriage.sibyl.eval;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ca.uleth.bugtriage.sibyl.classifier.eclipse.EclipseData;
import ca.uleth.bugtriage.sibyl.classifier.firefox.FirefoxData;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class DeveloperInfo {

	public final Set<String> cantconvert;

	private final String developerInfoFilename;

	public DeveloperInfo(String developerInfoFilename) {
		this.developerInfoFilename = developerInfoFilename;
		this.cantconvert = new HashSet<String>();
	}

	/*
	 * Parses a file in format: <Bug #>|developerName,developerName,...
	 */

	private Map<Integer, Set<String>> parseDeveloperInfo() {

		Map<Integer, Set<String>> developerInfo = new HashMap<Integer, Set<String>>();

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					this.developerInfoFilename));

			Map<String, Set<String>> conversionMap = createConversionMap();

			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				String[] firstSplit = line.split("\\|");
				if (firstSplit.length < 2) {
					continue;
				}
				Integer bugNumber = new Integer(firstSplit[0]);
				String developers = firstSplit[1];
				String[] developerNames = developers.split(",");

				if (developerInfo.containsKey(new Integer(bugNumber)) == false) {
					developerInfo.put(new Integer(bugNumber), convert(
							developerNames, conversionMap));
				} else {
					System.err
							.println("Already have developer information for bug "
									+ bugNumber);
				}
			}

		} catch (FileNotFoundException e) {
			System.err.println(this.developerInfoFilename + " not found");
			//System.exit(1);
		} catch (IOException e) {
			System.err.println("Problem reading a line");
			e.printStackTrace();
		} catch (NumberFormatException e) {
			System.err.println("Problem reading a line");
			e.printStackTrace();
		}

		return developerInfo;
	}

	public Map<String, Set<String>> createConversionMap() {
		String[] conversionData = {};

		if (this.developerInfoFilename.equals(EclipseData.DEVELOPER_INFO_PACKAGE) || this.developerInfoFilename.equals(EclipseData.DEVELOPER_INFO_FILES)) {
			conversionData = EclipseData.USER_NAMES;
		} else if (this.developerInfoFilename
				.equals(FirefoxData.DEVELOPER_INFO)) {
			conversionData = FirefoxData.USER_NAMES;
			// }else if
			// (this.developerInfoFilename.equals(GccDataset.DEVELOPER_INFO)) {
			// conversionData = GCCData.USER_NAMES;
		} 
		return createConversionMap(conversionData);
	}
	
		public static Map<String, Set<String>> createConversionMap(String[] conversionData) {
		Map<String, Set<String>> conversionMap = new HashMap<String, Set<String>>();

		String[] split;
		String to, from;
		Set<String> nameSet;
		for (String namePair : conversionData) {
			split = namePair.split("=");
			from = split[0];
			to = split[1];

			nameSet = new HashSet<String>();
			if (to.contains(",")) {
				for (String developerName : to.split(",")) {
					nameSet.add(developerName);
				}
			} else {
				nameSet.add(to);
			}

			conversionMap.put(from, nameSet);
		}

		return conversionMap;
	}

	private Set<String> convert(String[] developerNames,
			Map<String, Set<String>> conversionMap) {

		Set<String> obsoleteUsers = new HashSet<String>();
		if (this.developerInfoFilename.equals(EclipseData.DEVELOPER_INFO_PACKAGE) || this.developerInfoFilename.equals(EclipseData.DEVELOPER_INFO_FILES)) {
			for (String cvsUserName : EclipseData.USER_NAMES_OBSOLETE) {
				obsoleteUsers.add(cvsUserName);
			}
		}

		Set<String> developerList = new HashSet<String>();

		Set<String> convertedDeveloper;
		for (String developer : developerNames) {
			if (obsoleteUsers.contains(developer))
				continue;

			convertedDeveloper = conversionMap.get(developer);
			if (convertedDeveloper != null) {
				for (String name : convertedDeveloper) {
					developerList.add(name);
				}
				continue;
			}
			// System.out.println("Cannot convert: " +
			// developer);
			this.cantconvert.add(developer);

			developerList.add(developer);
		}

		return developerList;
	}

	public Map<Integer, Set<String>> getDeveloperInfo() {
		return this.parseDeveloperInfo();
	}

	public Set<BugReport> getTestingSet(String[] testingSetFilename) {
		Set<BugReport> reports = Utils.getReports(testingSetFilename);
		Set<BugReport> testingReports = new HashSet<BugReport>();
		Map<Integer, Set<String>> developerInfo = this.getDeveloperInfo();

		for (BugReport report : reports) {
			if (developerInfo.containsKey(new Integer(report.getId()))) {
				testingReports.add(report);
			}
		}

		return testingReports;
	}

	public Set<String> getCantconvert() {
		return this.cantconvert;
	}

}
