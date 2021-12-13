package server;
import spark.Request;
import spark.Response;
import spark.Route;
import com.google.common.collect.ImmutableMap;

import com.google.gson.Gson;

import org.json.JSONObject;

public class Reset implements Route {
  @Override
  public String handle(Request request, Response response) throws Exception {
	LettersDatabase lettersORM = new LettersDatabase("./mailDatabase.sqlite3");
    Gson gson = new Gson();
    JSONObject data = new JSONObject(request.body());
    String address = data.getString("address");
    String trueKey = data.getString("trueKey");
	lettersORM.changeAddress(trueKey, address);
    ImmutableMap<String, String> variables = ImmutableMap.of("status", "200");
    return gson.toJson(variables);
  }
}
