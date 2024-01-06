package app;

import javax.mail.MessagingException;

import client.POP3Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Main {
	public static void main(String[] args) {
	    try {
	        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

	        System.out.println("Email address: ");
	        String email = reader.readLine();

	        System.out.println("Password: ");
	        String password = reader.readLine();

	        POP3Client pop3Client = new POP3Client(email, password);

	        System.out.println("\n Pick an action : list - retrieve - delete - exit: ");
            String action = reader.readLine();

            switch (action.toLowerCase()) {
                case "list":
                    pop3Client.listMessages();
                    break;

                case "exit":
                    pop3Client.close();
                    System.exit(0);

                default:
                    System.out.println("Wrong action");
	        }
	    } catch (MessagingException | IOException e) {
	        System.out.println(e);
	    }
	}

}
