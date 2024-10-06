package com.buenrostroasociados.gestion_clientes.service.email;

import com.buenrostroasociados.gestion_clientes.entity.Usuario;
import com.buenrostroasociados.gestion_clientes.exception.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.context.Context;

@Service
public class EmailServiceImpl implements EmailService{

    private static final Logger logger= LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private final JavaMailSender mailSender;

    @Autowired
    private final SpringTemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendPasswordResetEmail(Usuario user, String resetUrl) {
        try {
            Context context = new Context();
            context.setVariable("resetUrl", resetUrl);
            context.setVariable("user", user);
            String template = "email/password-reset-email";

            String htmlBody = templateEngine.process(template, context);


            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
            helper.setTo(user.getEmail());
            helper.setSubject("Solicitud de Restablecimiento de contraseña");
            helper.setText(htmlBody, true);

            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException ex) {
            throw new EmailException("Ocurrió un error al enviar el email de Recuperacion: " + ex.getMessage());
        }
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        ///enviar coreeo sion platillas
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(mimeMessage);
        } catch (MailException | MessagingException e) {
            logger.error("Error sending email to {}: {}", to, e.getMessage());
            throw new EmailException("Falló al enviar el correo electrónico : " + e.getMessage());
        }
    }

}
