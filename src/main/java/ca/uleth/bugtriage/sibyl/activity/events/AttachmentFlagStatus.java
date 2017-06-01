package ca.uleth.bugtriage.sibyl.activity.events;

public enum AttachmentFlagStatus {
	REVIEW /* Firefox */, 
	SUPERREVIEW /* Firefox */, 
	APPROVAL /* Firefox */, 
	UI /* Firefox */, 
	BRANCH /* Firefox */, 
	OBSOLETE, 
	UNKNOWN, 
	COMMITTED /* Gnome */, 
	ACCEPTED /* Gnome accepted-committ_now*/, 
	COMMENTED /* Gnome */, 
	NONE /* Gnome */, 
	REJECTED/* Gnome */ ;
}
