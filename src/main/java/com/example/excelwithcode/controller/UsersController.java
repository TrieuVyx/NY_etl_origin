package com.example.excelwithcode.controller;
import com.example.excelwithcode.model.UsersModel;
import com.example.excelwithcode.service.UsersService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import jakarta.validation.Valid;
import java.util.Date;
@Controller
@CrossOrigin("http://localhost:3000/")

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
                String hashedPassword = encoder.encode(usersModel.getPassword(

                ));
                existingUser.setPassword(hashedPassword);
            }

            usersService.updateUser(existingUser);

            return new ResponseEntity<>(existingUser, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UsersModel> login(@Valid @RequestBody UsersModel usersModel) {
        try {
            UsersModel user = usersService.findByEmail(usersModel.getEmail());


            if(user!= null && BCrypt.checkpw(usersModel.getPassword(), user.getPassword())){
                String secretKey = "aa76ce10af6dbc9a8c75b4b2b68c8f84ba78794e689baf4d2c17d31bd0731f13b7c1e6ac381e4426494a89f6b4549b5327b705f16c2e4a1aee0cb82c90186914eb7a95c8e3f5ac6d0f44590cf5a6341bbdd189b1ff45fbdffa900abfecb76323c131a93a5fc8e90010e668913cdde5373428bd24a332a4dccba41d9e3895d1afee94457910df844a98a344d0e7a42db80e6c2f43a5d0454af0721fc33b3ff329733d1f6b9cf95b5586c4cb33edb96d12dfd1693cb189e2fb7696f07fb988098715e425c6bf7073f86f0dc4b294339171704c6d6455563e6e99d338fb3caa937e7a5a464cd0e01b5918bf1286817946851c4df56d1cfe3ea5c9e4704d21137b16";
                    /// set token refresh
                String token = Jwts.builder()
                        .setId(UUID.randomUUID().toString())
                        .setIssuedAt(Date.from(Instant.now()))
                        .setExpiration(Date.from(Instant.now().plus(5L, ChronoUnit.MINUTES)))
                        .setSubject(user.getEmail())
                        .signWith(SignatureAlgorithm.HS256, secretKey.getBytes())
                        .compact();

                user.setToken(token);


                return new ResponseEntity<>(user, HttpStatus.OK);
            }
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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



}
