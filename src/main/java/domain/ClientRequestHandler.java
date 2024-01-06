package domain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRequestHandler implements Runnable {
	private Socket clientSocket;

	public ClientRequestHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			 PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {

			// Set up a secure session using the BouncyCastle library.

			String requestType = in.readLine();
			if ("send".equalsIgnoreCase(requestType)) {
				// Send a message using an external SMTP server
			} else if ("retrieve".equalsIgnoreCase(requestType)) {
				// Retrieve messages from the processedMessages map, send them to the client,
				// and delete them from the server after acknowledgment
				// Check out BackedServer
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				clientSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
