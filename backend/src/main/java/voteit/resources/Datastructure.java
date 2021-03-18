package voteit.resources;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.libs.json.JsonObject;

public class Datastructure {

    public static JsonObject buildUser(ResultSet rs) throws SQLException {
        JsonObject user = new JsonObject();
        user.put("userId", rs.getInt("userId"));
        user.put("lastname", rs.getString("lastname"));
        user.put("name", rs.getString("name"));
        user.put("password", rs.getString("password"));
        return user;
    }

    public static JsonObject buildTopic(ResultSet rs) throws SQLException {
        JsonObject topic = new JsonObject();
        topic.put("topicId", rs.getInt("topicId"));
        topic.put("title", rs.getString("title"));
        topic.put("content", rs.getString("content"));
        topic.put("votes", rs.getInt("votes"));
        topic.put("userId", rs.getInt("userId"));
        topic.put("pollId", rs.getInt("pollId"));
        return topic;
    }

    public static JsonObject buildPoll(ResultSet rs) throws SQLException {
        JsonObject poll = new JsonObject();
        poll.put("pollId", rs.getInt("pollId"));
        poll.put("place", rs.getString("place"));
        poll.put("pollBegin", rs.getDate("pollBegin").toString());
        poll.put("pollEnd", rs.getDate("pollEnd").toString());
        poll.put("date", rs.getDate("date").toString());
        return poll;
    }

}
