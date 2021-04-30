package voteit.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.VoteitDB;
import voteit.libs.json.JsonArray;
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

public class PollHandler {

  public static void addHandlers(ServerHttp server) {
    Handler get = (Context context) -> {
      Response res = new Response(500);
      try {
        ResultSet rs = VoteitDB.getPolls();
        JsonArray polls = new JsonArray();
        while (rs.next()) {
          JsonObject poll = new JsonObject();
          poll.put("pollId", rs.getInt("pollId"));
          poll.put("place", rs.getString("place"));
          poll.put("pollBegin", rs.getTimestamp("pollBegin").getTime());
          poll.put("date", rs.getTimestamp("date").getTime());
          poll.put("pollEnd", rs.getTimestamp("pollEnd").getTime());
          polls.add(poll);
        }
        res = new Response(polls);
      } catch (SQLException e) {
        res = new Response(500);
      }
      return res;
    };

    Handler delete = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject body = JsonParser.parseObject(context.requestData);
        int amount = VoteitDB.deletePoll(body.getInteger("id"));
        if (amount == 0) {
          res = new Response(404);
        } else {
          res = new Response(200);
        }
      } catch (JsonFormattingException | UnsupportedTypeException | WrongTypeException | KeyNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      }
      return res;
    };

    Handler create = (Context context) -> {
      Response res = new Response(500);
      try {
        new AuthUser(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY));
        JsonObject body = JsonParser.parseObject(context.requestData);
        VoteitDB.addPoll(body);
        res = new Response(200);
      } catch (JsonFormattingException | UnsupportedTypeException | KeyNotFoundException | WrongTypeException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      }
      return res;
    };

    Handler edit = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject body = JsonParser.parseObject(context.requestData);
        int amount = VoteitDB.updatePoll(body);
        if (amount == 0) {
          res = new Response(404);
        } else {
          res = new Response(200);
        }
      } catch (JsonFormattingException | UnsupportedTypeException | WrongTypeException | KeyNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      }
      return res;
    };

    server.addRoute("/api/poll/get", get);
    server.addRoute("/api/poll/create", create);
    server.addRoute("/api/poll/edit", edit);
    server.addRoute("/api/poll/delete", delete);
  }
}
