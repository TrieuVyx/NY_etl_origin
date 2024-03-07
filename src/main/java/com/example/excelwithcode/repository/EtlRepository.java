package com.example.excelwithcode.repository;

import com.example.excelwithcode.model.EtlModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface EtlRepository extends JpaRepository<EtlModel,Long> {

}