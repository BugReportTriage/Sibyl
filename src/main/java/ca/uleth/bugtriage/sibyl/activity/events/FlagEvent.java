package ca.uleth.bugtriage.sibyl.activity.events;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FlagEvent extends BugActivityEvent {

	private static final long serialVersionUID = 3258693199936631348L;

	private static final Pattern id = Pattern.compile("\\d+");

	private int attachmentId;

	private List<BugzillaFlag> flags;

	public FlagEvent() {

	}

	public FlagEvent(int id, List<BugzillaFlag> flags) {
		this.what = BugActivityEvent.FLAG;
		this.attachmentId = id;
		this.flags = flags;
	}

	public String flagsString() {
		String flagString = "";
		for (BugzillaFlag flag : this.flags) {
			flagString += flag + " ";
		}
		return flagString;
	}

	public static int parseId(String attachment) {
		Matcher matcher = FlagEvent.id.matcher(attachment);
		if (matcher.find()) {
			return Integer.parseInt(matcher.group());
		}
		// Not an attachment flag
		return -1;
	}

	public static List<BugzillaFlag> parseFlags(String flag) {
		List<BugzillaFlag> flags = new ArrayList<BugzillaFlag>();
		AttachmentFlagStatus flagStatus = AttachmentFlagStatus.UNKNOWN;
		AttachmentFlagState flagState = AttachmentFlagState.UNKNOWN;

		String[] flagToken = flag.split(", ");
		for (int i = 0; i < flagToken.length; i++) {
			String token = flagToken[i];
			if (token.indexOf("(") != -1) {
				int end = token.indexOf("(");
				String substr = token.substring(0, end);
				token = substr;
			}

			/* Handle the case of the obsolete status 'needs-work' */

			// REVIEW
			if (token.toLowerCase().startsWith(AttachmentFlagStatus.REVIEW.label)) {
				flagStatus = AttachmentFlagStatus.REVIEW;
				// NEEDINFO
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.NEEDINFO.label)) {
				flagStatus = AttachmentFlagStatus.NEEDINFO;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.QE_VERIFY.label)) {
				flagStatus = AttachmentFlagStatus.QE_VERIFY;
			}else if (token.toLowerCase().startsWith(AttachmentFlagStatus.BACKLOG.label)) {
				flagStatus = AttachmentFlagStatus.BACKLOG;
			}else if (token.toLowerCase().startsWith(AttachmentFlagStatus.TEST_SUITE.label)) {
				flagStatus = AttachmentFlagStatus.TEST_SUITE;
			}
			
				/*
				
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.SUPERREVIEW.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.SUPERREVIEW;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.APPROVAL.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.APPROVAL;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.UI.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.UI;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.BRANCH.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.BRANCH;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.COMMITTED.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.COMMITTED;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.ACCEPTED.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.ACCEPTED;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.COMMENTED.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.COMMENTED;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.NONE.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.NONE;
			} else if (token.toLowerCase().startsWith(AttachmentFlagStatus.REJECTED.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.REJECTED;
			} else if (token.equals("1")
					|| token.toLowerCase().startsWith(AttachmentFlagStatus.OBSOLETE.name().toLowerCase())) {
				flagStatus = AttachmentFlagStatus.OBSOLETE;
			}
*/
			// Assure that flag was set to something meaningful
			if (flagStatus.equals(AttachmentFlagStatus.UNKNOWN) && token.equals("") == false) {
				System.err.println("WARNING: Attachment flag status unknown: " + token);
			}

			if (token.length() > 0) {
				if (token.charAt(token.length() - 1) == '?') {
					flagState = AttachmentFlagState.REQUESTED;
				} else if (token.charAt(token.length() - 1) == '+') {
					flagState = AttachmentFlagState.GRANTED;
				} else if (token.charAt(token.length() - 1) == '-') {
					flagState = AttachmentFlagState.DENIED;
				} /*
				else if (flagStatus.equals(AttachmentFlagStatus.OBSOLETE)
						|| flagStatus.equals(AttachmentFlagStatus.COMMITTED)
						|| flagStatus.equals(AttachmentFlagStatus.ACCEPTED)
						|| flagStatus.equals(AttachmentFlagStatus.COMMENTED)
						|| flagStatus.equals(AttachmentFlagStatus.NONE)
						|| flagStatus.equals(AttachmentFlagStatus.REJECTED)
						|| flagStatus.equals(AttachmentFlagStatus.REVIEW)) {
					flagState = AttachmentFlagState.OFF;
				}*/
			}
			// Assure that flag state was set to something meaningful
			if (flagState.equals(AttachmentFlagState.UNKNOWN) && token.equals("") == false) {
				System.err.println("WARNING: Attachment flag state unknown: " + token);
			}

			flags.add(new BugzillaFlag(flagStatus, flagState));
		}

		return flags;
	}

	@Override
	public String toString() {
		return this.getName() + " | " + this.getDate() + " | " + this.getWhat() + " | " + this.attachmentId + " | "
				+ this.flagsString();
	}

	public List<BugzillaFlag> getFlags() {
		return this.flags;
	}

	public int getAttachmentId() {
		return this.attachmentId;
	}

}
