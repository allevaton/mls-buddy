package com.allevato.mlsbuddy.scraper.data;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum PropertyStatus {
  NEW("New"),
  BOM("Back on Market"),
  ACT("Active"),
  PCG("Price Change"),
  OFF("Off Market"),
  CTG("Contingent"),
  RAC("Reactivated");

  @Getter private String status;
}
