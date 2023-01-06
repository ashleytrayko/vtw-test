package com.example.vtw.service;

import com.example.vtw.configuration.PrincipalDetail;
import com.example.vtw.domain.RoleType;
import com.example.vtw.domain.User;
import com.example.vtw.dto.UserDTO;
import com.example.vtw.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Bean
    public BCryptPasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }


    // 읽기만 하는 데이터이므로 ReadOnly해줌

    @Transactional(readOnly = true)
    public User findUser(String username){
        User user = userRepository.findByUsername(username).orElseGet(()-> new User());
        return user;
    }

    @Transactional
    public String join(UserDTO userDTO){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = userDTO.getPassword();
        String encodedPassword = encoder.encode(password);
        User user = User.builder()
                                .username(userDTO.getUsername())
                                .password(encodedPassword)
                                .role(RoleType.USER)
                                .build();
        User returnValue = userRepository.saveAndFlush(user);
        if(returnValue != null){
            return "success";
        }else{
            return "fail";
        }
    }
    @Transactional
    public String resign(PrincipalDetail principal) {
        int returnValue = userRepository.customDeleteById(principal.getUser().getUserId());
        if(returnValue >= 1){
            return "success";
        } else {
            return "fail";
        }
    }

    @Transactional
    public String updateUser(UserDTO userDTO) {
        User user = userRepository.findById(userDTO.getUserId())
                .orElseThrow(()->{
                    return new IllegalArgumentException("불러올 유저가 없습니다.");
                });
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String newPassword = encoder.encode(userDTO.getPassword());
        user.setPassword(newPassword);
        User returnValue = userRepository.saveAndFlush(user);
        if(returnValue != null){
            return "success";
        } else {
            return "fail";
        }
    }
}
