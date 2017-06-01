package ca.uleth.bugtriage.sibyl.classifier.gcc;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class GccData {
	public final static String GCC_DIR = Environment.getBugDataDir() + "gcc-recent/";

	public final static String GCC_SVN_DIR = Environment.getCVSDataDir()
	+ "gcc/";
	
	public final static String DUPLICATES = GCC_DIR
			+ "gccDups.bugs";
	
	//public final static String DEVELOPER_INFO = FIREFOX_DIR + "firefox.bug2dev"; //"developerInfoMay2005.firefox";
	//public final static String DEVELOPER_INFO = FIREFOX_DIR + "firefox.bug1NAMEdev"; 
	//public final static String DEVELOPER_INFO = FIREFOX_DIR + "firefox.bug2NAMEdev"; 
	public final static String DEVELOPER_INFO = GCC_SVN_DIR + "gcc.bugNAMEdev"; 
	//public final static String DEVELOPER_INFO = FIREFOX_DIR + "firefoxTime.bug2NAMEdev"; 
	
	public static final int TESTING_MONTH = 12;
	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;
	
	public static final String[] USER_NAMES = {
		"aj=aj@gcc.gnu.org",
		"aldyh=aldyh@gcc.gnu.org",
		"amodra=amodra@bigpond.net.au,amodra@gcc.gnu.org",
		"amylaar=amylaar@gcc.gnu.org",
		"aoliva=aoliva@gcc.gnu.org",
		"aph=aph@gcc.gnu.org",
		
		"bje=bje@gcc.gnu.org",
		"bkoz=bkoz@gcc.gnu.org",
		"bosch=bosch@gcc.gnu.org",
		"bonzini=bonzini@gcc.gnu.org",
		"bothner=bothner@gcc.gnu.org",
		
		"charlet=charlet@gcc.gnu.org",
		"dalej=dalej@apple.com,dalej@gcc.gnu.org",
		"dberlin=dberlin@gcc.gnu.org",
		"dnovillo=dnovillo@gcc.gnu.org",
		"dannysmith=dannysmith@users.sourceforge.net",
		"dj=dj@redhat.com",
		"dje=dje@gcc.gnu.org",
		"danglin=danglin@gcc.gnu.org",
		
		"ebotcazou=ebotcazou@gcc.gnu.org",
		"echristo=echristo@redhat.com",
		
		"fx=fxcoudert@gcc.gnu.org",
		"fche=fche@redhat.com",
		"fengwang=fengwang@gcc.gnu.org",
		"fitzsim=fitzsim@redhat.com",
		"fxcoudert=fxcoudert@gcc.gnu.org",
		
		"gdr=gdr@gcc.gnu.org",
		"geoffk=geoffk@gcc.gnu.org",
		"green=green@redhat.com",
		
		"hubicka=hubicka@gcc.gnu.org",
		
		"ian=ian@airs.com",
		"irar=irar@il.ibm.com",
		
		"jakub=jakub@gcc.gnu.org",
		"jason=jason@gcc.gnu.org,jason@redhat.com",
		"jsm28=jsm28@gcc.gnu.org",
		"jvdelisle=jvdelisle@gcc.gnu.org",
		
		"kazu=kazu@gcc.gnu.org",
		"kenner=kenner@vlsi1.ultra.nyu.edu",
		"kargl=kargl@gcc.gnu.org",
		
		"law=law@gcc.gnu.org,law@redhat.com",
		"lerdsuwa=lerdsuwa@gcc.gnu.org",
		"lmillward=lmillward@gcc.gnu.org",
		
		"mmitchel=mmitchel@gcc.gnu.org",
		"mark=mark@codesourcery.com",
		
		"nathan=nathan@gcc.gnu.org",
		"neil=neil@gcc.gnu.org",
		"neroden=neroden@gcc.gnu.org",
		
		"pinskia=pinskia@gcc.gnu.org",
		"pault=pault@gcc.gnu.org",
		"pbrook=pbrook@gcc.gnu.org",
		"pme=pme@gcc.gnu.org",
		"paolo=bonzini@gcc.gnu.org", // same as bonzini
		
		"reichelt=reichelt@gcc.gnu.org",
		"rth=rth@gcc.gnu.org",
		"rakdver=rakdver@gcc.gnu.org",
		"rearnsha=rearnsha@gcc.gnu.org",
		"ro=ro@gcc.gnu.org",
		"rsandifo=rsandifo@gcc.gnu.org",
		"rguenth=rguenth@gcc.gnu.org",
		
		"steven=steven@gcc.gnu.org",
		"schwab=schwab@suse.de",
		"sje=sje@cup.hp.com",
		
		"tobi=tobi@gcc.gnu.org",
		"tromey=tromey@gcc.gnu.org",
		"toon=toon@moene.indiv.nluug.nl",
		"tkoenig=tkoenig@gcc.gnu.org",
		
		"wilson=wilson@gcc.gnu.org",

		"zack=zackw@panix.com",
		"zlaski=zlaski@apple.com",
		"zlomek=zlomek@gcc.gnu.org",


	};

	public static final String URL = "http://gcc.gnu.org/bugzilla/";

	public static final String PROJECT = "gcc";
	
	
}
