package pl.wtrymiga.mandates.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

@Configuration
@Profile("dev")
public class FakeMailConfig {

	@Bean
	public MailSender mailSender() {
		return new MailSender() {
			@Override
			public void send(SimpleMailMessage simpleMessage) {
				System.out.println("Symulowane wysłanie maila: " + simpleMessage);
			}

			@Override
			public void send(SimpleMailMessage... simpleMessages) {
				System.out.println("Symulowane wysłanie maili: " + simpleMessages.length);
			}
		};
	}
}