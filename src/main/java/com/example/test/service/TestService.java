package com.example.test.service;

import com.example.test.domain.RoleType;
import com.example.test.domain.TestUser;
import com.example.test.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public TestUser findUser(String username){
        TestUser user = testRepository.findByUsername(username).orElseGet(()->{
            return new TestUser();
        });
        return user;
    }

    @Transactional
    public String join(TestUser user){
        String password = user.getPassword();
        String encodedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setRole(RoleType.USER);
        testRepository.save(user);
        return "ok";
    }


}
