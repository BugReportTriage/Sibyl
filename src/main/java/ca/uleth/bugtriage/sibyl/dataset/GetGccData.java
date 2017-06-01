package ca.uleth.bugtriage.sibyl.dataset;

import java.util.Calendar;
import java.util.Date;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.utils.Dates;

public class GetGccData {

	public static void main(String[] args) {
		int startDay = 01;
		int startMonth = 11;
		int startYear = 2006;
		
		int endDay = 31;
		int endMonth = 12;
		int endYear = 2006;
		
		
		System.out.println("Getting...");
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca",
				"BugTriage", Project.GCC);
		CreateDataset extrator = new CreateDataset(user);
		
		Calendar start = Calendar.getInstance();
		start.set(startYear, startMonth-1, startDay);
		
		Calendar end = Calendar.getInstance();
		end.set(endYear, endMonth-1, endDay);
		
		while(start.before(end)){
			extrator.run(start.getTime(), Dates.getTomorrow(start.getTime()));
			start.add(Calendar.DATE, 1);
		}
	}
}
