package server;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Route;

/**
 * InitAddr dummy endpoint
 *
 */
public class InitAddr implements Route {

  @Override
  public String handle(Request request, Response response) throws Exception {
    System.out.println("initaddr req receieved: " + request.headers());
    System.out.println("initaddr req body receieved: " + request.body());

    // what if we store each way as a Way object with id, lat, and lon
    // getWays returns a lst = List<Way>
    // and then ImmutableMap.of("ways", lst)
    Gson gson = new Gson();
    Map<String, String> variables = ImmutableMap.of("newaddr", "81");

    System.out.println("variables: " + gson.toJson(variables));

    return gson.toJson(variables);
  }

}
