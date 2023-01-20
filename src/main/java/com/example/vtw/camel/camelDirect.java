package com.example.vtw.camel;

import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class camelDirect extends EndpointRouteBuilder {

    @Override
    public void configure() throws Exception {
        // file -> direct
        from(file("output").fileName("camelResult.txt"))
                .to(direct("toKafka"));

        // direct -> kafka
        from(direct("toKafka"))
                .to(kafka("stream-test").brokers("localhost:9092"));

        // kafka -> direct
        from(kafka("stream-test").brokers("localhost:9092"))
                .to(direct("fromKafka"));

        // direct -> file
        from(direct("fromKafka"))
                .log("${body}")
                .to(file("output").fileName("resultFromKafka.txt").fileExist("Append").appendChars("\n-----------------------------------------------------\n" +
                        ""));

    }
}
