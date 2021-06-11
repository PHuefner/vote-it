package voteit.handlers;

import java.sql.SQLException;
import java.util.ArrayList;

import voteit.VoteitDB;
import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonException;
import voteit.libs.json.JsonObject;
import voteit.libs.json.JsonParser;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.Response;
import voteit.libs.serverhttp.ServerHttp;
import voteit.models.Poll;
import voteit.models.User;
import voteit.modules.Constants;
import voteit.modules.exceptions.LoginNotFoundException;

public class PollHandler {

  public static void addHandlers(ServerHttp server) {
    Handler get = (Context context) -> {
      Response res = new Response(500);
      try {
        ArrayList<Poll> polls = Poll.getPolls();
        JsonArray pollsJson = new JsonArray();

        for (Poll poll : polls) {
          JsonObject pollJson = new JsonObject();
          pollJson.put("pollId", poll.getPollId());
          pollJson.put("place", poll.getPlace());
          pollJson.put("pollEnd", poll.getPollEnd());
          pollJson.put("date", poll.getDate());
          pollsJson.add(pollJson);
        }
        res = new Response(pollsJson);

      } catch (SQLException e) {
        res = new Response(500);
      }
      return res;
    };

    /*
     * Handler delete = (Context context) -> { Response res = new Response(500); try
     * { JsonObject body = JsonParser.parseObject(context.requestData); int amount =
     * VoteitDB.deletePoll(body.getInteger("id")); if (amount == 0) { res = new
     * Response(404); } else { res = new Response(200); } } catch (JsonException e)
     * { res = new Response(e.getMessage()).setStatus(400); } catch (SQLException e)
     * { res = new Response(500); } return res; };
     */

    Handler create = (Context context) -> {
      Response res = new Response(500);
      try {
        User user = new User(Integer.parseInt(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY)));
        // TODO REMOVE
        user.setAdmin(true);
        // TODO REMOVE
        if (!user.getAdmin()) {
          res = new Response(403);
        } else {
          JsonObject req = JsonParser.parseObject(context.requestData);
          String place = req.getString("place");
          long pollEnd = req.getLong("pollEnd");
          long date = req.getLong("date");

          Poll.add(place, pollEnd, date);
          res = new Response(200);
        }

      } catch (JsonException | NumberFormatException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      }
      return res;
    };

    /*
     * Handler edit = (Context context) -> { Response res = new Response(500); try {
     * JsonObject body = JsonParser.parseObject(context.requestData); int amount =
     * VoteitDB.updatePoll(body); if (amount == 0) { res = new Response(404); } else
     * { res = new Response(200); } } catch (JsonException e) { res = new
     * Response(e.getMessage()).setStatus(400); } catch (SQLException e) { res = new
     * Response(500); } return res; };
     */

    server.addRoute("/api/poll/get", get);
    server.addRoute("/api/poll/create", create);
    // server.addRoute("/api/poll/edit", edit);
    // server.addRoute("/api/poll/delete", delete);
  }
}
