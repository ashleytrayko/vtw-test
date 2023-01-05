package com.example.test.dto;

import com.example.test.domain.VtwUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VtwBatchDTO {
    private String contents;
    private String vtwUser;
    private String creationDate;
}
