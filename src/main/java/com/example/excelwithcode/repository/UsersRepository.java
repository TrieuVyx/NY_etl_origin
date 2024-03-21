package com.example.excelwithcode.repository;


import com.example.excelwithcode.model.UsersModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends JpaRepository<UsersModel,Long> {
    UsersModel findByUsername(String username);
    UsersModel findByEmail(String email);
    UsersModel findFirstByEmail(String email);

}
