package server;

import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * A class that handles initialize address requests.
 */
public class InitAddr implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    System.out.println("initaddr req receieved: " + request.headers());
    System.out.println("initaddr req body receieved: " + request.body());

    // Connect to database
    LettersDatabase db;
    if (System.getenv("JDBC_DATABASE_URL") != null) {
      db = new LettersDatabase(System.getenv("JDBC_DATABASE_URL"));
      System.out.println("Heroku Postgresql database connected!");
    } else {
      // Don't think this works if you don't have postgres, so change it to connect to the
      // local sqlite3 DB instead if necessary.
      db = new LettersDatabase("jdbc:postgresql://localhost:5432/addresses");
      System.out.println("Local database connected!");
    }

    // Make new address not already in the database
    String newAddr = newAddressMaker();
    while (db.getAddressByAddress(newAddr).size() != 0) {
      newAddr = newAddressMaker();
    }

    // Add new address to database
    db.insertAddress(newAddr);
    System.out.println(newAddr + "  added to table.");

    // Return the new address to be displayed to the user
    Gson gson = new Gson();
    Map<String, String> variables = ImmutableMap.of("newaddr", newAddr);

    System.out.println("variables: " + gson.toJson(variables));

    return gson.toJson(variables);
  }

  /**
   * A helper method that creates a randomly generated 5-digit string of numbers.
   * @return - A string of 5 numbers.
   */
  private String newAddressMaker() {
    StringBuilder newAddr = new StringBuilder();
    for (int i = 0; i < 5; i++) {
      Double x = Math.random() * 10;
      int addrPart = x.intValue();
      newAddr.append(addrPart);
    }
    return newAddr.toString();
  }

}
