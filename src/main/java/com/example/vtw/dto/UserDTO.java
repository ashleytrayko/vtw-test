package com.example.vtw.dto;

import com.example.vtw.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private long userId;
    private String username;
    private String password;
    private RoleType role;
    private Timestamp creationDate;
    private Timestamp updateDate;
}
