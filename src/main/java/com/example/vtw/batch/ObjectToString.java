package com.example.vtw.batch;

import com.example.vtw.dto.LogDTO;
import org.springframework.core.convert.converter.Converter;

public class ObjectToString implements Converter<LogDTO, String> {


    @Override
    public String convert(LogDTO logDTO) {
        String returnValue = logDTO.toString();
        return returnValue;
    }
}
