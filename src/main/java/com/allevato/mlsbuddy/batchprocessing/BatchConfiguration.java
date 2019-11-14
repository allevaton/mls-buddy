package com.allevato.mlsbuddy.batchprocessing;

import com.allevato.mlsbuddy.scraper.data.Property;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
  @Autowired public JobBuilderFactory jobBuilderFactory;
  @Autowired public StepBuilderFactory stepBuilderFactory;

  @Bean
  PropertyListReader reader() {
    return new PropertyListReader();
  }

  @Bean
  public Job fetchAllPropertiesJob(NewPropertiesFetchedListener listener, Step step1) {
    return jobBuilderFactory
        .get("fetchAllPropertiesJob")
        .incrementer(new RunIdIncrementer())
        .listener(listener)
        .flow(step1)
        .end()
        .build();
  }

  @Bean
  public Step step1() {
    return stepBuilderFactory.get("step1")
        .reader(reader())
        .build();
  }
}
