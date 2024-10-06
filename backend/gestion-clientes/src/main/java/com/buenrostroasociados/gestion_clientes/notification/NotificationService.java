package com.buenrostroasociados.gestion_clientes.notification;

public interface NotificationService {
    void notifyActivityContableCreation(String email, String actividadTitle);

    void notifyActivityContableUpdate(String email, String actividadTitle);

    void notifyActivityContableDeletion(String email, String actividadTitle);

    void notifyActivityLitigioCreation(String email, String actividadTitle);

    void notifyActivityLitigioUpdate(String email, String actividadTitle);

    void notifyActivityLitigioDeletion(String email, String actividadTitle);

    void notifyActivityLitigioUpdatedStatus(String email, String activityTittle);

    void notifyEventResetPassword(String email, String subject, String text);


    void notifyEventUserLogin(String email, String subject, String text);

    void notifyEventUserLogout(String email, String subject, String text);

    void notifyEventUserRegister(String email, String subject, String text);

    void notifyArchivoCreation(String email, String actividadTitle);

    void notifyArchivoUpdate(String email, String actividadTitle);

    void notifyArchivoDeletion(String email, String actividadTitle);
}
