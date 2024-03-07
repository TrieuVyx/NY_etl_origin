package com.example.excelwithcode.controller;

import com.example.excelwithcode.service.EtlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EtlController {

    private final EtlService etlService;
    @Autowired
    public  EtlController(EtlService etlService){
        this.etlService = etlService;
    }

    @GetMapping("")
    public String GenderInterface(){
        return "index";
    }
}
