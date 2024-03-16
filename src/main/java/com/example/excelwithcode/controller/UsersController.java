package com.example.excelwithcode.controller;

import com.example.excelwithcode.model.UsersModel;
import com.example.excelwithcode.service.UsersService;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import javax.mail.Session;

@Controller
public class UsersController {
    private final UsersService usersService;
   @Autowired
    public  UsersController(UsersService usersService){
        this.usersService = usersService;
    }
    // read all
    @GetMapping("users")
    public String getAll(Model model){
        List<UsersModel> usersModel = usersService.getAllUser();
        model.addAttribute("usersModel", usersModel);
        return "Users/index";
    }

    // create form
    @GetMapping("users/create")
    public String ShowFormCreate(Model model){
        UsersModel usersModel = new UsersModel();
        model.addAttribute("usersModel",usersModel);
        return "Users/create";
    }

    //update form
    @GetMapping("/users/api/edit/{id}")
    public String ShowFormUpdate(@PathVariable Long id, Model model){
        UsersModel usersModel = usersService.getUserById(id);
        model.addAttribute("usersModel", usersModel);
        return "Users/update";
    }
    //delete form
    @GetMapping("/users/api/delete/{id}")
    public String ShowFormDelete(@PathVariable Long id, Model model){
        UsersModel usersModel = usersService.getUserById(id);
        model.addAttribute("usersModel", usersModel);
        return "Users/delete";
    }
    //register form
    @GetMapping("/register")
    public String ShowFormRegister( Model model){
        UsersModel usersModel = new UsersModel();
        model.addAttribute("usersModel",usersModel);
        return "register";
    }

    //login form
    @GetMapping("/login")
    public String ShowFormLogin(Model model){
        UsersModel usersModel = new UsersModel();
        model.addAttribute("usersModel",usersModel);
        return "login";
    }

    //create
    @PostMapping ( "users/create" )
    public String createUser(@Valid UsersModel usersModel, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                return "Users/create";
            }
            // Mã hóa mật keys
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(usersModel.getPassword());

            UsersModel usersModel1 = new UsersModel();
            usersModel1.setUsername(usersModel.getUsername());
            usersModel1.setEmail(usersModel.getEmail());
            usersModel1.setPassword(hashedPassword);

            // Tạo mã thông báo
            String token = Jwts.builder()
                    // .signWith( SignatureAlgorithm.HS256, "sdaAa@dawâss")
                    .setSubject(usersModel.getUsername())
                    .compact() ;

            // Trả về mã thông báo trong response
            usersModel1.setToken(token);
            usersService.setUser(usersModel1);
            return  "Users/create";
        }
        catch(Exception e){
            return e.getMessage();
        }
    }


    @PostMapping("/users/api/delete/{id}")
    public String deleteUser(@PathVariable Long id, Model model){
        try {
            usersService.deleteUser(id);
            model.addAttribute("message", "User deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
        }
        return "Users/index";

    }

    @PostMapping("/users/api/update/{id}")
    public ResponseEntity<UsersModel> updateUser(@PathVariable Long id, UsersModel usersModel) {
        try {
            UsersModel existingUser = usersService.getUserById(id);

            if (existingUser == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            existingUser.setUsername(usersModel.getUsername());
            existingUser.setEmail(usersModel.getEmail());

            if (usersModel.getPassword() != null) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashedPassword = encoder.encode(usersModel.getPassword());
                existingUser.setPassword(hashedPassword);
            }

            usersService.updateUser(existingUser);

            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public String login(@Valid UsersModel usersModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                return "login";
            }

            UsersModel user = usersService.findByEmail(usersModel.getEmail());
            if (user != null && Objects.equals(user.getPassword(), usersModel.getPassword())) {
                return "index";
            }

            return "login";
        } catch (Exception e) {
            return "login";
        }
    }


    @PostMapping ( "/register" )
    public String createUserR(@Valid UsersModel usersModel, BindingResult bindingResult){
        try{

            if(bindingResult.hasErrors()){
                return "register";
            }
            // Mã hóa mật keys
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(usersModel.getPassword());

            UsersModel usersModel1 = new UsersModel();
            if(!usersService.EmailSame(usersModel.getEmail()) & !usersService.UsernameSame(usersModel.getUsername())){
                return "register";
            }

            usersModel1.setUsername(usersModel.getUsername());
            usersModel1.setEmail(usersModel.getEmail());
            usersModel1.setPassword(hashedPassword);

            String token = Jwts.builder()
                    // .signWith( SignatureAlgorithm.HS256, "sdaAa@dawâss")
                    .setSubject(usersModel.getUsername())
                    .compact() ;

            usersModel1.setToken(token);
            usersService.setUser(usersModel1);
            return  "login";
        }
        catch(Exception e){
            return e.getMessage();
        }
    }
    @PostMapping ( "/deleteAll" )
    public String deleteAll(){
        usersService.DeleteAll();
        return "Users/index";
    }


    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody UsersModel usersModel) {
        String email = "trieuvy.nguyen@vn.wilmar-intl.com";
        String password = "vlvvkkmtxgpphdlh";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.port", "587");

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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(usersModel.getEmail()));

            // Tiêu đề email
            message.setSubject("Test Email");

            // Nội dung email
            message.setText("This is a test email sent from Java.");

            // Gửi email
            Transport.send(message);

            return ResponseEntity.ok("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email.");
        }
    }


}
