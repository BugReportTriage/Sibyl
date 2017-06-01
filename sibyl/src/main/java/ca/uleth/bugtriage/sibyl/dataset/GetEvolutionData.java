package ca.uleth.bugtriage.sibyl.dataset;

import java.util.Calendar;
import java.util.Date;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.utils.Dates;

public class GetEvolutionData {

	public static void main(String[] args) {
		int startDay = 11;
		int startMonth = 11;
		int startYear = 2005;
		
		int endDay = 30;
		int endMonth = 11;
		int endYear = 2006;
		
		
		
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca",
				"BugTriage", Project.EVOLUTION);
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
