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
 * ChangeAddr dummy endpoint
 *
 */
public class GetMail implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    System.out.println("changeaddr req receieved: " + request.headers());
    System.out.println("changeaddr req body receieved: " + request.body());

    Gson gson = new Gson();
    JSONObject data = new JSONObject(request.body());
    String address = data.getString("address");
    int maxNumPackages = data.getInt("maxNumParcels");

    // set up some dummy parcels
    JSONObject parcel1 = new JSONObject();
    parcel1.put("id", "1");
    parcel1.put("recipient", address);
    parcel1.put("sender","24601");

    JSONObject parcel2 = new JSONObject();
    parcel2.put("id", "2");
    parcel2.put("recipient", address);
    parcel2.put("sender","69420");

    JSONObject parcel3 = new JSONObject();
    parcel3.put("id", "3");
    parcel3.put("recipient", address);
    parcel3.put("sender","007");

    JSONObject parcel4 = new JSONObject();
    parcel4.put("id", "4");
    parcel4.put("recipient", address);
    parcel4.put("sender","1234");

    JSONObject parcel5 = new JSONObject();
    parcel5.put("id", "5");
    parcel5.put("recipient", address);
    parcel5.put("sender","666");


    // what if we store each way as a Way object with id, lat, and lon
    // getWays returns a lst = List<Way>
    // and then ImmutableMap.of("ways", lst)
    Map<String, String[]> variables = ImmutableMap.of("parcels",
        new String[] {parcel4.toString(), parcel5.toString()});

    return gson.toJson(variables);
  }

}