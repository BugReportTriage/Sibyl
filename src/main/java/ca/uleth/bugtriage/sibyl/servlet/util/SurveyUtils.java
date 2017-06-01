package ca.uleth.bugtriage.sibyl.servlet.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

import ca.uleth.bugtriage.sibyl.sibyl.Sibyl;
import ca.uleth.bugtriage.sibyl.sibyl.SibylUser;
import ca.uleth.bugtriage.sibyl.utils.Messages;

public class SurveyUtils {

	public static String formatData(Map<String, String> data) {
		StringBuffer sb = new StringBuffer();
		for (String key : data.keySet()) {
			if (data.get(key) == "" || key.equals("assignmentSurvey"))
				continue;
			sb.append(key + " : " + data.get(key) + "\n");
		}
		return sb.toString();
	}
	
	public static void writeSurveyToFile(SibylUser user,
			Map<String, String> data, String extention, Messages messages) {
	
		File submittedDir = Sibyl.createSubmittedDir(user, messages);
	
		String date = Sibyl.DMY_formatter.format(new Date(System
				.currentTimeMillis()));
		File submittedSurvey = new File(submittedDir + "/" + date + extention);
	
		try {
			FileWriter surveyFile = new FileWriter(submittedSurvey,
					submittedSurvey.exists());
	
			if(submittedSurvey.exists()){
				surveyFile.append("----------------------------\n");
			}
			for (String key : data.keySet()) {
				if (data.get(key) == "" || key.equals("assignmentSurvey") || key.equals("recommenderSurvey"))
					continue;
				surveyFile.append(key + ": " + data.get(key) + "\n");
			}
			surveyFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
