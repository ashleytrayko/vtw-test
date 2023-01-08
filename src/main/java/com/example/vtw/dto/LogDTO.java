package com.example.vtw.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogDTO implements Serializable {
    private Long logNumber;
    private String contents;
    private String user;
    private String creationDate;
}
