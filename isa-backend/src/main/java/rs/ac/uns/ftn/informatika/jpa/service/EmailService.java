/*
package rs.ac.uns.ftn.informatika.jpa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.informatika.rest.dto.UserDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;

    @Async
    public void sendVerificationEmail(UserDTO userDTO,String link) throws MailException, InterruptedException, MessagingException {

        MimeMessage mail = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mail, true);

        helper.setTo(userDTO.email);
        helper.setFrom(env.getProperty("spring.mail.username"));
        helper.setSubject("Verification email");

        String verificationUrl = link;
        String htmlMsg = "<p>Pozdrav " + userDTO.name + ",</p>"
                + "<p>Klikni na link ispod kako bi izvršio verifikaciju:</p>"
                + "<a href='" + verificationUrl + "'>Verifikuj svoj nalog</a>"
                + "<p>Hvala!</p>";

        // Postavite HTML sadržaj mejla
        helper.setText(htmlMsg, true);

        javaMailSender.send(mail);
    }
}*/