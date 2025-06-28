package com.codewithmanas.skillexplatformbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine springTemplateEngine;

    public EmailService(JavaMailSender mailSender, SpringTemplateEngine springTemplateEngine) {
        this.mailSender = mailSender;
        this.springTemplateEngine = springTemplateEngine;
    }

    @Async
    public void sendVerificationEmail(String to, String link) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("link", link);

            String htmlContent = springTemplateEngine.process("verification-email", context);

            helper.setTo(to);
            helper.setSubject("Verify Your Email");
            helper.setText(htmlContent, true); // Set `isHtml=true`

            mailSender.send(message);

        } catch (MessagingException ex) {
            throw new IllegalStateException("Failed to send email :" + ex.getMessage());
        }
    }

    @Async
    public void sendResetPasswordEmail(String to, String link) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("link", link);

            String htmlContent = springTemplateEngine.process("reset-password", context);

            helper.setTo(to);
            helper.setSubject("Reset Your Password");
            helper.setText(htmlContent, true);

            mailSender.send(message);

        } catch (MessagingException ex) {
            throw new IllegalStateException("Failed to send email :" + ex.getMessage());
        }
    }

}
