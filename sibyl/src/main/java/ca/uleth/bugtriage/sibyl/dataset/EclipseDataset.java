package ca.uleth.bugtriage.sibyl.dataset;

import java.util.Calendar;
import java.util.Date;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.utils.Dates;

public class EclipseDataset {

	public static void main(String[] args) {

		if(args.length < 6){
			System.err.println("Usage:\n\t" + EclipseDataset.class.getName() + "<start day> <start month> <start year> <end day> ");
			return;
		}
		
		User user = new User(User.UNKNOWN_USER_ID, "janvik@cs.ubc.ca",
				"BugTriage", Project.PLATFORM);
		CreateDataset extrator = new CreateDataset(user);

		Calendar start = Calendar.getInstance();
		int startyear = Integer.parseInt(args[2]);
		int startMonth = Integer.parseInt(args[1]);
		int startDay = Integer.parseInt(args[0]);
		start.set(startyear, startMonth-1, startDay);

		Calendar end = Calendar.getInstance();
		int endyear = Integer.parseInt(args[5]);
		int endMonth = Integer.parseInt(args[4]);
		int endDay = Integer.parseInt(args[3]);
		end.set(endyear, endMonth-1, endDay);

		System.out.println(new Date(start.getTimeInMillis()));
		System.out.println(new Date(end.getTimeInMillis()));

		for(; start.before(end); start.add(Calendar.DATE, 1)){
			extrator.run(start.getTime(), Dates.getTomorrow(start.getTime()));
		}
	}
}
