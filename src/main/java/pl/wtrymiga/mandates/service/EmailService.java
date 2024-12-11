package pl.wtrymiga.mandates.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private MailSender mailSender;

	public void sendEmail(String to, String subject, String text) {
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		message.setFrom("sm@um.gda.pl");

		mailSender.send(message);
	}
}