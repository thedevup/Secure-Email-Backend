package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

public class MessageCollectionTest {

	private MessageCollection messageCollection;
	private Contact to;
	private Contact from;
	private LocalDateTime received;
	private Message message;

	// Message
	// String firstName, String middleName, String familyName, String emailAddress
	
	@BeforeEach
	public void setUp() {
		messageCollection = MessageCollection.getInstance();
		to = new Contact("Nour", null, "Aarab", "Nour@gmail,com");
		from = new Contact("Adam", "Something", "Aarab", "Adam@gmail.com");
		received = LocalDateTime.now();
		message = new Message(from, to, null, "some subject", "some random funny email");
	}

	@Test
	public void testGetInstance() {
		assertEquals(messageCollection, MessageCollection.getInstance());
	}

	@Test
	public void testAddMessage() {
		messageCollection.addMessage(to, from, received, message);
		Message retrievedMessage = messageCollection.getMessage(to, from, received);
		assertEquals(message, retrievedMessage);
	}

	@Test
	public void testGetMessage() {
		messageCollection.addMessage(to, from, received, message);
		Message retrievedMessage = messageCollection.getMessage(to, from, received);
		assertEquals(message, retrievedMessage);
	}
}
