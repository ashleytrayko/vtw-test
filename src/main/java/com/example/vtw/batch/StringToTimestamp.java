package com.example.vtw.batch;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StringToTimestamp {
    // CSV에서 읽어온 데이터는 String이기 때문에 Timestamp로 변환해줌
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
