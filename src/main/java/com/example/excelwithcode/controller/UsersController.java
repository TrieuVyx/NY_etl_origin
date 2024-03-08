package com.example.excelwithcode.controller;

import com.example.excelwithcode.model.UsersModel;
import com.example.excelwithcode.service.UsersService;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Objects;

import jakarta.validation.Valid;

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
//            return new ResponseEntity<>( usersModel1,HttpStatus.OK);
            return  "Users/create";
        }
        catch(Exception e){
            return e.getMessage();
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
        if (bindingResult.hasErrors()) {
            return "register"; // Return to the registration page if there are validation errors
        }

        boolean isAuthenticated = usersService.authenticateUser(usersModel.getEmail(), usersModel.getPassword());

        if (!isAuthenticated) {
            // Generate the token
            String token = Jwts.builder()
                    // .signWith(SignatureAlgorithm.HS256, "sdaAa@dawâss")
                    .setSubject(usersModel.getEmail())
                    .compact();

            return "login"; // Redirect to the login success page
        }
        return "index";
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

            // Tạo mã thông báo
            String token = Jwts.builder()
                    // .signWith( SignatureAlgorithm.HS256, "sdaAa@dawâss")
                    .setSubject(usersModel.getUsername())
                    .compact() ;

            // Trả về mã thông báo trong response
            usersModel1.setToken(token);
            usersService.setUser(usersModel1);
        //  return new ResponseEntity<>( usersModel1,HttpStatus.OK);
            return  "login";
        }
        catch(Exception e){
            return e.getMessage();
            //    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping ( "/deleteAll" )
    public String deleteAll(){
        usersService.DeleteAll();
        return "Users/index";
    }



//    @PostMapping("/login")
//    public ResponseEntity<String> login(@Valid UsersModel usersModel, BindingResult bindingResult) {
//        try {
//            boolean isAuthenticated = usersService.authenticateUser(usersModel.getUsername(), usersModel.getPassword());
//
//            if (isAuthenticated) {
//                String token = Jwts.builder()
//                        // .signWith( SignatureAlgorithm.HS256, "sdaAa@dawâss")
//                        .setSubject(usersModel.getUsername())
//                        .compact();
//
//                return new ResponseEntity<>(token, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//            }
//        } catch (Exception e) {
//            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//        }
//    }
}
