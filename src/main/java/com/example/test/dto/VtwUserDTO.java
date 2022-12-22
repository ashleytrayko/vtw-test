package com.example.test.dto;

import com.example.test.domain.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VtwUserDTO {
    private long userId;
    private String username;
    private String password;
    private RoleType role;
    private Timestamp creationDate;
    private Timestamp updateDate;
}
