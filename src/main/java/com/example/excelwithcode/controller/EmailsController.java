package com.example.excelwithcode.controller;


import com.example.excelwithcode.model.EmailModel;
import com.example.excelwithcode.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Properties;

@Controller
public class EmailsController {

    @Autowired
    private  final EmailService emailService;
    @Value("${EMAIL}")
    private String EmailENV;
    @Value("${APP_PASS}")
    private String APP_PASS;
    @Value("${PORT_SERVICES}")
    private String PORT_SERVICES;
    @Value("${OUTLOOK_SERVICES}")
    private String OUTLOOK_SERVICES;


    public EmailsController(EmailService emailService){
        this.emailService = emailService;
    }
    @GetMapping("/send-email")
    public String show (Model model){
        EmailModel emailModel = new EmailModel();
        model.addAttribute("emailModel",emailModel);
        return "Email/index";
    }
    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(EmailModel emailModel) {

        String email = EmailENV;
        String password = APP_PASS;

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", OUTLOOK_SERVICES);
        props.put("mail.smtp.port", PORT_SERVICES);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            Message message = new MimeMessage(session);

            // Địa chỉ người gửi
            message.setFrom(new InternetAddress(email));

            // Địa chỉ người nhận
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailModel.getEmail()));

            // Tiêu đề email
            message.setSubject(emailModel.getSubject());

            // Nội dung email
            message.setText(emailModel.getText());

            // Gửi email
            Transport.send(message);

            return ResponseEntity.ok("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }
    }

}
