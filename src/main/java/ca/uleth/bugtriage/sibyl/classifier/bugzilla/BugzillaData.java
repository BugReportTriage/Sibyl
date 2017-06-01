package ca.uleth.bugtriage.sibyl.classifier.bugzilla;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class BugzillaData {
	public final static String BUGZILLA_DIR = Environment.getBugDataDir() + "bugzilla/";

	public final static String BUGZILLA_CVS_DIR = Environment.getCVSDataDir()
	+ "bugzilla/";
	
	public final static String DUPLICATES = BUGZILLA_DIR
			+ "bugzillaDups.bugs";
	
	public final static String DEVELOPER_INFO = BUGZILLA_CVS_DIR + "bugzilla.bug3NAMEdev"; 
	
	public static final int TESTING_MONTH = 10;
	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;
	
	public static final String[] USER_NAMES = {
		"justdave@syndicomm.com=justdave@syndicomm.com,justdave@bugzilla.org", 
		"justdave@bugzilla.org=justdave@syndicomm.com,justdave@bugzilla.org",
		
		"jake@acutex.net=jake@acutex.net,jake@bugzilla.org", 
		"jake@bugzilla.org=jake@bugzilla.org,jake@acutex.net", 
		"travis@sedsystems.ca=travis@sedsystems.ca", 
		
		//uid623, 
		"seth@cs.brandeis.edu=seth@cs.brandeis.edu", 
		"ghendricks@novell.com=ghendricks@novell.com", 
		"barnboy@trilobyte.net=barnboy@trilobyte.net", 
		"kiko@async.com.br=kiko@async.com.br", 
		
		"timeless@mac.com=timeless@mac.com,timeless@mozdev.org",
		"timeless@mozdev.org=timeless@mozdev.org,timeless@mac.com",
		
		"preed@sigkill.com=preed@sigkill.com", 
		"mozilla@colinogilvie.co.uk=mozilla@colinogilvie.co.uk", 
		"bbaetz@acm.org=bbaetz@acm.org", 
		"dkl@redhat.com=dkl@redhat.com", 
		"jeff.hedlund@matrixsi.com=jeff.hedlund@matrixsi.com", 
		"zach@zachlipton.com=zach@zachlipton.com", 
		/*
		olav@bkor.dhs.org, 
		andrew@redhat.com, 
		dave@intrec.com, 
		blakeross@telocity.com, 
		*/
		"lpsolit@gmail.com=LpSolit@gmail.com", 
		/*
		bbaetz@student.usyd.edu.au, 
		braddr@puremagic.com, 
		endico@mozilla.org, 
		bugzilla@glob.com.au, 
		john@johnkeiser.com, 
		
		karl@kornel.name, 
		karl.kornel@mindspeed.com
		
		caillon@returnzero.com, 
		terry@mozilla.org, 
		myk@mozilla.org, 
		jocuri@softhome.net, 
		jouni@heikniemi.net, 
		
		mbarnson@excitehome.net, 
		mbarnson@sisna.com
		
		mkanat@bugzilla.org, 
		mkanat@kerio.com
		
		bbaetz@cs.mcgill.ca, 
		wurblzap@gmail.com, 
		harrison@netscape.com, 
		jkeiser@netscape.com, 
		bugreport@peshkin.net, 
		gerv@gerv.net, 
		terry@netscape.com, 
		tara@tequilarista.org, 
		bryce-mozilla@nextbus.com, 
		erik@dasbistro.com, 
		cyeh@bluemartini.com, 
		dmose@mozilla.org, 
		matty@chariot.net.au, 
		burnus@gmx.de, 
		vladd@bugzilla.org, 
		donm@bluemartini.com
		*/
		};
}
