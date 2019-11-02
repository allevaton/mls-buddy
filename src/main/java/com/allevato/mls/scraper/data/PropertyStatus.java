package com.allevato.mls.scraper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PropertyStatus {
  NEW("New"),
  BOM("Back on Market"),
  ACT("Active"),
  PCG("Price Change"),
  OFF("Off Market"),
  CTG("Contingent");

  @Getter private String status;
}
