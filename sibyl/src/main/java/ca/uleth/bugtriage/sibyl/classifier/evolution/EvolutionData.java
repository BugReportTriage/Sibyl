package ca.uleth.bugtriage.sibyl.classifier.evolution;

import ca.uleth.bugtriage.sibyl.utils.Environment;

public class EvolutionData {
	public final static String EVOLUTION_DIR = Environment.getBugDataDir() + "evolution/";
	
	public final static String EVOLUTION_CVS_DIR = Environment.getCVSDataDir()
	+ "evolution/";
	
	public static final String DUPLICATES = EVOLUTION_DIR + "evolutionDups.bugs";
	
	
	public final static String DEVELOPER_INFO = EVOLUTION_CVS_DIR + "evolution.bugNAMEdev"; 
	
	public static final int TESTING_MONTH = 7;
	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;
	
	public static final String[] USER_NAMES = {
		"ireneh=Irene.Huang@sun.com",     
		//"pchakravarthi@novell.com",        
		//"drichard@largo.com",      
		"jeffcai=jeff.cai@sun.com",        
		"simonz=simon.zheng@Sun.COM",     
		"psankar=psankar@novell.com",      
		"shres=sshreyas@gmail.com",     
		"vvaradan=vvaradhan@novell.com",    
		"rsushma=rsushma@novell.com",     
		"liyuan=li.yuan@sun.com", 
		"fejj=fejj@novell.com", 
		"kbrae=guenther@rudersport.de", // not at all sure  
		//"ajaysusarla@gmail.com",   
		"kharish=kharish@novell.com",      
		"dsharma=sdevashish@novell.com",   
		"pchen=pchenthill@novell.com", // not certain   
		"sragavan=sragavan@novell.com",     
		"aklapper=a9016009@gmx.de", 

		};
}
