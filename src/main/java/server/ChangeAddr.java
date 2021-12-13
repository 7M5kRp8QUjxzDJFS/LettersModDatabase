package server;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * ChangeAddr dummy endpoint
 *
 */
public class ChangeAddr implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    System.out.println("changeaddr req receieved: " + request.headers());
    System.out.println("changeaddr req body receieved: " + request.body());

    Gson gson = new Gson();
    JSONObject data = new JSONObject(request.body());
    int oldaddr = data.getInt("oldaddr");

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

    // Get previous TrueKey
    Address oldAddress = db.getAddressByAddress(Integer.toString(oldaddr)).get(0);
    String trueKey = Integer.toString(oldAddress.getTrueKey());

    // Make new address, checking that it isn't already in use or the old one
    String newAddr = newAddressMaker();
    while (db.getAddressByAddress(newAddr).size() != 0 || newAddr.equals(Integer.toString(oldaddr))) {
      newAddr = newAddressMaker();
    }

    // Update database
    db.changeAddress(trueKey, newAddr);
    System.out.println("Address with TrueKey: " + trueKey + " set to " + newAddr);

    // Return the new address to be displayed to the user
    Map<String, String> variables = ImmutableMap.of("newaddr", newAddr);

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
