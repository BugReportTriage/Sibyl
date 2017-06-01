package ca.uleth.bugtriage.sibyl.activity.events;

public enum ResolutionType {
	
	FIXED, DUPLICATE, WONTFIX, INVALID, WORKSFORME, REOPENED, LATER, 
	REMIND, MOVED, UNKNOWN, NOTABUG, NOTGNOME, INCOMPLETE, OBSOLETE, EXPIRED, 
	NOTXIMIAN, NEXTRELEASE, ERRATA, RAWHIDE, UPSTREAM, CANTFIX, CURRENTRELEASE, INSUFFICIENT_DATA, DEFERRED;

	public static ResolutionType convert(String change) {
		if (change.equals("FIXED")) {
			return ResolutionType.FIXED;
		}
		if (change.contains("DUPLICATE")) {
			return ResolutionType.DUPLICATE;
		}
		if (change.equals("INVALID")) {
			return ResolutionType.INVALID;
		}
		if (change.contains("LATER")) {
			return ResolutionType.LATER;
		}
		if (change.equals("WORKSFORME")) {
			return ResolutionType.WORKSFORME;
		}
		if (change.equals("REOPENED")) {
			return ResolutionType.REOPENED;
		}
		if (change.equals("WONTFIX")) {
			return ResolutionType.WONTFIX;
		}
		if (change.contains("REMIND")) {
			return ResolutionType.REMIND;
		}
		if (change.equals("MOVED")) {
			return ResolutionType.MOVED;
		}
		if (change.equals("EXPIRED")) {
			return ResolutionType.EXPIRED;
		}
		if (change.equals("NOTABUG")) { // Gnome
			return ResolutionType.NOTABUG;
		}
		if (change.equals("NOTGNOME")) { // Gnome
			return ResolutionType.NOTGNOME;
		}
		if (change.equals("INCOMPLETE")) { // Gnome
			return ResolutionType.INCOMPLETE;
		}
		if (change.equals("OBSOLETE")) { // Gnome
			return ResolutionType.OBSOLETE;
		}
		if(change.equals("NOTXIMIAN")){ // Gnome
			return ResolutionType.NOTXIMIAN;
		}
		if(change.equals("NEXTRELEASE")){ // Redhat
			return ResolutionType.NEXTRELEASE;
		}
		if(change.equals("ERRATA")){// Redhat
			return ResolutionType.ERRATA;
		}
		if(change.equals("RAWHIDE")){// Redhat
			return ResolutionType.RAWHIDE;
		}
		if(change.equals("UPSTREAM")){// Redhat
			return ResolutionType.UPSTREAM;
		}
		if(change.equals("CANTFIX")){// Redhat
			return ResolutionType.CANTFIX;
		}
		if(change.equals("CURRENTRELEASE")){// Redhat
			return ResolutionType.CURRENTRELEASE;
		}
		if(change.equals("INSUFFICIENT_DATA")){// Redhat
			return ResolutionType.INSUFFICIENT_DATA;
		}
		if(change.equals("DEFERRED")){// Redhat
			return ResolutionType.DEFERRED;
		}
		if (change.equals("") == false) {
			System.err.println("Unknown resolution type: " + change);
		}
		return ResolutionType.UNKNOWN;
	}
}
