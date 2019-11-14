package com.allevato.mlsbuddy.scraper;

import java.io.IOException;

public class Application {

  public static void main(String[] args) throws IOException {
    var scraper = new Scraper();
    scraper.scrapeAllProperties();
  }
}
