package com.example.vtw.dto;

import com.example.vtw.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private long boardNo;
    private String subject;
    private String contents;
    private User user;
    private Timestamp creationDate;
    private Timestamp updateDate;

}
