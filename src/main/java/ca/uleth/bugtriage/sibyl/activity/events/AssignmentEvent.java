package ca.uleth.bugtriage.sibyl.activity.events;

public class AssignmentEvent extends BugActivityEvent {

	private static final long serialVersionUID = 3258693199936631348L;
	
	private final String assigned;
	
	public AssignmentEvent(String change){
		this.what = BugActivityEvent.ASSIGNMENT;
		this.assigned = change;
		this.added = change;
	}

	public String getAssigned() {
		return this.assigned;
	}
	
	@Override
	public String toString() {
		return this.getName() + " | " + this.getDate() + " | " + this.getWhat() + " | " + this.getRemoved() + " | "
				+ this.getAssigned();
	}
}