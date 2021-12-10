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
public class Send implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    System.out.println("send req receieved: " + request.headers());
    System.out.println("send req body receieved: " + request.body());

    Gson gson = new Gson();
    JSONObject data = new JSONObject(request.body());
    String sender = data.getString("sender");
    String recipient = data.getString("recipient");
    String parcelString = data.getString("parcelString");


    // what if we store each way as a Way object with id, lat, and lon
    // getWays returns a lst = List<Way>
    // and then ImmutableMap.of("ways", lst)
    Map<String, String> variables = ImmutableMap.of("receivedParcel", parcelString);

    return gson.toJson(variables);
  }

}
