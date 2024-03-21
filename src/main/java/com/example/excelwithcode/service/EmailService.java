package com.example.excelwithcode.service;

import com.example.excelwithcode.model.EmailModel;
import com.example.excelwithcode.repository.EmailRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailService {

    private final EmailRepository emailRepository;
    public EmailService(EmailRepository emailRepository){
        this.emailRepository = emailRepository;
    }

    public List<EmailModel> getAll(){
        return emailRepository.findAll();
    }


}
