package ca.uleth.bugtriage.sibyl.eval;

import java.util.List;

import ca.uleth.bugtriage.sibyl.Login;
import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.datacollection.BugzillaSearch;

public class GetTestIds {

	public static String constructQuery(Project project, String start, String end){
		
		String projectName = (project.getName().contains("Eclipse")) ? "Eclipse" : project.getName() ;
		
		StringBuffer query = new StringBuffer();
		query.append(project.getUrl());
		query.append("buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&");
		if(project.getName().contains("Eclipse")){
			query.append("classification=" + projectName);
		}
		query.append("&product=" + project.getProduct());
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
	
	public static void main(String[] args) {
		Project project = Project.MYLAR;
		User user = new User(User.UNKNOWN_USER_ID, Login.USER, Login.PASSWORD, project);
		
		String start = "2006-10-01";
		String end = "2006-10-31";
		String queryUrl = constructQuery(project, start, end);
		//System.out
			//	.println("https://bugzilla.mozilla.org/buglist.cgi?query_format=advanced&short_desc_type=allwordssubstr&short_desc=&product=Firefox&long_desc_type=allwordssubstr&long_desc=&bug_file_loc_type=allwordssubstr&bug_file_loc=&status_whiteboard_type=allwordssubstr&status_whiteboard=&keywords_type=allwords&keywords=&bug_status=RESOLVED&bug_status=VERIFIED&bug_status=CLOSED&resolution=FIXED&emailtype1=substring&email1=&emailtype2=substring&email2=&bugidtype=include&bug_id=&votes=&chfieldfrom=2006-06-01&chfieldto=2006-06-30&chfield=resolution&chfieldvalue=&cmdtype=doit&order=Reuse+same+sort+as+last+time&field0-0-0=noop&type0-0-0=noop&value0-0-0=");
		System.out.println(queryUrl);

		BugzillaSearch searcher = new BugzillaSearch(user, queryUrl);
		List<String> ids = searcher.search();
		//System.out.println("Size: " + ids.size());

		for (String id : ids) {
			System.out.println(id);
		}

	}
}
