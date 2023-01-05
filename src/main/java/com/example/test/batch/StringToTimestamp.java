package com.example.test.batch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToTimestamp {
    public Timestamp convert(String input){
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(input);
            Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
            return timestamp;
        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }
        return null;
    }
}
