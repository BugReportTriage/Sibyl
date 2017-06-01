package ca.uleth.bugtriage.sibyl.sibyl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import ca.uleth.bugtriage.sibyl.Project;
import ca.uleth.bugtriage.sibyl.User;
import ca.uleth.bugtriage.sibyl.exceptions.PasswordNotFoundException;
import ca.uleth.bugtriage.sibyl.exceptions.UserNotFoundException;
import ca.uleth.bugtriage.sibyl.servlet.webpage.AccountSetupForm;
import ca.uleth.bugtriage.sibyl.utils.Environment;
import ca.uleth.bugtriage.sibyl.utils.Messages;
import ca.uleth.bugtriage.sibyl.utils.Utils;

public class SibylUser extends User {

	public static final String USER_DIR = "users/";

	private static final String USER_ID_FILE = Environment.getServletDataDir()
			+ USER_DIR + "userId";

	public static final String USER_ID_KEY = "userId";

	private static final int INITIAL_ID = 1;

	private static final String PASSWORD_FILE = "password";

	private static final String SIBYL_PASSWORD_FILE = "sibylPassword";
	
	private static final String BUG_LIST_COOKIE_FILE = "buglist.cookie";
	
	private static final String LAST_ORDER_COOKIE_FILE = "lastorder.cookie";

	private static final SibylUser UNKNOWN_SIBYL_USER = new SibylUser(User.UNKNOWN_USER_ID);

	public static final String SURVEY_FILE = "survey";

	private final String userDataDir;

	public SibylUser(String id) {
		this(id, UNKNOWN_USER_NAME, UNKNOWN_USER_PASSWORD, Project.UNKNOWN);
	}

	public SibylUser(String id, String name, String password, Project project) {
		super(id, name, password, project);
		this.userDataDir = Environment.getServletDataDir() + USER_DIR + this.id
		+ "/";
	}

	public void createDataDir() {
		// Create user directory if needed
		File file = new File(this.userDataDir);
		if (file.exists() == false) {
			file.mkdirs();
		}
	}

	public static SibylUser getUser(String userId, Messages msgs)
			throws UserNotFoundException, PasswordNotFoundException {
		try {
			SibylUser tempUser = new SibylUser(userId);
			String userFile = tempUser.getUserDataDir() + Sibyl.ACCOUNT_FILE;
			// System.out.println("User file: " + userFile);
			BufferedReader reader = new BufferedReader(new FileReader(userFile));
			String name = UNKNOWN_USER_NAME, password = UNKNOWN_USER_PASSWORD;
			Project project = Project.UNKNOWN;
			for (String line = reader.readLine(); line != null; line = reader
					.readLine()) {
				String[] splitLine = line.split(":");
				if (splitLine.length == 2) {
					String key = splitLine[0];
					String value = splitLine[1];

					if (key.equals(AccountSetupForm.USERNAME)) {
						name = value;
					}
					if (key.equals(AccountSetupForm.PASSWORD)) {
						msgs.add("Password should not be in the account file!");
					}
					if (key.equals(Project.PROJECT_ID_TAG)) {
						String product = value;
						project = Project.getProject(product);
					}
				}
			}

			password = Utils.getPassword(tempUser.getPasswordFile());
			return new SibylUser(userId, name, password, project);
		} catch (FileNotFoundException e) {
			throw new UserNotFoundException(userId);
		} catch (IOException e) {
			msgs.add(e.getMessage());
		}
		return UNKNOWN_SIBYL_USER;
	}

	public int getChangeCount() {
		String countFile = getUserDataDir() + Sibyl.ASSIGNMENT_COUNT_FILE;
		try {
			try {
				// Get change count
				BufferedReader reader = new BufferedReader(new FileReader(
						countFile));
				int count = Integer.parseInt(reader.readLine());
				reader.close();
				return count;
			} catch (FileNotFoundException e) {
				// Create the file
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						countFile));
				writer.write(String.valueOf(1));
				writer.close();
			}
		} catch (FileNotFoundException e) {
			System.err.println("Couldn't create count file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public void updateChangeCount() {

		try {
			int count = getChangeCount();
			// Write new id
			BufferedWriter writer = new BufferedWriter(new FileWriter(
					getUserDataDir() + Sibyl.ASSIGNMENT_COUNT_FILE));
			count++;
			writer.write(String.valueOf(count));
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static synchronized String getNewId(Messages msgs) {
		try {
			try {
				// Get id
				BufferedReader reader = new BufferedReader(new FileReader(
						USER_ID_FILE));
				String idStr = reader.readLine();
				int id = Integer.parseInt(idStr);
				// System.out.println("user id: " + id);
				reader.close();

				// Write new id
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						USER_ID_FILE));
				int nextId = id + 1;
				System.out.println("New user id issued: " + nextId);
				writer.write(String.valueOf(nextId));
				writer.close();

				return String.valueOf(id);
			} catch (FileNotFoundException e) {
				// Create the file
				BufferedWriter writer = new BufferedWriter(new FileWriter(
						USER_ID_FILE));
				writer.write(INITIAL_ID);
				writer.close();
				return String.valueOf(INITIAL_ID);
			}
		} catch (IOException e) {
			msgs.add(e.getMessage());
		}
		return UNKNOWN_USER_ID;
	}
	
	public String getPasswordFile() {
		return this.getUserDataDir() + PASSWORD_FILE;
	}

	public String getSibylPasswordFile() {
		return this.getUserDataDir() + SIBYL_PASSWORD_FILE;
	}
	
	public String getUserDataDir() {
		return this.userDataDir;
	}
	
	public String getBuglist() {
		return this.readFile(this.getUserDataDir() + "/" + BUG_LIST_COOKIE_FILE).trim(); //"BUGLIST=19%3A20%3A27; path=/; expires=Fri, 01-Jan-2038 00:00:00 GMT";
	}
	
	public String getLastOrder() {
		//return "LASTORDER=bugs.bug_id; path=/; expires=Fri, 01-Jan-2038 00:00:00 GMT";
		return this.readFile(this.getUserDataDir() + "/" + LAST_ORDER_COOKIE_FILE).trim(); 
	}
	
	public void saveBuglist(String cookie) {
		writeToFile(this.getUserDataDir() + "/" + BUG_LIST_COOKIE_FILE, cookie);
	}

	public void saveLastOrder(String cookie) {
		writeToFile(this.getUserDataDir() + "/" + LAST_ORDER_COOKIE_FILE, cookie);
		
	}
	
	private void writeToFile(String filename, String content){
		try {
			FileWriter writer = new FileWriter(filename);
			writer.append(content);
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String readFile(String filename){
		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			for (String line = new String(); line != null; line = reader.readLine()) {
				sb.append(line + "\n");
			}
			reader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		SibylUser.getNewId(new Messages());
		try {
			User user = SibylUser.getUser("1", new Messages());
			System.out.println(user.getName());
			System.out.println(user.getPassword());
		} catch (UserNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PasswordNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	
}
