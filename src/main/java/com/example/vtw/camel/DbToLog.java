package com.example.vtw.camel;

import com.example.vtw.domain.User;
import com.example.vtw.dto.UserDTO;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class DbToLog extends EndpointRouteBuilder {
    @Override
    public void configure() throws Exception {

//         Routebuilder version
//        from("jpa:java.util.List?scheduler=spring&scheduler.cron=0/10+*+*+*+*+*&resultClass=com.example.vtw.domain.User&nativeQuery=SELECT * FROM user&consumeDelete=false")
//                .log("${body.userId}, ${body.username}, ${body.password}, ${body.role}, ${body.creationDate}, ${body.updateDate}")
//                .transform()
//                .simple("${body.userId}, ${body.username}, ${body.password}, ${body.role}, ${body.creationDate}, ${body.updateDate}")
//                .to("file:output/?fileName=camelResult.txt&fileExist=Append&appendChars=\\n");

//         EndpointRouteBuilder
        from(jpa("java.util.List").scheduler("spring").schedulerProperties("cron","0/10+*+*+*+*+*").resultClass("com.example.vtw.domain.User").nativeQuery("select * from user").consumeDelete(false))
                .log("${body.userId}, ${body.username}, ${body.password}, ${body.role}, ${body.creationDate}, ${body.updateDate}")
                .transform()
                .simple("${body.userId}, ${body.username}, ${body.password}, ${body.role}, ${body.creationDate}, ${body.updateDate}")
                .to(file("output?fileName=camelResult.txt&fileExist=Append&appendChars=\\n"))
                .stop();
    }
}
