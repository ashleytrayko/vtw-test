package com.example.test.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final PrincipalDetailService principalDetailService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(authentication == null){
            throw new InternalAuthenticationServiceException("인증정보가 없습니다.");
        }
        String username = authentication.getName();
        if(authentication.getCredentials() == null){
            throw new AuthenticationCredentialsNotFoundException("Credentials이 없습니다.");
        }
        String password = authentication.getCredentials().toString();
        try{
            PrincipalDetail loadedUser = (PrincipalDetail) principalDetailService.loadUserByUsername(username);
            if(loadedUser == null){
                throw new InternalAuthenticationServiceException("유저정보가 입력되지 않았습니다.");
            }

            if(!bCryptPasswordEncoder.matches(password, loadedUser.getPassword())){
                throw new BadCredentialsException("아이디나 비밀번호가 올바르지 않습니다.");
            }

            UsernamePasswordAuthenticationToken result = new UsernamePasswordAuthenticationToken(loadedUser, null, loadedUser.getAuthorities());
            result.setDetails(authentication.getDetails());
            return result;
        } catch (Exception e){
            e.printStackTrace();
        }
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
