package com.KapybaraWeb.kapyweb.service.config;

import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("realEmailService")
public class MailService {
    @Value("${spring.mail.username}")
    private String email;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public MailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendWelcomeEmail(String toEmail, String name) {
        try {
            Context context = new Context();
            context.setVariable("name", name);
            String html = templateEngine.process("thank-you-register", context);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(email);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to Kapibala!");
            helper.setText(html, true);

            mailSender.send(message);
            log.info("Email sent to {}", toEmail);
        } catch (Exception e) {
            log.error("Error sending email", e); // Hiển thị lỗi chi tiết
        }
    }
}
