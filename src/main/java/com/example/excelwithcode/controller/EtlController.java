package com.example.excelwithcode.controller;

import com.example.excelwithcode.model.EtlModel;
import com.example.excelwithcode.service.EtlService;
import com.opencsv.CSVReader;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.logging.Logger;
@Controller
@PersistenceContext
@Transactional
public class EtlController {
    @Autowired
    private final EtlService etlService;
    private JdbcOperations jdbcTemplate;
    private EntityManager entityManager;
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/DbEtl";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "0837441290Aa@";
    private static final Logger logger = Logger.getLogger(EtlController.class.getName());

    public EtlController(EtlService etlService) {
        this.etlService = etlService;
    }

    @GetMapping("etl/index")
    public String GenderInterface() {
        return "Etl/index";
    }

    @GetMapping("etl/create")
    public String CreateInterface() {
        return "Etl/create";
    }

    @GetMapping("etl/upload")
    public String UploadFile() {
        return "Etl/upload";
    }

    @GetMapping("etl/read")
    public String ReadFileDatabase(Model model) {
        List<EtlModel> etlModelList = etlService.getAllEtl();
        model.addAttribute("etlModelList", etlModelList);
        return "Etl/read";
    }
    @GetMapping("etl/export")
    public String ExportFile() {
        return "Etl/export";
    }
    @PostMapping("etl/create")
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
        return "Etl/create";
    }

    @PostMapping("/etl/upload")
    public String importCSV(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "Etl/upload";
        }
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            String sql = "INSERT INTO etl_model (id, name, age, email) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            // Bỏ qua dòng tiêu đề trong tệp tin CSV (nếu có)
            reader.readLine();
            // Đọc từng dòng trong tệp tin CSV và thêm vào cơ sở dữ liệu
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                long id = Long.parseLong(data[0]);
                String name = data[1];
                int age = Integer.parseInt(data[2]);
                String email = data[3];
                statement.setLong(1, id);
                statement.setString(2, name);
                statement.setInt(3, age);
                statement.setString(4, email);
                // Thực thi truy vấn
                statement.executeUpdate();
            }
            System.out.println("Import data from CSV to PostgreSQL successful.");
        } catch (Exception e) {
            return e.getMessage();
        }

        return "/Etl/upload";
    }


    @PostMapping("etl/export")
   public void exportCSVFile(){
        EtlModel etlModel = new EtlModel();

        String filePath = "E:/output.csv";
        EtlService.exportToCSV(etlModel, filePath);
    }

}






