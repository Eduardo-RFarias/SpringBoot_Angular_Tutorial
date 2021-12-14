package com.example.reddit.service;

import com.example.reddit.exception.UnknownException;
import com.example.reddit.model.NotificationEmail;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;

@Service
@AllArgsConstructor
@Log
public class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);

            messageHelper.setFrom("springreddit@email.com");
            messageHelper.setTo(notificationEmail.getRecipient());
            messageHelper.setSubject(notificationEmail.getSubject());
            messageHelper.setText(
                    mailContentBuilder.build(
                            notificationEmail.getMessage(),
                            notificationEmail.getLink()),
                    true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Email sent");
        } catch (MailException e) {
            throw new UnknownException(
                    "Exception occurred when sending email to: " + notificationEmail.getRecipient());
        }

    }
}
