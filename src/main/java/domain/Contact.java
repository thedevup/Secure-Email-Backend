package domain;

import java.io.Serializable;
import java.util.Objects;

public class Contact implements Serializable, Comparable<Contact> {
	private static final long serialVersionUID = 5615140951263062343L;
	private String firstName;
	private String middleName;
	private String familyName;
	private String emailAddress;

	public Contact(String emailAddress) {
		this(null, null, null, emailAddress);
	}
	
	public Contact(String firstName, String middleName, String familyName, String emailAddress) {
		super();
		if (firstName != null) {
			this.firstName = firstName.trim();
		}
		if (middleName != null) {
			this.middleName = middleName.trim();
		}
		else {
			this.middleName = middleName;
		}
		if (familyName != null) {
			this.familyName = familyName.trim();
		}
		if (emailAddress != null) {
			this.emailAddress = emailAddress;
		}
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName.trim();
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName.trim();
	}

	public String getFamilyName() {
		return familyName;
	}

	public void setFamilyName(String familyName) {
		this.familyName = familyName.trim();
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress.trim();
	}

	public int compareTo(Contact o) {
		return this.emailAddress.compareTo(o.emailAddress);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(emailAddress);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		return Objects.equals(emailAddress, other.emailAddress);
	}

	@Override
	public String toString() {
		return ((firstName + " " + middleName + " ").trim()) + familyName + " (" + emailAddress + ")";
	}
}