package ca.uleth.bugtriage.sibyl.utils;

import java.util.Calendar;
import java.util.Date;

public class Dates {
	
	public static Date getYesterday(Date today) {
		Calendar date = Calendar.getInstance();
		date.setTime(today);
		date.add(Calendar.DATE, -1);
		return date.getTime();
	}
	
	public static Date getTomorrow(Date today) {
		Calendar date = Calendar.getInstance();
		date.setTime(today);
		date.add(Calendar.DATE, 1);
		return date.getTime();
	}
}
