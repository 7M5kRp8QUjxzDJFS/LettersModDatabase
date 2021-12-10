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


    // what if we store each way as a Way object with id, lat, and lon
    // getWays returns a lst = List<Way>
    // and then ImmutableMap.of("ways", lst)
    Map<String, String> variables = ImmutableMap.of("newaddr", Integer.toString(oldaddr*3));

    return gson.toJson(variables);
  }

}
