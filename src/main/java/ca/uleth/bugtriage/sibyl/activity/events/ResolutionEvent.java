package ca.uleth.bugtriage.sibyl.activity.events;

public class ResolutionEvent extends BugActivityEvent {

	private static final long serialVersionUID = 3258693199936631348L;

	private final ResolutionType type;

	public ResolutionEvent(ResolutionType type) {
		this.what = BugActivityEvent.RESOLUTION;
		this.type = type;
	}

	public ResolutionType getType() {
		return this.type;
	}

	public String getResolvedBy() {
		return this.getName();
	}

	@Override
	public String toString() {
		return this.getName() + " | " + this.getDate() + " | " + this.getWhat()
				+ " | " + this.getRemoved() + " | " + (this.getType().equals(ResolutionType.UNKNOWN) ? "" : this.getType());
	}

}
