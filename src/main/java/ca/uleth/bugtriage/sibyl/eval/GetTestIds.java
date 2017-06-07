package ca.uleth.bugtriage.sibyl.eval;

import ca.uleth.bugtriage.sibyl.Project;

public class GetTestIds {

	public static String constructQuery(Project project, String start, String end){
		
		String projectName = (project.name.contains("Eclipse")) ? "Eclipse" : project.name ;
		
		StringBuffer query = new StringBuffer();
		query.append(project.url);
		query.append("buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&");
		if(project.name.contains("Eclipse")){
			query.append("classification=" + projectName);
		}
		query.append("&product=" + project.product);
		query.append("&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&status_whiteboard_type=allwordssubstr&status_whiteboard=&keywords_type=allwords&keywords=&");
		query.append("bug_status=RESOLVED&");
		query.append("bug_status=VERIFIED&");
		query.append("bug_status=CLOSED&");
		query.append("resolution=FIXED");
		query.append("&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&");
		query.append("chfieldfrom=" + start + "&");
		query.append("chfieldto=" + end);
		query.append("&chfield=resolution&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=");
		return query.toString();
	}	
}
