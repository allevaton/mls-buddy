package com.allevato.mlsbuddy.batchprocessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
  public final JobBuilderFactory jobBuilderFactory;
  public final StepBuilderFactory stepBuilderFactory;

  public BatchConfiguration(
      JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
    this.jobBuilderFactory = jobBuilderFactory;
    this.stepBuilderFactory = stepBuilderFactory;
  }

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
  public Step step1(PropertyListReader reader) {
    return stepBuilderFactory.get("step1").chunk(1).reader(reader).build();
  }
}
