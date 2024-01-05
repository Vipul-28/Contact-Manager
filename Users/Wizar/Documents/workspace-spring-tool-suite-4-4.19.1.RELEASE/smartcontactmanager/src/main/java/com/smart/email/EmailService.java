package com.smart.email;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import javax.mail.internet.MimeMessage;

//import jakarta.mail.Authenticator;
//import jakarta.mail.Message;
//import jakarta.mail.PasswordAuthentication;
//import jakarta.mail.Session;
//import jakarta.mail.Transport;
//import jakarta.mail.internet.InternetAddress;
//import jakarta.mail.internet.MimeMessage;
@Component
public class EmailService {
	@Autowired
	private JavaMailSender mailSender;
	public boolean sendEmail(String to, String subject, String message) {
		boolean b = false;
		/*
		 * Properties properties=new Properties(); properties.put("mail.smtp.auth",
		 * "true");
		 * 
		 * properties.put("mail.smtp.ssl.enable", "true");
		 * 
		 * properties.put("mail.smtp.port", "465");
		 * 
		 * properties.put("mail.smtp.host", "smtp.gmail.com ");
		 * 
		 * String user="vipuldongre348@gmail.com"; String password="bhumhewjfxscwpzq";
		 * //session Session session=Session.getInstance(properties,new Authenticator()
		 * {
		 * 
		 * @Override protected PasswordAuthentication getPasswordAuthentication() {
		 * return new PasswordAuthentication(user,password);
		 * 
		 * } });
		 */
		try {
//			Message message1 = new MimeMessage(session);
			SimpleMailMessage message1=new SimpleMailMessage();
			message1.setFrom("vipuldongre348@gmail.com");
			message1.setSubject(subject);
			message1.setText(message);
			message1.setTo(to);
			mailSender.send(message1);
			b = true;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
}
