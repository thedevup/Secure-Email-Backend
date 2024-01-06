package client;

import conf.Settings;
import domain.Message;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SMTPClient {
	private Session session;

	public SMTPClient() {
		Properties properties = new Properties();
		// Check out Settings
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", Settings.SMTP_SERVER);
		properties.put("mail.smtp.port", Integer.toString(Settings.SMTP_PORT));
		properties.put("mail.smtp.starttls.enable", "true");

		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(Settings.SMTP_USER, Settings.SMTP_PASSWORD);
			}
		});
	}

	public void sendEmail(Message message) throws Exception {
		MimeMessage mimeMessage = new MimeMessage(session);
		mimeMessage.setFrom(new InternetAddress(Settings.SMTP_USER));
		mimeMessage.setRecipients(null, InternetAddress.parse(message.getTo().getEmailAddress()));
		mimeMessage.setSubject(message.getSubject());
		mimeMessage.setText(message.getContent());

		Transport.send(mimeMessage);
	}
}
