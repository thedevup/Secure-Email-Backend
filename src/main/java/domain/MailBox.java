package domain;

import java.util.ArrayList;
import java.util.List;

public class MailBox {
	private User owner;
	private List<Message> messages;

	public MailBox(User owner) {
		this.owner = owner;
		this.messages = new ArrayList<>();
	}

	public User getOwner() {
		return owner;
	}

	public void addMessage(Message message) {
		messages.add(message);
	}

	public List<Message> getMessages() {
		return messages;
	}
}
