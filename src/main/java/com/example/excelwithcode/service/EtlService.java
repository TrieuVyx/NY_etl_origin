package com.example.excelwithcode.service;


import com.example.excelwithcode.model.EtlModel;
import com.example.excelwithcode.repository.EtlRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EtlService {
    private final EtlRepository etlRepository;

    public EtlService(EtlRepository etlRepository) {
        this.etlRepository = etlRepository;
    }

    public void batchUpdate(String sql, String s) {
    }
    public List<EtlModel> getAllEtl(){
        return etlRepository.findAll();
    }

}
