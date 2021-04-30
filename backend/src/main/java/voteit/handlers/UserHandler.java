package voteit.handlers;

import java.sql.SQLException;

import voteit.VoteitDB;
import voteit.libs.json.JsonFormattingException;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.json.KeyNotFoundException;
import voteit.libs.json.UnsupportedTypeException;
import voteit.libs.json.WrongTypeException;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.Response;
import voteit.libs.serverhttp.ServerHttp;
import voteit.modules.AuthUser;
import voteit.modules.Constants;
import voteit.modules.LoginNotFoundException;

public class UserHandler {
  public static void addHandlers(ServerHttp server) {

    Handler register = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject body = JsonParser.parseObject(context.requestData);
        VoteitDB.addUser(body);
        res = new Response(200);
      } catch (JsonFormattingException | UnsupportedTypeException | WrongTypeException | KeyNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(501);
      }
      return res;
    };

    Handler delete = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject body = JsonParser.parseObject(context.requestData);
        VoteitDB.deleteUser(body.getInteger("id"));
      } catch (JsonFormattingException | UnsupportedTypeException | WrongTypeException | KeyNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      }
      return res;
    };

    Handler login = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject body = JsonParser.parseObject(context.requestData);
        int token = VoteitDB.createToken(body.getString("name"), body.getString("password"));
        return new Response(200).addCookie(Constants.LOGINTOKENCOOKIEKEY, Integer.toString(token));

      } catch (JsonFormattingException | UnsupportedTypeException | WrongTypeException | KeyNotFoundException e) {
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
        AuthUser authUser = new AuthUser(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY));
        JsonObject user = new JsonObject();
        user.put("id", authUser.getId());
        user.put("name", authUser.getName());
        res = new Response(user);
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
    server.addRoute("/api/user/delete", delete);
    server.addRoute("/api/user/register", register);
    server.addRoute("/api/user/data", data);
  }

}
