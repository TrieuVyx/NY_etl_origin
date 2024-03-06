package com.example.excelwithcode.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UsersModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;


    private String password;


    private String email;
    private String token;
    private String permission;



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
}
