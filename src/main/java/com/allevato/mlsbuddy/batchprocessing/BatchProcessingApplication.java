package com.allevato.mlsbuddy.batchprocessing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class BatchProcessingApplication {

  public static void main(String[] args) throws Exception {
    System.setProperty("spring.devtools.restart.enabled", "true");
    SpringApplication.run(BatchProcessingApplication.class, args);
  }
}
