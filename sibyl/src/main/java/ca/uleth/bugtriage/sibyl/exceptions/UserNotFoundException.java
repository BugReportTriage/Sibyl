package ca.uleth.bugtriage.sibyl.exceptions;

public class UserNotFoundException extends Exception {

	/**
	 * Generated id
	 */
	private static final long serialVersionUID = 8615627008628288122L;
	private String userId;
	
	public UserNotFoundException(String userId){
		this.userId = userId;
	}

	@Override
	public String getMessage() {
		return "User id: " + this.userId;
	}
	
	

}
