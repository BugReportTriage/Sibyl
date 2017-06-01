package ca.uleth.bugtriage.sibyl.classifier.eclipse;

import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class EclipseData {
	// private final static String ECLIPSE_DIR = Environment.dataDir +
	// "eclipse/resolved/";
	public final static String ECLIPSE_DIR = Environment.getBugDataDir()
			+ "eclipse/";

	public final static String ECLIPSE_CVS_DIR = Environment.getCVSDataDir()
			+ "eclipse/";

	public final static String DUPLICATES = ECLIPSE_DIR + "eclipseDups.bugs";

	// public static final String DEVELOPER_INFO = ECLIPSE_DIR +
	// "eclipse.bug2dev";
	public static final String DEVELOPER_INFO_PACKAGE = ECLIPSE_CVS_DIR
			+ "eclipse.bugNAMEdev";
	public static final String DEVELOPER_INFO_FILES = ECLIPSE_CVS_DIR
	+ "eclipse.bugNAMEdev-files";

	// public static final String DEVELOPER_INFO = ECLIPSE_DIR +
	// "eclipse.bugUNIdev";
	// public static final String DEVELOPER_INFO = ECLIPSE_DIR +
	// "eclipse.bugUNI-NAMEdev";

	public static final int TESTING_MONTH = 6;

	public static final int LAST_TRAINING_MONTH = TESTING_MONTH - 1;

	public static final String[] USER_NAMES_OBSOLETE = {
			// "malice", // from 2001 - see org.eclipse.swt
			// "eidsness", // from 2004 - see org.eclipse.ui
			// "jeem", // from 2003 - see org.eclipse.ui

			// Derived list (developers with no log entries in 2006 or 2005)
			/*
			 * "egamma", "cvs", "chrix", "james", "kmaetzel", "lbourlier",
			 * "jburns", "dwilson", "malice", "wadman", "eidsness", "torres",
			 * "bshingar", "jlemieux", "tmaeder", "lparsons", "sdimitro2",
			 * "lkues", "lchui", "kradloff", "mcq", "jszursze", "cmckillop",
			 * "kevinm", "davids", "rodrigo", "sarsenau", "vlad", "randyg",
			 * "kevinh", "kdkelley", "tellison", "cmclaren", "knutr", "eduardo",
			 * "airvine", "cknaus", "ssq", "bfarn", "rperetti", "celek",
			 * "ptobias", "droberts", "yamanaka", "cmarti", "daved"
			 */

			// Derived list (developers with no log entries in June 2006)
			"egamma", "cvs", "chrix", "james", "cgoldthor", "kmaetzel",
			"hargrave", "lbourlier", "jfogell", "jburns", "dwilson",
			"kkolosow", "malice", "wadman", "mfaraj", "eidsness", "torres",
			"jlemieux", "tmaeder", "bshingar", "lparsons", "dpollock",
			"sdimitro2", "lkues", "ccornu", "bbiggs", "kradloff", "lchui",
			"jeff", "mcq", "jszursze", "kevinm", "cmckillop", "davids",
			"rodrigo", "sarsenau", "vlad", "deboer", "randyg", "kevinh",
			"kdkelley", "tellison", "jaburns", "ssarkar", "jeem", "cmclaren",
			"knutr", "mvanmeek", "eduardo", "airvine", "cknaus", "ssq",
			"bfarn", "dbirsan", "rperetti", "celek", "ptobias", "sxenos",
			"mhatem", "yamanaka", "droberts", "rchaves", "lkemmel", "cmarti",
			"veronika", "dbaeumer", "daved", "nick", "mvoronin",

	};

	public static final String[] USER_NAMES = {
			"acovas=andrea_covas@ca.ibm.com",
			"aweinand=andre_weinand@ch.ibm.com",
			"airvine=lenar_hoyt@yahoo.com",
			"akiezun=akiezun@mit.edu",

			"bbiggs=bbiggs@ca.ibm.com, billy.biggs@eclipse.org",
			"bshingar=bshingar@ca.ibm.com",
			"bbokowsk=Boris_Bokowski@ca.ibm.com",
			"bbokowski=Boris_Bokowski@ca.ibm.com",
			"btripkovic=btripkov@ca.ibm.com",
			"bbaumgart=benno_baumgartner@ch.ibm.com",

			"celek=celek@ca.ibm.com",
			"cknaus=Claude_Knaus@oti.com",
			"carolyn=Carolyn_MacLeod@ca.ibm.com",
			"ccornu=christophe_cornu@ca.ibm.com",
			"chrix=christophe_cornu@ca.ibm.com",
			"cmclaren=csmclaren@andelain.com",
			"cmarti=christof_marti@ch.ibm.com",
			"curtispd=curtispd@ca.ibm.com",
			"cgoldthor=cgold@us.ibm.com",

			"dbirsan=birsan@ca.ibm.com",
			"deboer=deboer@ca.ibm.com",
			"dejan=dejan@ca.ibm.com",
			"dj=dj_houghton@ca.ibm.com",
			"dpollock=douglas.pollock@magma.ca,pollockd@ca.ibm.com,dpollock@acm.org", // two
			// addresses
			"dwilson=debbie_wilson@ca.ibm.com",
			"dmegert=daniel_megert@ch.ibm.com",
			"droberts=dean_roberts@ca.ibm.com",
			"wadman=dean_roberts@ca.ibm.com",
			"dbaeumer=dirk_baeumer@ch.ibm.com",
			"darins=Darin_Swanson@us.ibm.com",
			"darin=Darin_Wright@ca.ibm.com",
			"davids=davidms@ca.ibm.com",
			"daved=daved@dis-corp.com",

			"eduardo=eduardo_pereira@ca.ibm.com",
			"egamma=erich_gamma@ch.ibm.com",
			"erich=erich_gamma@ch.ibm.com",
			"emoffatt=emoffatt@ca.ibm.com",

			"fheidric=felipe_heidrich@ca.ibm.com",

			"ggayed=grant_gayed@ca.ibm.com",
			"gheorghe=gheorghe@ca.ibm.com",
			"gkarasiu=karasiuk@ca.ibm.com",

			"hargrave=hargrave@us.ibm.com",
			
			"ikhelifi=ines@vt.edu",

			"jburns=jared_burns@us.ibm.com", // jaredburns@acm.org,
			"jaburns=jared_burns@us.ibm.com",
			"johna=john_arthorne@ca.ibm.com",
			"jlemieux=jean-michel_lemieux@ca.ibm.com",
			"jeff=jeff_mcaffer@ca.ibm.com",
			"jfogell=jfogell@us.ibm.com",
			"james=James_Moody@ca.ibm.com",
			"jszursze=eclipse@szurszewski.com",
			"jszurszewski=eclipse@szurszewski.com",
			"jeromel=jerome_lanneluc@fr.ibm.com",
			"jeem=jim_des_rivieres@ca.ibm.com",
			"jed=jed.anderson@genuitec.com",
"jlebrun=Jacques_lebrun@oti.com",
"jbrown=jeff_brown@oti.com",
			
			"kevinh=Kevin_Haaland@ca.ibm.com",
			"kevinm=Kevin_McGuire@ca.ibm.com",
			"khorne=kim_horne@ca.ibm.com",
			"kkolosow=konradk@ca.ibm.com",
			"kmoir=kmoir@ca.ibm.com",
			"karice=Karice_McIntyre@ca.ibm.com",
			"kdkelley=kdkelley@ca.ibm.com",
			"knutr=knut_radloff@us.ibm.com",
			"kradloff=knut_radloff@us.ibm.com", // same as knutr?
			"krbarnes=krbarnes@ca.ibm.com",
			"kmaetzel=kai-uwe_maetzel@ch.ibm.com",
			"kcornell=kcornell@ca.ibm.com",

			"lkues=lynne_kues@us.ibm.com",
			"lparsons=lparsons@ca.ibm.com",
			"lbourlier=eclipse@skyluc.org",
			"lkemmel=lkemmel@il.ibm.com",
			"lynne=lynne_kues@us.ibm.com",

			"maeschli=martin_aeschlimann@ch.ibm.com",
			"mfaraj=mfaraj@ca.ibm.com",
			"mvalenta=Michael_Valenta@ca.ibm.com",
			"mvanmeek=mvm@ca.ibm.com, michaelvanmeekeren@yahoo.ca",
			"mcq=Mike_Wilson@ca.ibm.com",
			"melder=mdelder@us.ibm.com",
			"mrennie=Michael_Rennie@ca.ibm.com",
			"mhatem=Matthew_Hatem@notesdev.ibm.com",
			"mvoronin=Mikhail.Voronin@intel.com",
"mkeller=markus_keller@ch.ibm.com",

			"nick=nick_edgar@ca.ibm.com",

			"oliviert=Olivier_Thomann@ca.ibm.com",

			"ptobias=tobias_widmer@ch.ibm.com", // not certain
			"prapicau=pascal_rapicault@ca.ibm.com",
			"pwebster=pwebster@ca.ibm.com",
			"pdubroy=Patrick_Dubroy@ca.ibm.com",

			"rchaves=rafael_chaves@ca.ibm.com",
			"rodrigo=rodrigo_peretti@ca.ibm.com",
			"rperetti=rodrigo_peretti@ca.ibm.com", // same as rodrigo?
			"randyg=Randy_Giffen@oti.com",

			"sdimitro=sonia_dimitrov@ca.ibm.com",
			"sdimitro2=sonia_dimitrov@ca.ibm.com", // same as sdimitro?
			"silenio=Silenio_Quarti@ca.ibm.com",
			"ssq=Silenio_Quarti@ca.ibm.com",
			"steve=steve_northover@ca.ibm.com",
			"sarsenau=simon_arsenault@ca.ibm.com",
			"ssarkar=sumit.sarkar@hp.com", "sxenos=sxenos@gmail.com",
			"sfranklin=susan_franklin@us.ibm.com", "schan=chanskw@ca.ibm.com",
			"skaegi=simon.kaegi@cognos.com",
			"semion=semion@il.ibm.com",

			"tod=Tod_Creasey@ca.ibm.com", "twatson=tjwatson@us.ibm.com",
			"torres=alexandre.torres@gmail.com",
			"tmaeder=t.s.maeder@hispeed.ch", "teicher=eclipse@tom.eicher.name",
			"twidmer=tobias_widmer@ch.ibm.com",

			"veronika=veronika_irvine@ca.ibm.com, veronika_irvine@ca.ibm.com", "vlad=vhirsl@hotmail.com",

			"wmelhem=wassimm@ca.ibm.com", "wharley=wharley@bea.com",

			"yamanaka=AtsuhikoYamanaka@hotmail.com", };

	public static void main(String[] args) {
		String[] files;
		files = Utils.getTestingSet(ECLIPSE_DIR, TESTING_MONTH);
		System.out.println(files.length);

		files = Utils.getTrainingSet(ECLIPSE_DIR, 1, LAST_TRAINING_MONTH);
		System.out.println(files.length);
		files = Utils.getTrainingSet(ECLIPSE_DIR, 3, LAST_TRAINING_MONTH);
		System.out.println(files.length);
		files = Utils.getTrainingSet(ECLIPSE_DIR, 6, LAST_TRAINING_MONTH);
		System.out.println(files.length);
		files = Utils.getTrainingSet(ECLIPSE_DIR, 8, LAST_TRAINING_MONTH);
		System.out.println(files.length);
		files = Utils.getTrainingSet(ECLIPSE_DIR, 12, LAST_TRAINING_MONTH);
		System.out.println(files.length);
	}
}
