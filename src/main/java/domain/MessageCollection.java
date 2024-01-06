package domain;

import java.time.LocalDateTime;
import java.util.Map;

// Used to ensure consistency in a multithreaded env
import java.util.concurrent.ConcurrentSkipListMap;

// Used the singleton design pattern
// The same instance of of MessageCollection is used throughout the whole app
public class MessageCollection {
	private static MessageCollection instance;
	private Map<String, Message> messages;

	private MessageCollection() {
		// - ConcurrentSkipListMap is used here to ensure thread safety.
		// - ConcurrentSkipListMap ensures that multiple threads can access and modify
		// the map concurrently without blocking each other
		messages = new ConcurrentSkipListMap<>();
	}

	public static MessageCollection getInstance() {
		if (instance == null) {
			instance = new MessageCollection();
		}
		return instance;
	}

	public void addMessage(Contact to, Contact from, LocalDateTime received, Message message) {
		String key = generateKey(to, from, received);
		messages.put(key, message);
	}

	public Message getMessage(Contact to, Contact from, LocalDateTime received) {
		String key = generateKey(to, from, received);
		return messages.get(key);
	}

	private String generateKey(Contact to, Contact from, LocalDateTime received) {
		return to.getEmailAddress() + "-" +from.getEmailAddress()+"-" + received.toString();
	}
}
