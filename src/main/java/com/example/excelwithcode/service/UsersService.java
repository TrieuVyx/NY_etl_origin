package com.example.excelwithcode.service;


import com.example.excelwithcode.model.UsersModel;
import com.example.excelwithcode.repository.UsersRepository;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersService {
    private final UsersRepository usersRepository;
    public UsersService(UsersRepository usersRepository){
        this.usersRepository =usersRepository;
    }

    //all user
    public List<UsersModel> getAllUser(){
        return usersRepository.findAll();
    }

    // id
    public UsersModel getUserById(Long id){
        return usersRepository.findById(id).orElse(null);
    }
    //create
    public String setUser(UsersModel usersModel){
        usersRepository.save(usersModel);
        return null;
    }
    //delete
    public void deleteUser(Long id) throws NotFoundException {
        Optional<UsersModel> optionalUser = usersRepository.findById(id);
        if (optionalUser.isPresent()) {
            UsersModel user = optionalUser.get();
            usersRepository.delete(user);
        } else {
            throw new NotFoundException();
        }
    }
    //update
    public void updateUser(UsersModel usersModel){
        usersRepository.save(usersModel);
    }
    // login
//    public boolean authenticateUser(String email, String password) {
//        UsersModel user = usersRepository.findByEmail(email);
//        if (user != null) {
//            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//            return encoder.matches(password, user.getPassword());
//        }
//        return false;
//    }
    //checkemail exists
    public boolean EmailSame(String email){
        UsersModel EmailExist = usersRepository.findByEmail(email);
        return EmailExist == null;
    }
    // checkuser exists
    public boolean UsernameSame(String username){
        UsersModel UsernameExist = usersRepository.findByEmail(username);
        return UsernameExist == null;
    }


    //delete all

    public void DeleteAll(){
        usersRepository.deleteAll();
    }


    public UsersModel findByEmail(String email) {
        return usersRepository.findFirstByEmail(email);
    }
}
