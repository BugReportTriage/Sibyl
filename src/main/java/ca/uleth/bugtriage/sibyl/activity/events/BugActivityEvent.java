/*******************************************************************************
 * Copyright (c) 2003 University Of British Columbia and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     University Of British Columbia - initial API and implementation
 *******************************************************************************/
package ca.uleth.bugtriage.sibyl.activity.events;

import java.io.Serializable;

public class BugActivityEvent implements Comparable<BugActivityEvent>,
		Serializable {

	private static final long serialVersionUID = 3258693199936631348L;

	protected final static String STATUS = "status";

	protected final static String RESOLUTION = "resolution";

	protected final static String ASSIGNMENT = "AssignedTo";

	protected final static String FLAG = "flagtypes.name";

	protected String name;

	protected String date;

	protected String what;

	protected String removed;

	protected String added;

	protected BugActivityEvent(){
		this.added = "";
	}
	
	public static BugActivityEvent createEvent(String type, String change) {
		BugActivityEvent event = new BugActivityEvent();

		if (STATUS.equals(type)) {
			StatusEvent e = new StatusEvent();
			e.setType(StatusType.convert(change));
			return e;
		}

		if (RESOLUTION.equals(type)) {
			ResolutionEvent e = new ResolutionEvent();
			e.setType(ResolutionType.convert(change));
			return e;
		}

		if (ASSIGNMENT.equals(type)) {
			return new AssignmentEvent(change);
		}

		if (type.contains(FLAG)) {
			return new FlagEvent(FlagEvent.parseId(type),
					FlagEvent.parseFlags(change));
		}

		event.setWhat(type);
		event.setAdded(change);
		return event;
	}

	private void setAdded(String added) {
		this.added = added;
	}

	public void setRemoved(String removed) {
		this.removed = removed;
	}

	private void setWhat(String what) {
		this.what = what;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdded() {
		return this.added;
	}

	public String getRemoved() {
		return this.removed;
	}

	public String getWhat() {
		return this.what;
	}

	public String getDate() {
		return this.date;
	}

	public String getName() {
		return this.name;
	}

	@Override
	public String toString() {
		return this.name + " | " + this.date + " | " + this.what + " | " + this.removed + " | "
				+ this.added;
	}

	public int compareTo(BugActivityEvent o) {
		return this.date.compareTo(o.getDate());
	}
}
