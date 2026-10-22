package com.nexion.backend.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service 
public class EmailService {
    
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String remetente;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarResetSenha(String destinatario, String token) {
        String link = frontendUrl + "/redefinir-senha/" + token;

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Nexion - Redefinição de senha");
        mensagem.setText("Voce solicitou a redefinição da sua senha. \n\n"+ "Acesse este link abaixo para criar uma nova senha: \n" + link + "\n\n" + "Este link expira em 30 minutos. \n" +
        "Caso não tenha feito a solicitação, ignore este e-mail..");

    mailSender.send(mensagem);
    }
}
