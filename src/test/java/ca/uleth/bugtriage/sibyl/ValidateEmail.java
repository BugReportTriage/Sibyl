package ca.uleth.bugtriage.sibyl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class ValidateEmail {

	private static final String SCRIPT_FILE = Environment.getWebPageDir() + "emailValidation.js";
	public static String get() {
		StringBuffer sb = new StringBuffer();
		
		try {
			BufferedReader reader = new BufferedReader(new FileReader(SCRIPT_FILE));
			for(String line = reader.readLine(); line != null; line = reader.readLine()){
				sb.append(line + "\n");
			}
		} catch (FileNotFoundException e) {
			sb.append(e.getMessage());
		} catch (IOException e) {
			sb.append(e.getMessage());
		}
		
		return sb.toString();
	}

	
}
