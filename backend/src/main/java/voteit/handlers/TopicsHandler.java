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
import voteit.resources.DataStructureFactory;

public class TopicsHandler {
  public static void addHandlers(ServerHttp server) {
    Handler submit = (Context context) -> {
      Response res = new Response(500);
      try {
        AuthUser user = new AuthUser(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY));
        JsonObject poll = JsonParser.parseObject(context.requestData);
        ResultSet pollRs = VoteitDB.getPoll(poll.getInteger("pollId"));
        if (!pollRs.next()) {
          res = new Response(404);
        } else {
          int pollId = pollRs.getInt("pollId");
          VoteitDB.addTopic(poll, user.getId(), pollId);
          res = new Response(200);
        }
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      } catch (UnsupportedTypeException | JsonFormattingException | WrongTypeException | KeyNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(400);
      }
      return res;
    };
  }

  Handler get = (Context context) -> {
    Response res = new Response(500);
    JsonObject req;
    try {
      req = JsonParser.parseObject(context.requestData);
      ResultSet topicsRs = VoteitDB.getTopics(req.getInteger("pollId"));
      JsonArray topics = new JsonArray();
      while (topicsRs.next()) {
        topics.add(DataStructureFactory.buildTopic(topicsRs));
      }
      res = new Response(topics);
    } catch (JsonFormattingException | UnsupportedTypeException | KeyNotFoundException | WrongTypeException e) {
      res = new Response(e.getMessage()).setStatus(400);
    } catch (SQLException e) {
      res = new Response(500);
    }

    return res;
  };
}
