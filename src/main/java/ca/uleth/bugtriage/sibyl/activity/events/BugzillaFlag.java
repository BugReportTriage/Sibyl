package ca.uleth.bugtriage.sibyl.activity.events;

import java.io.Serializable;

public class BugzillaFlag implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AttachmentFlagStatus status;
	private AttachmentFlagState state;
	
	public BugzillaFlag() {}
	
	public BugzillaFlag(AttachmentFlagStatus status, AttachmentFlagState state) {
		this.status = status;
		this.state = state;
	}

	public AttachmentFlagState getState() {
		return this.state;
	}

	public AttachmentFlagStatus getStatus() {
		return this.status;
	}

	@Override
	public String toString() {
		return this.status.name() + "[" + (this.state.equals(AttachmentFlagState.UNKNOWN) ?  "" : this.state.name()) + "]";
	}
}