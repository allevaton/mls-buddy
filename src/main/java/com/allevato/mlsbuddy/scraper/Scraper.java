package com.allevato.mlsbuddy.scraper;

import com.allevato.mlsbuddy.scraper.data.Property;
import com.allevato.mlsbuddy.scraper.data.PropertyStatus;
import com.allevato.mlsbuddy.scraper.data.PropertyType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Scraper {
  private String authenticate() throws IOException {
    var startUrl = "https://vow.mlspin.com/clients/validate.aspx?id=";
    String mlsUsername = System.getenv("MLS_USERNAME");
    String mlsPassword = System.getenv("MLS_PASSWORD");

    if (mlsUsername == null || mlsUsername.isEmpty()) {
      throw new RuntimeException("MLS_USERNAME environmental variable not provided");
    }

    if (mlsPassword == null || mlsPassword.isEmpty()) {
      throw new RuntimeException("MLS_PASSWORD environmental variable not provided");
    }

    var connection =
        Jsoup.connect(startUrl)
            .requestBody(String.format("user=%s&pass=%s&signin=Sign+In", mlsUsername, mlsPassword));

    var doc = connection.post();
    var response = connection.response();
    return response.cookies().get("ASP.NET_SessionId");
  }

  private Document getDocument(int pageNumber) throws IOException {
    var sessionId = authenticate();

    var base = "https://vow.mlspin.com/clients/index.aspx?hpr=y";

    var authenticatedUrl = pageNumber == 0 ? base : String.format("%s&p=%d", base, pageNumber);
    System.out.println(authenticatedUrl);

    var connection =
        Jsoup.connect(authenticatedUrl)
            .cookie("ASP.NET_SessionId", sessionId)
            .followRedirects(true);

    return connection.get();
  }

  public List<Property> scrapeAllProperties() throws IOException {
    var doc = getDocument(0);

    var pageCount = doc.select("select").get(0).children().size();

    var properties = new ArrayList<Property>();

    for (int page = 0; page < pageCount; page++) {
      var mainTable = doc.select("table").get(7);

      for (var tr : mainTable.select("tr")) {
        // Ignore header...
        // TODO: Find a better, potentially more reliable method of detecting the table header
        if (!tr.hasAttr("bgcolor")) {
          continue;
        }

        var builder = Property.builder();

        try {
          /*
          id - 4, span with the id
          Type - 5
          Status - 7
          Address - 9
          Description - 11
          Price - 13
          Added date - 15, "Oct 28"
          Added time - 16, "7:18 PM"
           */

          var td = tr.select("td");

          var id = Integer.valueOf(td.get(4).select("span").attr("id"));
          builder.id(id);

          var type = td.get(5).text().strip();
          builder.type(PropertyType.valueOf(type));

          var status = td.get(7).text().strip();
          builder.status(PropertyStatus.valueOf(status));

          builder.shortAddress(td.get(9).text().strip());
          builder.shortDescription(td.get(11).text().strip());

          var price = Double.valueOf(td.get(13).text().strip().replaceAll("\\D", ""));
          builder.price(price);

          var addedDate = td.get(15).text().strip();
          var addedTime = td.get(16).text().strip();

          try {
            var format = new SimpleDateFormat("yyyy MMM dd hh:mm a", Locale.ENGLISH);
            var date = format.parse(String.format("2019 %s %s", addedDate, addedTime));
            System.out.println(date);
            builder.receivedAt(date);
          } catch (ParseException e) {
            e.printStackTrace();
          }

          Property builtProperty = builder.build();
          properties.add(builtProperty);

          System.out.println(builtProperty);
        } catch (IllegalArgumentException e) {
          System.err.println("Failed to process entry");
          System.err.format("Current builder state: %s", builder.build());
          e.printStackTrace();
        }
      }

      doc = getDocument(page + 1);
    }

    return properties;
  }
}
