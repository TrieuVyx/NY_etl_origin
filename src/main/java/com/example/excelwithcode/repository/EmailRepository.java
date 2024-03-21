package com.example.excelwithcode.repository;

import com.example.excelwithcode.model.EmailModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<EmailModel,Long> {
}
