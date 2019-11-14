package com.allevato.mlsbuddy.batchprocessing;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
  @Autowired public JobBuilderFactory jobBuilderFactory;

  @Bean
  PropertyListReader reader() {
    return new PropertyListReader();
  }
}
