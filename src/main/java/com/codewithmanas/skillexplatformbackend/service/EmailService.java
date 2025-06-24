package com.codewithmanas.skillexplatformbackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
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

            String htmlContent = springTemplateEngine.process("verification-email.html", context);



            helper.setTo(to);
            helper.setSubject("Verify Your Email");
            helper.setText(htmlContent, true); // Set `isHtml=true`

            mailSender.send(message);

        } catch (MessagingException ex) {
            throw new IllegalStateException("Failed to send email :" + ex.getMessage());
        }
    }

//    private String buildEmailContent(String verificationLink) {
//        return """
//                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #ddd; border-radius: 10px; text-align: center;">
//                    <h2 style="color: #333;">Verify Your Email</h2>
//                    <p style="font-size: 16px;">Click the button below to verify your email address:</p>
//                    <a href="%s" style="display: inline-block; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px;">Verify Email</a>
//                    <p style="font-size: 14px; color: #666;">If you didn’t request this, you can ignore this email.</p>
//                </div>
//                """.formatted(verificationLink);
//    }

}
