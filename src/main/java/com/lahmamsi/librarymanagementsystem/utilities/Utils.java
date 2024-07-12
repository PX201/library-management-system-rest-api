package com.lahmamsi.librarymanagementsystem.utilities;

import java.util.Properties;

import com.lahmamsi.librarymanagementsystem.config.MailConfig;
import jakarta.mail.Authenticator;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMessage.RecipientType;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class Utils {

	public static Pageable page(int pageSize, int pageNumber, String field) {
		return PageRequest.of(pageNumber, pageSize, Sort.by(Direction.ASC, field));
	}
	
	public static void sendEmail( String to,String subject, String body) {
		String username = MailConfig.getUsername();
		String password =  MailConfig.getPassword();
		
		Properties prop = new Properties();
		prop.put("mail.smtp.host", MailConfig.getHost());
		prop.put("mail.smtp.port", MailConfig.getPort());
		prop.put("mail.smtp.auth", MailConfig.isAuth());
		prop.put("mail.smtp.starttls.enable", MailConfig.isTlsEnable());
		prop.put("mail.smtp.ssl.trust", MailConfig.getSslTrust());
		
		Session session = Session.getDefaultInstance(prop, new Authenticator() {
			 protected jakarta.mail.PasswordAuthentication getPasswordAuthentication() {
	                return new jakarta.mail.PasswordAuthentication(username, password);
	            }
		});
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.addRecipients(RecipientType.TO ,to);
			message.setSubject(subject);
			message.setText(body);
			
			Transport.send(message);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
}
