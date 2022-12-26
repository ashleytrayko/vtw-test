package com.example.test.service;

import com.example.test.configuration.PrincipalDetail;
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
    public void join(VtwUserDTO vtwUserDTO){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = vtwUserDTO.getPassword();
        String encodedPassword = encoder.encode(password);
        VtwUser vtwUser = VtwUser.builder()
                                .username(vtwUserDTO.getUsername())
                                .password(encodedPassword)
                                .role(RoleType.USER)
                                .build();
        vtwUserRepository.save(vtwUser);
    }
    @Transactional
    public void resign(PrincipalDetail principal) {
        System.out.println(principal.getUser());
        vtwUserRepository.deleteById(principal.getUser().getUserId());
    }

    public void updateUser(VtwUserDTO vtwUserDTO) {
        VtwUser vtwUser = vtwUserRepository.findById(vtwUserDTO.getUserId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("불러올 유저가 없습니다.");
                });
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = encoder.encode(vtwUserDTO.getPassword());
        vtwUser.setPassword(newPassword);
        vtwUserRepository.save(vtwUser);
    }
}
