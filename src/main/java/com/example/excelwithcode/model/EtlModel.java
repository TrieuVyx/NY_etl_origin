package com.example.excelwithcode.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class EtlModel {
    @Id
    private Long id;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    private String name;
    private Integer age;
    private String email;

    public void setEmail(String email){ this.email = email;}
    public String getEmail(){return email;}
    public void setAge(Integer age){this.age = age;}

    public Integer getAge(){return age;}
    public void setName(String name ){this.name = name;}

    public String getName(){return name;}

}
