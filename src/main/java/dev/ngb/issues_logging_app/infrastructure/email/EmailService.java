package dev.ngb.issues_logging_app.infrastructure.email;

import java.util.Map;

public interface EmailService {
    /**
     * Sends an email asynchronously.
     *
     * @param email   the recipient's email address
     * @param subject the subject of the email
     * @param message the content of the email
     */
    void sendEmailAsync(String email, String subject, String message);

    /**
     * Sends a template-based email asynchronously.
     *
     * @param subject      the subject of the email
     * @param emailTo      the recipient's email address
     * @param templatePath the path to the email template
     * @param templateData the data to be used in the template
     */
    void sendTemplateEmailAsync(String subject, String emailTo, String templatePath, Map<String, Object> templateData);

    /**
     * Sends an email.
     *
     * @param email   the recipient's email address
     * @param subject the subject of the email
     * @param message the content of the email
     */
    void sendEmail(String email, String subject, String message);

    /**
     * Sends a template-based email.
     *
     * @param subject      the subject of the email
     * @param emailTo      the recipient's email address
     * @param templatePath the path to the email template
     * @param templateData the data to be used in the template
     */
    void sendTemplateEmail(String subject, String emailTo, String templatePath, Map<String, Object> templateData);
}
