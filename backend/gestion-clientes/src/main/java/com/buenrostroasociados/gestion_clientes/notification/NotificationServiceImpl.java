package com.buenrostroasociados.gestion_clientes.notification;

import com.buenrostroasociados.gestion_clientes.repository.ActividadContableRepository;
import com.buenrostroasociados.gestion_clientes.repository.ActividadLitigioRepository;
import com.buenrostroasociados.gestion_clientes.service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class NotificationServiceImpl implements NotificationService{

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private EmailService emailService;
    @Autowired
    private ActividadContableRepository actividadContableRepo;
    @Autowired
    private ActividadLitigioRepository actividadLitigioRepo;

    @Autowired
    private TemplateEngine templateEngine;

    @Override
    public void notifyActivityContableCreation(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Activity-C Creation...");
        String subject = "Nueva Actividad Contable Creada";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "Se ha creado una nueva actividad contable: " + actividadTitle);

        String body = templateEngine.process("notification/notification-email", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyActivityContableUpdate(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Activity-C Update...");
        String subject = "Actividad Contable Actualizada";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "La actividad contable ha sido actualizada: " + actividadTitle);

        String body = templateEngine.process("notification/notification-email", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyActivityContableDeletion(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Activity-C Deletion...");
        String subject = "Actividad Contable Eliminada";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "La actividad contable ha sido eliminada: " + actividadTitle);

        String body = templateEngine.process("notification/notification-email", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyActivityLitigioCreation(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Activity-L Creation...");
        String subject = "Nueva Actividad Litigiosa Creada";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "Se ha creado una nueva actividad litigiosa: " + actividadTitle);

        String body = templateEngine.process("notification/notification-email", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyActivityLitigioUpdate(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Activity-L Update...");
        String subject = "Actividad Litigiosa Actualizada";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "La actividad litigiosa ha sido actualizada: " + actividadTitle);

        String body = templateEngine.process("notification/notification-email", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyActivityLitigioDeletion(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Activity-L Deletion...");
        String subject = "Actividad Litigiosa Eliminada";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "La actividad litigiosa ha sido eliminada: " + actividadTitle);

        String body = templateEngine.process("notification/notification-email", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyActivityLitigioUpdatedStatus(String email, String activityTittle){
        logger.info("Procesing Template Notification Activity-L Update status Caso...");
        String subject = "Notificacion del Seguimiento del caso";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "Hola, El Status de la actividad Litigio ha sido actualizado:  "
                + activityTittle +".\nDirigete e inicia sesion en nuestra plataforma para visualizar y verificar el seguimiento del caso.");

        String body = templateEngine.process("notification/notification-activity-l-estadocaso", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyEventResetPassword(String email, String subject, String text){
        logger.info("Procesing Template Notification ResetPassword ...");
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", text);

        String body = templateEngine.process("notification/notification-password-reset", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyEventUserLogin(String email, String subject, String text){
        logger.info("Procesing Template Notification Login ...");
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", text);

        String body = templateEngine.process("notification/notification-login", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }
    @Override
    public void notifyEventUserLogout(String email, String subject, String text){
        logger.info("Procesing Template Notification Logout ...");
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", text);

        String body = templateEngine.process("notification/notification-logout", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }
    @Override
    public void notifyEventUserRegister(String email, String subject, String text){
        logger.info("Procesing Template Notification Register ...");
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", text);

        String body = templateEngine.process("notification/notification-register", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyArchivoCreation(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Archivo creation ...");
        String subject = "Notificacion-Nuevo Archivo Subido";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "Se ha registrado un nuevo archivo a una actividad: " + actividadTitle);

        String body = templateEngine.process("notification/notification-archivo", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyArchivoUpdate(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Archivo Update ...");
        String subject = "Notificacion-Actualizacion de Archivo";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "Se ha detectado cambios realizado en el archivo: " + actividadTitle + " puedes Verificarlos en nuestra plataforma.");

        String body = templateEngine.process("notification/notification-archivo", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

    @Override
    public void notifyArchivoDeletion(String email, String actividadTitle) {
        logger.info("Procesing Template Notification Archivo Deletion ...");
        String subject = "Notificacion-Archivo Eliminado";
        Context context = new Context();
        context.setVariable("subject", subject);
        context.setVariable("message", "Confirmamos que el archivo " + actividadTitle+ " Ha sido Eliminado con exito.");

        String body = templateEngine.process("notification/notification-archivo", context);
        emailService.sendEmail(email, subject, body);
        logger.info("Template Send to Email: {}, {}", email, context);
    }

}
