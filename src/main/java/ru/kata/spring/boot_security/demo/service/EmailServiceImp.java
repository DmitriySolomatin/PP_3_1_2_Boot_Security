package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.EmailDetails;
import ru.kata.spring.boot_security.demo.model.User;

@Service
public class EmailServiceImp implements EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String sender;

    @Autowired
    public EmailServiceImp(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendMail(EmailDetails details) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(sender);
        mailMessage.setTo(details.getRecipient());
        mailMessage.setText(details.getMsgBody());
        mailMessage.setSubject(details.getSubject());

        try {
            this.javaMailSender.send(mailMessage);
        } catch (MailException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void sendRegistrationInfo(User user, String password) {
        EmailDetails details = new EmailDetails();
        details.setSubject("Регистрация на сайте www.ru");
        details.setRecipient(user.getEmail());
        details.setMsgBody(String.format("""
                        Привет, %s!
                        Ты зарегистрировался на сайте www.ru.
                        Данные для входа:
                        Логин: %s
                        Пароль: %s""",
                user.getName(), user.getEmail(), password));
        sendMail(details);
    }

}
