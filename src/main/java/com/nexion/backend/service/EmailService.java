package com.nexion.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void enviarResetSenha(String destinatario, String token) {
        Context contexto = new Context();
        contexto.setVariable("link", frontendUrl + "/redefinir-senha/" + token);

        String html = templateEngine.process("email-reset-senha", contexto);

        try {
            MimeMessage mensagem = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");
            helper.setFrom(remetente);
            helper.setTo(destinatario);
            helper.setSubject("Nexion - Redefinição de senha");
            helper.setText(html, true);
            mailSender.send(mensagem);
        } catch (MessagingException e) {
            log.error("Falhar na montagem do e-mail de redefinição", e);
            throw new IllegalStateException("Falha ao enviar o e-mail", e);
        }
    }
}