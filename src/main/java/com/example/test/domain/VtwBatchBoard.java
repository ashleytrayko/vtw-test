package com.example.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VtwBatchBoard {

    @Lob
    @Column(nullable = false)
    private String contents;


    @Column(nullable = false)
    private String username;

    @Id
    private Timestamp creationDate;
}
