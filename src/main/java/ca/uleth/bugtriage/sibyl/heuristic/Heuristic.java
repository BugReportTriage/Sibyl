package ca.uleth.bugtriage.sibyl.heuristic;

public enum Heuristic {
	//ECLIPSE(new EclipseHeuristic()), 
	MOZILLA(new MozillaHeuristic()),
	//GCC(new GCCHeuristic()), 
	//EVOLUTION(new EvolutionHeuristicOptimized()), 
	//COMPONENT(new ComponentHeuristic()), SUBCOMPONENT(new SubcomponentHeuristic()),
	//TEST(new TestHeuristic()), ECLIPSE_FIXED(new EclipseFixedHeuristic()), ANT(null), MYLAR(new MylarHeuristic()), WONTFIX(new WontFixHeuristic())
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