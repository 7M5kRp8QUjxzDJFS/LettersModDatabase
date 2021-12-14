package server;

import spark.Request;
import spark.Response;
import spark.Route;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.json.JSONObject;

public class TakeMail implements Route {
  @Override
  public String handle(Request request, Response response) throws Exception {
    Gson gson = new Gson();
	LettersDatabase lettersORM = new LettersDatabase("./mailDatabase.sqlite3");
    JSONObject data = new JSONObject(request.body());
    int id = data.getInt("id");
	lettersORM.deleteParcel(id);
    ImmutableMap<String, String> variables = ImmutableMap.of("status", "200");
    return gson.toJson(variables);
  }
}
