package com.example.excelwithcode.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EmailModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
    private String email;
    private String subject ;
    private String text;

    public void setEmail(String email)
    {
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setSubject(String subject){
        this.subject = subject;
    }
    public String getSubject(){
        return subject;
    }

    public void setText(String text){
        this.text = text;
    }
    public String getText(){
        return text;
    }




}
