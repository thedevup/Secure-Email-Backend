package domain;

import cipher.ICipher;

public class User extends Contact {
	private static final long serialVersionUID = 8035979939319283701L;
	private String username;
	private String password;
	private MailBox mailbox;
	private ICipher key = null;
	
	public User(String firstName, String middleName, String familyName, String emailAddress, String username, String password) {
		super(firstName, middleName, familyName, emailAddress);
		if (username != null) {
			username = username.trim();
		}
		this.username = username;
		if (password != null) {
			password = password.trim();
		}
		this.password = password;
	}

	public User(String firstName, String middleName, String familyName, String emailAddress, String username) {
		this(firstName, middleName, familyName, username, emailAddress, null);
	}
	
	 public MailBox getMailBox() {
	        return mailbox;
	}

	public void setMailBox(MailBox mailbox) {
	        this.mailbox = mailbox;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public ICipher getkey() {
		return key;
	}

	public void setKey(ICipher key) throws Exception {
		this.key = key;
	}
}
