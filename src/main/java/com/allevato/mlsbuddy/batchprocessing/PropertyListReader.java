package com.allevato.mlsbuddy.batchprocessing;

import com.allevato.mlsbuddy.scraper.Application;
import com.allevato.mlsbuddy.scraper.data.Property;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

@Slf4j
public class PropertyListReader implements ItemReader<List<Property>> {

  @Override
  public List<Property> read()
      throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
    var scrapedProperties = Application.scrapeAllProperties();

    return scrapedProperties;
  }
}
