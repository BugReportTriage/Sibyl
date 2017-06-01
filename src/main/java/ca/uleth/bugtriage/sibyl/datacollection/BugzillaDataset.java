package ca.uleth.bugtriage.sibyl.datacollection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import ca.uleth.bugtriage.sibyl.Project;

public class BugzillaDataset {

	private static final String DATA_FIELDS = "id," + "cc," + "component," + "summary," + "creator," + "assigned_to,"
			+ "resolution," + "creation_time," + "op_sys," + "platform," + "last_change_time," + "priority,"
			+ "dupe_of," + "severity," + "status";

	// https://bugzilla.mozilla.org/rest/bug?product=Firefox&resolution=FIXED&status=VERIFIED&creation_time=2016-01-01
	public static String constructUrl(Project project) {
		return project.getURL()
				/* RESOLVED and ASSIGNED */
				+ "/rest/bug?" + "product=" + project.getProduct()
				// Fixed bugs that are marked RESOLVED, VERIFIED or CLOSED
				+ "&resolution=FIXED&status=RESOLVED&status=VERIFIED&status=CLOSED" + "&creation_time="
				+ project.getStartDate() + "&include_fields=" + DATA_FIELDS;
	}

	public static String getData(String repositoryUrl) {
		InputStream is = null;
		try {
			try {
				is = new URL(repositoryUrl).openStream();
				BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				StringBuilder sb = new StringBuilder();
				int cp;
				while ((cp = rd.read()) != -1) {
					sb.append((char) cp);
				}
				return sb.toString();

			} finally {
				if (is != null)
					is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void writeToFile(Project project, String data) {
		try {

			File dataFile = new File(
					project.getDataDir() + "/" + project.getName() + "_" + project.getStartDate() + ".json");
			File parent = new File(dataFile.getParent());
			if (parent.exists() == false) {
				parent.mkdir();
			}

			FileWriter out = new FileWriter(dataFile);
			System.out.println("Writing to: " + dataFile.getName());
			out.write(data);
			out.close();
			System.out.println("Bugs written out: " + dataFile.getName());
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
	}
}
