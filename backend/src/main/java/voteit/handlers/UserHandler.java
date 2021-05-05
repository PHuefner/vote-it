package voteit.handlers;

import java.sql.SQLException;

import voteit.VoteitDB;
import voteit.libs.json.JsonException;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.Response;
import voteit.libs.serverhttp.ServerHttp;
import voteit.models.User;
import voteit.modules.Constants;
import voteit.modules.exceptions.InvalidFormatException;
import voteit.modules.exceptions.LoginNotFoundException;

public class UserHandler {
  public static void addHandlers(ServerHttp server) {

    Handler register = (Context context) -> {
      Response res = new Response(500);

      try {
        JsonObject req = JsonParser.parseObject(context.requestData);
        String name = req.getString("name");
        String password = req.getString("password");

        User.register(name, password, false);
        User newUser = new User(name, password);
        res = new Response(200).addCookie(Constants.LOGINTOKENCOOKIEKEY, Integer.toString(newUser.getToken()));
      } catch (JsonException | InvalidFormatException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        // Shouldnt happen
        res = new Response(e.getMessage()).setStatus(401);
      }

      return res;
    };

    /*
     * Handler delete = (Context context) -> { Response res = new Response(500); try
     * { JsonObject body = JsonParser.parseObject(context.requestData);
     * VoteitDB.deleteUser(body.getInteger("id")); } catch (JsonException e) { res =
     * new Response(e.getMessage()).setStatus(400); } catch (SQLException e) { res =
     * new Response(500); } return res; };
     */

    Handler login = (Context context) -> {
      Response res = new Response(500);

      try {
        JsonObject req = JsonParser.parseObject(context.requestData);
        String name = req.getString("name");
        String password = req.getString("password");

        User user = new User(name, password);
        res = new Response(200).addCookie(Constants.LOGINTOKENCOOKIEKEY, Integer.toString(user.getToken()));

      } catch (JsonException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      }

      return res;
    };

    Handler data = (Context context) -> {
      Response res = new Response(500);

      try {
        User user = new User(Integer.parseInt(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY)));
        JsonObject userJson = new JsonObject();
        userJson.put("userId", user.getUserId());
        userJson.put("name", user.getName());
        userJson.put("admin", user.getAdmin());
        res = new Response(userJson);
      } catch (NumberFormatException e) {
        res = new Response("token syntax invalid").setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      }
      return res;
    };

    Handler logout = (Context context) -> {
      Response res = new Response(500);

      try {
        int token = Integer.parseInt(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY));
        new User(token);
        VoteitDB.deleteToken(token);
        res = new Response(200);
      } catch (NumberFormatException e) {
        res = new Response("token syntax invalid").setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      }
      return res;
    };

    server.addRoute("/api/user/login", login);
    // server.addRoute("/api/user/delete", delete);
    server.addRoute("/api/user/register", register);
    server.addRoute("/api/user/data", data);
    server.addRoute("/api/user/logout", logout);
  }

}
