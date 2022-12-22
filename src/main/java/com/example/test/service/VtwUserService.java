package com.example.test.service;

import com.example.test.domain.RoleType;
import com.example.test.domain.VtwUser;
import com.example.test.dto.VtwUserDTO;
import com.example.test.repository.VtwUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VtwUserService {

    private final VtwUserRepository vtwUserRepository;

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }


    // 읽기만 하는 데이터이므로 ReadOnly해줌

    @Transactional(readOnly = true)
    public VtwUser findUser(String username){
        VtwUser user = vtwUserRepository.findByUsername(username).orElseGet(()->{
            return new VtwUser();
        });
        return user;
    }

    @Transactional
    public String join(VtwUserDTO vtwUserDTO){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = vtwUserDTO.getPassword();
        String encodedPassword = encoder.encode(password);
        VtwUser vtwUser = VtwUser.builder()
                .username(vtwUserDTO.getUsername())
                .password(encodedPassword)
                .role(RoleType.USER)
                .build();
        vtwUserRepository.save(vtwUser);
        return "ok";
    }

}
