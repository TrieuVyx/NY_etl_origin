package com.example.excelwithcode.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
@Entity
public class UsersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Max(15)
//    @Min(5)
//    @NotBlank(message = "Username cannot empty")
    private String username;
//    @Max(value = 15, message = "Character have to equal 15 or much than 15")
//    @Min(value = 10, message = "Character have to equal 10 or less than 10")
//    @NotBlank(message = "Password cannot empty")
    private String password;
//    @NotBlank(message = "Email cannot empty")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$", message = "Invalid email format")
    private String email;
    private String token;
    private String permission;
    private String avatar;
    private String code;
    public UsersModel() {
    }
    public void setCode(String code){
        this.code = code;
    }
    public String getCode(){
        return code;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Long getId() {
        return id;
    }
    public void setUsername(String username){
        this.username = username;
    }
    public  String getUsername(){
        return username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public  String getPassword(){
        return password;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public  String getEmail(){
        return email;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public String getPermission() {
        return permission;
    }
    public void setPermission(String permission){
        this.permission = permission;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return avatar;
    }


}
