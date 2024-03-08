package com.example.excelwithcode.controller;

import com.example.excelwithcode.service.EtlService;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class EtlController {

    private final EtlService etlService;
    @Autowired
    public  EtlController(EtlService etlService){
        this.etlService = etlService;
    }

    @GetMapping("")
    public String GenderInterface(){
        return "Etl/index";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
        try {
            InputStream inputStream = file.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
            CSVReader csvReader = new CSVReader(inputStreamReader);
            List<String[]> rows = csvReader.readAll();

            model.addAttribute("rows", rows);

            csvReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (Exception e) {
            return e.getMessage();
        }

        return "Etl/index";
    }
}
