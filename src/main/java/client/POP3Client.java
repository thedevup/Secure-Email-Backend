package client;

import javax.mail.*;

import conf.Settings;
import domain.Contact;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

// Retrieve email from a remote server
public class POP3Client {
	private Store store;

	public POP3Client(String email, String password) throws MessagingException {
		Properties properties = new Properties();
		// Check out settings
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.pop3.host", Settings.POP3_SERVER);
		properties.put("mail.pop3.port", Integer.toString(Settings.POP3_PORT));
		properties.put("mail.pop3.ssl.enable", "true");

		Session session = Session.getDefaultInstance(properties);
		store = session.getStore("pop3");
		store.connect(email, password);
	}
	
	// I have used domain.Message instead of Domain because there is a conflict
	// between that class and the one from javax.mail
	public List<domain.Message> listMessages() throws MessagingException {
		Folder inbox = store.getFolder("INBOX");
		inbox.open(Folder.READ_ONLY);

		javax.mail.Message[] messages = inbox.getMessages();
		List<domain.Message> messageList = new ArrayList<>();
		for (javax.mail.Message message : messages) {
			domain.Message customMessage = convertToCustomMessage(message);
			messageList.add(customMessage);
		}

		inbox.close(false);
		return messageList;
	}
	
	// This class have been used convert messages of type javax.mail.Message to our custom class
	private domain.Message convertToCustomMessage(javax.mail.Message message) throws MessagingException {
		try {
			
			// We only take into consideration the case where there is only one sender and one receiver
			Address fromAddress = message.getFrom()[0];
			Contact from = new Contact(fromAddress.toString());

			Address toAddress = message.getRecipients(Message.RecipientType.TO)[0];
			Contact to = new Contact(toAddress.toString());

			LocalDateTime dateTime = message.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

			String subject = message.getSubject();

			String content;

			// We want to only work with mails that contain plain text
			if (message.isMimeType("text/plain")) {
				content = (String) message.getContent();
			} else if (message.isMimeType("multipart/*")) {
				Multipart multipart = (Multipart) message.getContent();
				content = null;
				for (int i = 0; i < multipart.getCount(); i++) {
					BodyPart bodyPart = multipart.getBodyPart(i);
					if (bodyPart.isMimeType("text/plain")) {
						content = (String) bodyPart.getContent();
						break;
					}
				}
				if (content == null) {
					content = "Content not supported";
				}
			} else {
				// Otherwise the content of the message is as follows
				content = "Content not supported";
			}

			// Added to debug
			System.out.println("Subject:" + subject + "\nContent:" + content);
			
			return new domain.Message(from, to, dateTime, subject, content);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// Once a message is retrieved, we want to delete it from the external server
	public void deleteMessage(javax.mail.Message message) throws MessagingException {
	    Folder inbox = store.getFolder("INBOX");
	    inbox.open(Folder.READ_WRITE);

	    message.setFlag(Flags.Flag.DELETED, true);

	    inbox.close(true);
	}
	
	public Store getStore() {
	    return store;
	}

	public void close() throws MessagingException {
		store.close();
	}
}
