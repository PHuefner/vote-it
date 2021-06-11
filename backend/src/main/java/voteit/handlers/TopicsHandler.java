package voteit.handlers;

import java.sql.SQLException;
import java.util.ArrayList;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonException;
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
import voteit.models.Poll;
import voteit.models.Topic;
import voteit.models.TopicVoted;
import voteit.models.User;
import voteit.modules.Constants;
import voteit.modules.exceptions.LoginNotFoundException;
import voteit.modules.exceptions.ResourceNotFoundException;

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

        ArrayList<Topic> topics;
        try {
          // Try to get with voted status
          User user = new User(Integer.parseInt(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY)));
          topics = TopicVoted.getTopicsVoted(pollId, user.getUserId());
        } catch (Exception e) {
          // Get without votes on any failure
          topics = Topic.getTopics(pollId);
        }

        JsonArray topicsJson = new JsonArray();
        for (Topic topic : topics) {
          JsonObject topicJson = new JsonObject();
          topicJson.put("topicId", topic.getTopicId());
          topicJson.put("title", topic.getTitle());
          topicJson.put("content", topic.getContent());
          topicJson.put("votes", topic.getVotes());
          topicJson.put("userId", topic.getUserId());
          topicJson.put("pollId", topic.getPollId());
          if (topic instanceof TopicVoted) {
            topicJson.put("voted", ((TopicVoted) topic).getVoted());
          }
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

    Handler vote = (Context context) -> {
      Response res = new Response(500);

      try {
        JsonObject voteJson = JsonParser.parseObject(context.requestData);
        int topicId = voteJson.getInteger("topicId");
        boolean voted = voteJson.getBool("voted");

        User user = new User(Integer.parseInt(context.cookies.get(Constants.LOGINTOKENCOOKIEKEY)));
        TopicVoted topic = TopicVoted.fromIds(topicId, user.getUserId());
        topic.setVoted(voted);
        res = new Response(200);
      } catch (NumberFormatException | JsonFormattingException | UnsupportedTypeException | KeyNotFoundException
          | WrongTypeException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (LoginNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(401);
      } catch (ResourceNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(404);
      }

      return res;
    };

    Handler delete = (Context context) -> {
      Response res = new Response(500);
      try {
        JsonObject body = JsonParser.parseObject(context.requestData);
        Topic topic = Topic.getTopic(body.getInteger("topicId"));
        topic.delete();
        res = new Response(200);
      } catch (JsonException e) {
        res = new Response(e.getMessage()).setStatus(400);
      } catch (SQLException e) {
        res = new Response(500);
      } catch (ResourceNotFoundException e) {
        res = new Response(e.getMessage()).setStatus(404);
      }
      return res;
    };

    server.addRoute("/api/topic/get", get);
    server.addRoute("/api/topic/submit", submit);
    server.addRoute("/api/topic/vote", vote);
    server.addRoute("/api/topic/delete", delete);
  }

}
