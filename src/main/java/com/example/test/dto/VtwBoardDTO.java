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
public class VtwBoardDTO {
    private long boardNo;
    private String subject;
    private String contents;
    private VtwUser vtwUser;
    private Timestamp writeTime;
    private Timestamp updateTime;
}
