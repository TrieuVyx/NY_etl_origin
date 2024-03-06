package com.example.excelwithcode.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ProductsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String product_name;
    private Float price;
    private Number quality;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }


    public void setProduct_name(String product_name){
        this.product_name = product_name;
    }
    public String getProduct_name(){
        return product_name;
    }
    public void setPrice(Float price){
        this.price = price;
    }

    public Float getPrice(){
        return price;
    }

    public Number getQuality(){
        return quality;
    }
    public  void setQuality(Number quality){
        this.quality = quality;
    }
}
