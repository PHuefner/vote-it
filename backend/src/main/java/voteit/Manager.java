package voteit;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;

public class Manager {

    public Manager() {

    }

    public static JsonArray getTopics() throws SQLException {
        JsonArray topicArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitTopics");
        while (rs.next()) {
            JsonObject topicObject = new JsonObject();
            topicObject.put("topicId", rs.getInt("topicId"));
            topicObject.put("title", rs.getString("title"));
            topicObject.put("content", rs.getString("content"));
            topicObject.put("votes", rs.getInt("votes"));
            topicObject.put("userId", rs.getInt("userId"));
            topicObject.put("pollId", rs.getInt("pollId"));
            topicArray.add(topicObject);
        }
        return topicArray;
    }

    public static JsonArray getUsers() throws SQLException {
        JsonArray userArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitUsers");
        while (rs.next()) {
            JsonObject userObject = new JsonObject();
            userObject.put("userId", rs.getInt("userId"));
            userObject.put("lastname", rs.getString("lastname"));
            userObject.put("name", rs.getString("name"));
            userObject.put("password", rs.getString("password"));
            userArray.add(userObject);
        }
        return userArray;
    }

    public static JsonObject getUserJson(int id) throws SQLException {
        JsonObject user = new JsonObject();
        ResultSet rs = VoteitDB.getUser(id);
        rs.next();
        user.put("userId", rs.getInt("userId"));
        user.put("lastname", rs.getString("lastname"));
        user.put("name", rs.getString("name"));
        user.put("password", rs.getString("password"));
        return user;
    }

    public static JsonObject getPollJson(int id) throws SQLException {
        JsonObject poll = new JsonObject();
        ResultSet rs = VoteitDB.getPoll(id);
        rs.next();
        poll.put("pollId", rs.getInt("pollId"));
        poll.put("place", rs.getString("place"));
        poll.put("pollBegin", rs.getDate("pollBegin").toString());
        poll.put("date", rs.getDate("date").toString());
        return poll;
    }

    public static void deleteUser(int userId) {
        VoteitDB.delete("VoteitUsers", userId);
    }

    public static void deletePoll(int pollId) {
        VoteitDB.delete("VoteitPolls", pollId);
    }

    public static void deleteTopic(int topicId) {
        VoteitDB.delete("VoteitTopics", topicId);
    }

}
