package com.rabiloo.khanhhd.issues_logging.infrastructure.email.impl;

import com.rabiloo.khanhhd.issues_logging.infrastructure.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

@Component
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender emailSender, TemplateEngine templateEngine) {
        this.emailSender = emailSender;
        this.templateEngine = templateEngine;
    }

    @Async
    @Override
    public void sendEmailAsync(String email, String subject, String message) {
        sendEmail(email, subject, message);
    }

    @Async
    @Override
    public void sendTemplateEmailAsync(String subject, String emailTo, String templatePath, Map<String, Object> templateData) {
        sendTemplateEmail(subject, emailTo, templatePath, templateData);
    }

    @Override
    public void sendEmail(String email, String subject, String message) {
        send(subject, email, message, false);
    }

    @Override
    public void sendTemplateEmail(String subject, String emailTo, String templatePath, Map<String, Object> templateData) {
        Context templateContext = new Context();
        templateContext.setVariables(templateData);
        String content = templateEngine.process(templatePath, templateContext);
        send(subject, emailTo, content, true);
    }

    private void send(String subject, String emailTo, String content, boolean isHtmlFormat) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "utf-8");

            message.setSubject(subject);
            message.setText(content, isHtmlFormat);
            message.setTo(emailTo);

            emailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("Failed to send email to " + emailTo, e);
        }
    }
}
