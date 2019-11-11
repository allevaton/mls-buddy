package com.allevato.mlsbuddy.scraper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PropertyType {
  SF("Single Family"),
  CC("Condo");

  @Getter private String type;
}
