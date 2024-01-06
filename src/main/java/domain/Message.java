package domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import cipher.ICipher;

public class Message implements Serializable, Comparable<Message> {
	private static final long serialVersionUID = 6526982049872068590L;
	private Contact from;
	private Contact to;
	private final LocalDateTime dateTime;
	private final String subject;
	private String content;
	private ICipher key = null;
	
	public Message(Contact from, Contact to, LocalDateTime dateTime, String subject, String content) {
		super();
		this.from = from;
		this.to = to;
		this.dateTime = dateTime;
		this.content = content;
		this.subject = subject;
	}

	public Contact getFrom() {
		return from;
	}

	public void setFrom(Contact from) {
		this.from = from;
	}

	public Contact getTo() {
		return to;
	}

	public void setTo(Contact to) {
		this.to = to;
	}

	public String getContent() throws Exception {
		if (key == null) {
			return content;
		}
		else {
			return key.decrypt(content);
		}
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String getSubject() {
		return subject;
	}

	public ICipher getkey() {
		return key;
	}

	public void setKey(ICipher key) throws Exception {
		this.key = key;
	}

	public int compareTo(Message o) {
		if (this.dateTime.equals(o.dateTime)) {
			return from.compareTo(o.from);
		}
		else {
			return this.dateTime.compareTo(o.dateTime);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 10001;
		int result = 1;
		result = prime * result + ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Email from " + from.getEmailAddress() + " to " + to.getEmailAddress() + " sent on "+ dateTime;
	}
}

