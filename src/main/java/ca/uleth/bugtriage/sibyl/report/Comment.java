package ca.uleth.bugtriage.sibyl.report;

public class Comment {

	private String text;
	private String authour;
	private String created;

	public String getText() {		
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getAuthour() {
		return authour;
	}

	public void setAuthour(String authour) {
		this.authour = authour;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

}
