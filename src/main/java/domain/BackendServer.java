package domain;

import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.mail.Folder;
import javax.mail.MessagingException;

import cipher.CaesarCipher;
import cipher.ICipher;
import client.POP3Client;
import client.SMTPClient;
import conf.Settings;

public class BackendServer {
	// ConcurrentHashMap is used here to ensure thread safety
	private ConcurrentHashMap<String, Message> incomingMessages;
	private ConcurrentHashMap<String, Message> processedMessages;
	private ConcurrentHashMap<String, Message> outgoingMessages;
	private POP3Client pop3Client;

	public BackendServer(String email, String password) throws MessagingException {
		incomingMessages = new ConcurrentHashMap<>();
		processedMessages = new ConcurrentHashMap<>();
		outgoingMessages = new ConcurrentHashMap<>();
		pop3Client = new POP3Client(email, password);
	}

	public void storeMessagesToFile() {
		try (FileWriter writer = new FileWriter(Settings.WORDS_FILE + "\\messages.txt")) {
			for (Message message : incomingMessages.values()) {
				writer.write(message.toString() + "\n");
			}
			for (Message message : processedMessages.values()) {
				writer.write(message.toString() + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void retrieveMessagesFromPOP3() {
	    try {
	        List<Message> messages = pop3Client.listMessages();
	        
	        // Retrieve messages and add them to the incomingMessages map
	        for (Message message : messages) {
	            String key = message.getFrom().getEmailAddress() + "-" + message.getTo().getEmailAddress() + "-" + message.getDateTime().toString();
	            incomingMessages.put(key, message);
	        }
	        
	        // Delete messages from the external server after retrieving them
	        Folder inbox = pop3Client.getStore().getFolder("INBOX");
	        inbox.open(Folder.READ_WRITE);
	        javax.mail.Message[] javaxMessages = inbox.getMessages();
	        for (javax.mail.Message javaxMessage : javaxMessages) {
	            pop3Client.deleteMessage(javaxMessage);
	        }
	        inbox.close(true);
	    } catch (MessagingException e) {
	        e.printStackTrace();
	    }
	}
	
	public void processIncomingMessages() {
		// Get the singleton instance of the message collection
		MessageCollection messageCollection = MessageCollection.getInstance();

		// Iterate over incoming messages and attempt to crack their encryption
		for (Message message : incomingMessages.values()) {
			try {
				// Attempt to crack CaesarCipher encryption
				ICipher crackedCaesarCipher = crackCaesarCipher(message.getContent());
				if (crackedCaesarCipher != null) {
					
					// Added to debug
					System.out.println("Cracked");
					
					message.setKey(crackedCaesarCipher);
					String decryptedContent = message.getContent();
					message.setContent(decryptedContent);
				} 
				else {
					// Added to debug
					System.out.println("Not cracked");
				}

				// Add the message to the message collection
				messageCollection.addMessage(message.getTo(), message.getFrom(), message.getDateTime(), message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Clear the incoming messages map
		incomingMessages.clear();
	}
	
	private ICipher crackCaesarCipher(String encryptedContent) {
		WordSet wordSet = WordSet.getInstance();

		for (int key = 0; key < 26; key++) {
			CaesarCipher cipher = new CaesarCipher(key);
			String decryptedContent = cipher.decrypt(encryptedContent);

			String[] words = decryptedContent.split(" ");
			int knownWords = 0;

			for (String word : words) {
				if (wordSet.contains(word.trim().toLowerCase())) {
					knownWords++;
				}
			}
			
			// 
			System.out.println("Key: " + key + ", Decrypted content: " + decryptedContent + ", Known words: " + knownWords);
			
			// We check if at least 95% of the words in the decrypted message exist
			// if so, we can consider that the message as cracked
			if (((float) knownWords) / words.length >= 0.95) {
				return cipher;
			}
		}
		return null;
	}
	
	public void sendOutgoingMessages() throws MessagingException {
		// Get the singleton instance of the message collection
		MessageCollection messageCollection = MessageCollection.getInstance();

		// Iterate over outgoing messages and send them
		for (Message message : outgoingMessages.values()) {
			try {
				// Encrypt the content of the message using the CaesarCipher class
				CaesarCipher cipher = new CaesarCipher(3); // Shift of 3
				message.setKey(cipher);
				String encryptedContent = cipher.encrypt(message.getContent());
				message.setContent(encryptedContent);

				// Create a new SMTP client and send the message
				SMTPClient smtpClient = new SMTPClient();
				smtpClient.sendEmail(message);

				// Add the message to the message collection
				messageCollection.addMessage(message.getTo(), message.getFrom(), message.getDateTime(), message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Clear the outgoing messages map
		outgoingMessages.clear();
	}

	public void listenForIncomingTCPIPRequests() {
		try (ServerSocket serverSocket = new ServerSocket(0)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				// Start a new short-lived thread for each incoming connection to handle the request
				new Thread(new IncomingRequestHandler(clientSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void listenForClientApplicationRequests() {
		try (ServerSocket serverSocket = new ServerSocket(0)) {
			while (true) {
				Socket clientSocket = serverSocket.accept();
				// Start a new short-lived thread for each incoming connection to handle the request
				new Thread(new ClientRequestHandler(clientSocket)).start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws MessagingException {
		// Starting the server
		// Check out Settings for the parameters
		BackendServer server = new BackendServer(Settings.POP3_USER, Settings.POP3_PASSWORD);

		// Here we start listening for incoming TCP/IP requests and client application requests in separate threads
		new Thread(server::listenForIncomingTCPIPRequests).start();
		new Thread(server::listenForClientApplicationRequests).start();

		// Here we check external SMTP servers every one minute to retrieve messages
		while (true) {
			// retrieve messages
			server.retrieveMessagesFromPOP3();
			// process incoming messages
			server.processIncomingMessages();
			try {
				// Check out Settings for the parameter
				Thread.sleep(Settings.DEFAULT_POLLING_INTERVAL); // Sleep for 1 minute
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
