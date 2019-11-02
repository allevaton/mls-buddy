package com.allevato.mls.scraper;

import com.allevato.mls.scraper.data.Property;
import com.allevato.mls.scraper.data.PropertyStatus;
import com.allevato.mls.scraper.data.PropertyType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Application {
  private static String authenticate() throws IOException {
    var startUrl = "https://vow.mlspin.com/clients/validate.aspx?id=";
    var connection =
        Jsoup.connect(startUrl)
            .requestBody(
                String.format(
                    "user=%s&pass=%s&signin=Sign+In",
                    System.getenv("MLS_USERNAME"), System.getenv("MLS_PASSWORD")));

    var doc = connection.post();
    var response = connection.response();
    return response.cookies().get("ASP.NET_SessionId");
  }

  private static Document getDocument(int pageNumber) throws IOException {
    var sessionId = Application.authenticate();

    var base = "https://vow.mlspin.com/clients/index.aspx?hpr=y";

    var authenticatedUrl = pageNumber == 0 ? base : String.format("%s&p=%d", base, pageNumber);
    System.out.println(authenticatedUrl);

    var connection =
        Jsoup.connect(authenticatedUrl)
            .cookie("ASP.NET_SessionId", sessionId)
            .followRedirects(true);

    return connection.get();
  }

  public static void main(String[] args) throws IOException {
    var doc = getDocument(0);

    var pageCount = doc.select("select").get(0).children().size();

    for (int page = 0; page < pageCount - 1; page++) {
      var mainTable = doc.select("table").get(7);

      for (var tr : mainTable.select("tr")) {
        if (!tr.hasAttr("bgcolor")) {
          continue;
        }

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

        var builder = Property.builder();
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

        System.out.println(builder.build());
      }

      doc = getDocument(page + 1);
    }
  }
}
