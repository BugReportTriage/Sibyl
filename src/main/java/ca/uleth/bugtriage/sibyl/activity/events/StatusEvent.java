package ca.uleth.bugtriage.sibyl.activity.events;

public class StatusEvent extends BugActivityEvent {

	private static final long serialVersionUID = 3258693199936631348L;

	private StatusType type;

	public StatusEvent() {
		this.what = BugActivityEvent.STATUS;
	}

	public void setType(StatusType type) {
		this.type = type;
	}

	public StatusType getType() {
		return this.type;
	}

	@Override
	public String toString() {
		return this.getName() + " | " + this.getDate() + " | " + this.getWhat() + " | " + this.getRemoved() + " | "
				+ this.getType();
	}
}
