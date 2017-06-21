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
package ca.uleth.bugtriage.sibyl.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.uleth.bugtriage.sibyl.activity.events.AssignmentEvent;
import ca.uleth.bugtriage.sibyl.activity.events.FlagEvent;
import ca.uleth.bugtriage.sibyl.activity.events.BugzillaFlag;
import ca.uleth.bugtriage.sibyl.activity.events.AttachmentFlagState;
import ca.uleth.bugtriage.sibyl.activity.events.AttachmentFlagStatus;
import ca.uleth.bugtriage.sibyl.activity.events.BugActivityEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionEvent;
import ca.uleth.bugtriage.sibyl.activity.events.ResolutionType;
import ca.uleth.bugtriage.sibyl.activity.events.StatusEvent;
import ca.uleth.bugtriage.sibyl.report.BugReport;
import ca.uleth.bugtriage.sibyl.utils.Email;
import ca.uleth.bugtriage.sibyl.utils.FrequencyTable;

public class BugActivity implements Iterable<BugActivityEvent>, Serializable {

	private static final long serialVersionUID = 3258693199936631348L;

	@JsonProperty
	private final List<StatusEvent> statusEvents;
	
	@JsonProperty
	private final List<ResolutionEvent> resolutionEvents;
	
	@JsonProperty
	private final List<AssignmentEvent> assignmentEvents;
	
	@JsonProperty
	private final List<BugActivityEvent> otherEvents;
	
	@JsonProperty
	private final List<FlagEvent> flagEvents;	

	public BugActivity() {
		this.statusEvents = new ArrayList<StatusEvent>();
		this.resolutionEvents = new ArrayList<ResolutionEvent>();
		this.assignmentEvents = new ArrayList<AssignmentEvent>();
		this.flagEvents = new ArrayList<FlagEvent>();
		this.otherEvents = new ArrayList<BugActivityEvent>();
	}

	public void addEvent(BugActivityEvent event) {
		if (event instanceof StatusEvent) {
			this.statusEvents.add((StatusEvent) event);
			return;
		}

		if (event instanceof ResolutionEvent) {
			this.resolutionEvents.add((ResolutionEvent) event);
			return;
		}

		if (event instanceof AssignmentEvent) {
			this.assignmentEvents.add((AssignmentEvent) event);
			return;
		}

		if (event instanceof FlagEvent) {
			this.flagEvents.add((FlagEvent) event);
			return;
		}

		this.otherEvents.add(event);
	}

	private List<BugActivityEvent> getEvents() {
		List<BugActivityEvent> events = new ArrayList<BugActivityEvent>();
		events.addAll(this.statusEvents);
		events.addAll(this.resolutionEvents);
		events.addAll(this.assignmentEvents);
		events.addAll(this.flagEvents);
		events.addAll(this.otherEvents);
		Collections.sort(events);
		return events;
	}

	public Iterator<BugActivityEvent> iterator() {
		return getEvents().iterator();
	}

	public int size() {
		return this.otherEvents.size() + this.statusEvents.size()
				+ this.resolutionEvents.size() + this.assignmentEvents.size();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		for (Object event : this) {
			sb.append(event);
			sb.append("\n");
		}
		return sb.toString();
	}

	/**
	 * Get a list of all the people that this bug has been assigned to
	 */
	public List<String> allAssignedTo() {
		List<String> assignedTo = new ArrayList<String>();
		for (AssignmentEvent event : this.assignmentEvents) {
			assignedTo.add(event.getAssigned());
		}
		return assignedTo;
	}

	/**
	 * 
	 * Get the name of the person who resolved the report
	 * 
	 * @return the name of the person who resolved the bug if resolved, null
	 *         otherwise
	 */
	public ResolutionEvent resolution() {

		if (this.resolutionEvents.size() > 0) {
			return this.resolutionEvents.get(this.resolutionEvents.size() - 1);
		}
		return null;
	}

	/**
	 * 
	 * Get the name of the person who last updated the status of the report
	 * 
	 * @return the name of the person who last set the status of the report,
	 *         null otherwise
	 */
	public String whoSetStatus() {

		if (this.statusEvents.size() > 0) {
			return this.statusEvents.get(this.statusEvents.size() - 1)
					.getName();
		}
		return null;

	}

	/**
	 * Get the name of the last person that this report was assigned to
	 * 
	 * @return name of the last person the report was assigned to, or null
	 *         otherwise
	 */
	public String lastAssignedTo() {
		if (this.assignmentEvents.size() > 0) {
			return this.assignmentEvents.get(this.assignmentEvents.size() - 1)
					.getAssigned();
		}
		return null;
	}

	public List<FlagEvent> approvedAttachments() {
		List<FlagEvent> approvalEvents = new ArrayList<FlagEvent>();

		/* Case 1: Attachement gets approved */
		for (FlagEvent event : this.flagEvents) {
			List<BugzillaFlag> flags = event.getFlags();
			for (BugzillaFlag flag : flags) {
				boolean approvalGranted = flag.getStatus().equals(
						AttachmentFlagStatus.REVIEW)
						&& flag.getState().equals(AttachmentFlagState.GRANTED);
				if (approvalGranted) {
					approvalEvents.add(event);
				}
			}
		}

		if (approvalEvents.isEmpty()) {
			/* Case 2: Attachment only gets superreview granted */
			for (FlagEvent event : this.flagEvents) {
				List<BugzillaFlag> flags = event.getFlags();
				for (BugzillaFlag flag : flags) {
					/*
					boolean superReviewGranted = flag.getStatus().equals(
							AttachmentFlagStatus.SUPERREVIEW)
							&& flag.getState().equals(
									AttachmentFlagState.GRANTED);
					if (superReviewGranted) {
						approvalEvents.add(event);
					}
					*/
				}
			}
		}

		return approvalEvents;
	}

	public Set<String> attachmentSubmitters(
			List<FlagEvent> approvalEvents) {
		Set<String> reviewEventNames = new HashSet<String>();

		// Case 1: Attachment was not submitted by a reviewer
		for (FlagEvent approvalEvent : approvalEvents) {
			for (FlagEvent event : this.flagEvents) {
				if (event.getAttachmentId() == approvalEvent.getAttachmentId()) {
					List<BugzillaFlag> flags = event.getFlags();
					for (BugzillaFlag flag : flags) {
						boolean reviewRequested = flag.getStatus().equals(
								AttachmentFlagStatus.REVIEW)
								&& flag.getState().equals(
										AttachmentFlagState.REQUESTED);
						if (reviewRequested) {
							reviewEventNames.add(event.getName());
						}
					}
				}
			}
		}

		if (reviewEventNames.isEmpty()) {
			// Case 2: Attachment was submitted and reviewed by a reviewer
			// (see Mozilla bug 95520, Mozilla bug 228968, Mozilla bug 290494)
			for (FlagEvent approvalEvent : approvalEvents) {
				for (FlagEvent event : this.flagEvents) {
					if (event.getAttachmentId() == approvalEvent
							.getAttachmentId()) {
						List<BugzillaFlag> flags = event.getFlags();
						for (BugzillaFlag flag : flags) {
							boolean reviewGranted = flag.getStatus().equals(
									AttachmentFlagStatus.REVIEW)
									&& flag.getState().equals(
											AttachmentFlagState.GRANTED);
							/*boolean superReviewRequested = flag.getStatus()
									.equals(AttachmentFlagStatus.SUPERREVIEW)
									&& flag.getState().equals(
											AttachmentFlagState.REQUESTED);
							if (reviewGranted || superReviewRequested) {
								reviewEventNames.add(event.getName());
							}*/
						}
					}
				}
			}
		}

		if (reviewEventNames.isEmpty()) {
			// Case 3: Minor fix after superreview
			// (see Mozilla bug 290494)
			for (FlagEvent approvalEvent : approvalEvents) {
				for (FlagEvent event : this.flagEvents) {
					if (event.getAttachmentId() == approvalEvent
							.getAttachmentId()) {
						List<BugzillaFlag> flags = event.getFlags();
						for (BugzillaFlag flag : flags) {
							/*
							boolean approvalRequested = flag.getStatus()
									.equals(AttachmentFlagStatus.APPROVAL)
									&& flag.getState().equals(
											AttachmentFlagState.REQUESTED);
							if (approvalRequested) {
								reviewEventNames.add(event.getName());
							}*/
						}
					}
				}
			}
		}

		return reviewEventNames;
	}

	public String mostFrequentAttachmentSubmitter() {
		// Map<String, Integer> attachmentSubmitters = new HashMap<String,
		// Integer>();
		FrequencyTable attachmentSubmitters = new FrequencyTable();
		for (FlagEvent event : this.flagEvents) {
			String submitter = event.getName();
			for (BugzillaFlag attachmentFlag : event.getFlags()) {
				/*
				boolean review = attachmentFlag.getStatus().equals(
						AttachmentFlagStatus.REVIEW)
						|| attachmentFlag.getStatus().equals(
								AttachmentFlagStatus.SUPERREVIEW);
				if (review
						&& attachmentFlag.getState().equals(
								AttachmentFlagState.REQUESTED)) {
					attachmentSubmitters.add(submitter);
				}
				*/
			}
		}

		List<String> mostFrequentSubmitters = new ArrayList<String>();
		int numAttachments = 0;
		for (String name : attachmentSubmitters.getKeys()) {
			if (attachmentSubmitters.getFrequency(name) == numAttachments) {
				mostFrequentSubmitters.add(name);
			}

			if (attachmentSubmitters.getFrequency(name) > numAttachments) {
				mostFrequentSubmitters.clear();
				mostFrequentSubmitters.add(name);
				numAttachments = attachmentSubmitters.getFrequency(name);
			}

		}

		if (mostFrequentSubmitters.size() == 1) {
			return mostFrequentSubmitters.get(0);
		}

		/*
		 * If two (or more) submitted the same number, go with the one assigned
		 * to the bug otherwise go with the one with the latest submission
		 */
		String lastAssigned = this.lastAssignedTo();
		if (lastAssigned != null) {
			if (mostFrequentSubmitters.contains(Email.getAddress(lastAssigned))) {
				return lastAssigned;
			}
		}

		Collections.sort(this.flagEvents);
		for (int index = this.flagEvents.size() - 1; index >= 0; index--) {
			FlagEvent event = this.flagEvents.get(index);
			for (BugzillaFlag attachmentFlag : event.getFlags()) {
				if (attachmentFlag.getStatus().equals(
						AttachmentFlagStatus.REVIEW)
						&& attachmentFlag.getState().equals(
								AttachmentFlagState.REQUESTED)) {
					return event.getName();
				}
			}
		}

		// Should never get here
		return "";
	}

	public List<AssignmentEvent> getAssignmentEvents() {
		return this.assignmentEvents;
	}

	public List<String> componentChanges() {
		List<String> componentsChanges = new ArrayList<String>();
		for (BugActivityEvent event : this.otherEvents) {
			if (event.getWhat().equals("Component")) {
				boolean dontCount = false;
				// Check if the Product also changed to something other than
				// 'Platform' (see Eclipse 161846
				List<BugActivityEvent> alsoChangedEvents = new ArrayList<BugActivityEvent>();
				for (BugActivityEvent otherEvent : this.otherEvents) {
					if (otherEvent.getDate().equals(event.getDate())) {
						alsoChangedEvents.add(otherEvent);
					}
				}
				for (BugActivityEvent alsoChanged : alsoChangedEvents) {
					if (alsoChanged.getWhat().equals("Product")) {
						if (alsoChanged.getRemoved().equals("Platform")) {
							dontCount = true;
						}
					}
				}
				if (dontCount == false) {
					componentsChanges.add(event.getAdded());
				}
			}
		}
		return componentsChanges;
	}

	public List<String> subcomponentChanges() {
		List<String> subcomponentsChanges = new ArrayList<String>();
		for (BugActivityEvent event : this.otherEvents) {
			if (event.getWhat().equals("Summary")) {
				Matcher matcher = BugReport.SUBCOMPONENT_REGEX
						.matcher(event.getAdded());
				if (matcher.find()) {
					subcomponentsChanges.add(matcher.group(1));
				}
			}
		}
		return subcomponentsChanges;
	}

	/**
	 * Get a list of all the people that have marked this bug FIXED
	 */
	public List<String> fixers() {
		List<String> fixers = new ArrayList<String>();
		for (ResolutionEvent event : this.resolutionEvents) {
			if (event.getType().equals(ResolutionType.FIXED)) {
				fixers.add(event.getName());
			}
		}
		return fixers;
	}

	/**
	 * Get a list of all the people that have marked this bug FIXED
	 */
	public List<String> resolvers() {
		List<String> resolvers = new ArrayList<String>();
		for (ResolutionEvent event : this.resolutionEvents) {
			resolvers.add(event.getName());
		}
		return resolvers;
	}

	public List<String> ccAdded() {
		List<String> ccAdded = new ArrayList<String>();
		for (BugActivityEvent event : this.otherEvents) {
			if (event.getWhat().equals("cc") && event.getRemoved().equals("")) {
				String[] ccs = event.getAdded().split(",");
				for (int i = 0; i < ccs.length; i++)
					ccAdded.add(ccs[i].trim());
			}
		}
		return ccAdded;
	}
}
