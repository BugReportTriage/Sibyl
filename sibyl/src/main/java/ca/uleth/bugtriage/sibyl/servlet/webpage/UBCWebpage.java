package ca.uleth.bugtriage.sibyl.servlet.webpage;

import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.servlet.util.Webpage;

public abstract class UBCWebpage {

	protected abstract String content(User user);
	protected abstract String scripts();
	protected abstract String getPageTitle();
	protected abstract String getPageFooter();
	
	public String createPage(User user){
		StringBuffer page = new StringBuffer();
		
		page.append(Webpage.startPage(this.getPageTitle(), "", scripts()));
		page.append(content(user));
		page.append(Webpage.endPage(this.getPageFooter()));
		
		return page.toString();
		
	}
}
