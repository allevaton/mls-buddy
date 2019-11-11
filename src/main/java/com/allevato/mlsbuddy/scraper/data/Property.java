package com.allevato.mlsbuddy.scraper.data;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Builder
@Data
public class Property {
  private Integer id;
  private Double price;
  private String shortAddress;
  private String shortDescription;
  private PropertyStatus status;
  private PropertyType type;
  private Date receivedAt;

  public String getLink() {
    return String.format("https://vow.mlspin.com/clients/report.aspx?mls=%d", id);
  }
}
