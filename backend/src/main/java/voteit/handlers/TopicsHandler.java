package voteit.handlers;

import java.sql.ResultSet;
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
import voteit.models.Topic;
import voteit.models.User;
import voteit.modules.Constants;
import voteit.modules.exceptions.LoginNotFoundException;
import voteit.modules.exceptions.ResourceNotFoundException;
import voteit.resources.DataStructureFactory;

public class TopicsHandler {
  public static void addHandlers(ServerHttp server) {
    Handler submit = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject req = JsonParser.parseObject(context.requestData);
        int pollId = req.getInteger("pollId");
        String title = req.getString("title");
        String content = req.getString("content");

        User user = new User(Integer.parseInt(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY)));
        Poll poll = new Poll(pollId);
        Topic.submit(title, content, user, poll);
        res = new Response(200);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      } catch (JsonException | NumberFormatException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (ResourceNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(404);
      }
      return res;
    };

    Handler get = (Context context) -> {
      Response res = new Response(500);

      try {
        JsonObject req = JsonParser.parseObject(context.requestData);
        int pollId = req.getInteger("pollId");
        ArrayList<Topic> topics = Topic.getTopics(pollId);

        JsonArray topicsJson = new JsonArray();
        for (Topic topic : topics) {
          JsonObject topicJson = new JsonObject();
          topicJson.put("topicId", topic.getTopicId());
          topicJson.put("title", topic.getTitle());
          topicJson.put("content", topic.getContent());
          topicJson.put("userId", topic.getUserId());
          topicJson.put("pollId", topic.getPollId());
          topicsJson.add(topicJson);
        }
        res = new Response(topicsJson);
      } catch (JsonException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      }

      return res;
    };

    server.addRoute("/api/topic/get", get);
    server.addRoute("/api/topic/submit", submit);
  }

}
