package com.stronans.hydromodelweb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {
    /**
     * The <code>Logger</code> to be used.
     */
    private static Logger log = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        log.info("Starting Hydro Model..");
        SpringApplication.run(Application.class, args);
    }
}
