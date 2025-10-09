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
import rs.ac.uns.ftn.informatika.jpa.dto.UserDTO;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Environment env;




    @Async
    public void sendVerificationEmail(UserDTO userDTO, String link) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(userDTO.getEmail());
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setSubject("Verification email");

            String htmlMsg = "<p>Pozdrav " + userDTO.getFirstName() + ",</p>"
                    + "<p>Klikni na link ispod kako bi izvršio verifikaciju:</p>"
                    + "<a href='" + link + "'>Verifikuj svoj nalog</a>"
                    + "<p>Hvala!</p>";

            helper.setText(htmlMsg, true);
            System.out.println("Slanje emaila na: " + userDTO.getEmail());
            javaMailSender.send(mail);
        } catch (MessagingException e) {

            Thread.currentThread().interrupt();
        }
    }

    public void sendNotificationEmail(String userEmail, String htmlMsg){
        try{
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);

            helper.setTo(userEmail);
            helper.setFrom(env.getProperty("spring.mail.username"));
            helper.setSubject("User statistic from past 7 days");
            helper.setText(htmlMsg, true);

            System.out.println("Mail sa statistikom poslat na: " + userEmail);

            javaMailSender.send(mail);
        } catch (MessagingException e){
            Thread.currentThread().interrupt();
        }
    }

}