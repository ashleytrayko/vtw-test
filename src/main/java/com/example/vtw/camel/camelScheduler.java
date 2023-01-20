package com.example.vtw.camel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

//@Component
public class camelScheduler extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("quartz://myGroup/myTimerName?cron=0/5+*+*+*+*+?")
                .setBody(constant("Test"))
                .log("${body}")
                .to("log:info");
    }
}
