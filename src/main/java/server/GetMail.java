package server;

import java.util.ArrayList;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * A class that handles get mail requests.
 */
public class GetMail implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    System.out.println("getmail req receieved: " + request.headers());
    System.out.println("getmail req body receieved: " + request.body());

    Gson gson = new Gson();
    JSONObject data = new JSONObject(request.body());
    String address = data.getString("address");
    int maxNumPackages = data.getInt("maxNumParcels");

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


    int recipientId = db.getAddressByAddress(address).get(0).getTrueKey();
    ArrayList<Parcel> undownloadedParcels = db.getUndownloadedParcelsByRecipient(recipientId);
    System.out.println("Undownloaded parcels retrieved.");

    // Store query results in JSONs 
    ArrayList<String> parcelJSONs = new ArrayList<String>();
    for (int i = 0; i < undownloadedParcels.size(); i++) {
      if (i > maxNumPackages) {
        break;
      }
      
      Parcel currentParcel = undownloadedParcels.get(i); 
      JSONObject parcelJSON = new JSONObject();
      parcelJSON.put("id", currentParcel.getId());
      parcelJSON.put("recipient", currentParcel.getRecipient());
      parcelJSON.put("sender", currentParcel.getSender());
      parcelJSON.put("parcelString", currentParcel.getParcel());

      parcelJSONs.add(parcelJSON.toString());
    }

    Map<String, String[]> variables = ImmutableMap.of("parcels",
        parcelJSONs.toArray(new String[0]));
    System.out.println(parcelJSONs.size() + " parcels sent to user.");

    return gson.toJson(variables);
  }

}
