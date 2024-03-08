package com.example.excelwithcode.service;


import com.example.excelwithcode.model.EtlModel;
import com.example.excelwithcode.repository.EtlRepository;
import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class EtlService {
    private static  EtlRepository etlRepository ;

    public EtlService(EtlRepository etlRepository) {
        this.etlRepository = etlRepository;
    }

    public void batchUpdate(String sql, String s) {
    }
    public List<EtlModel> getAllEtl(){
        return etlRepository.findAll();
    }
    public static void exportToCSV(EtlModel etlModels, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {
            List<EtlModel> etlModelList = etlRepository.findAll();
            String[] header = {"ID", "Name", "Age", "Email"};
            writer.writeNext(header);

            for (EtlModel etlModel : etlModelList) {
                String[] data = {etlModel.getName(), String.valueOf(etlModel.getId()), etlModel.getEmail(), String.valueOf(etlModel.getAge())};
                writer.writeNext(data);
            }


            System.out.println("CSV file exported successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
