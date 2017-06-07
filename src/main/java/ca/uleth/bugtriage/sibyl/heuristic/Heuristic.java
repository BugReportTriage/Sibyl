package ca.uleth.bugtriage.sibyl.heuristic;

public enum Heuristic {
	ECLIPSE(new EclipseHeuristic()), 
	MOZILLA(new MozillaHeuristic()),
	COMPONENT(new ComponentHeuristic()), SUBCOMPONENT(new SubcomponentHeuristic()),
	KDE_PLASMA(null), LIBREOFFICE(null)
	;
	

	private final HeuristicClassifier classifier;

	private Heuristic(HeuristicClassifier classifier) {
		this.classifier = classifier;
	}

	public HeuristicClassifier getClassifier() {
		return this.classifier;
	}
}