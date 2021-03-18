package voteit;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;
import voteit.resources.Datastructure;

public class Manager {

    public static JsonArray getTopics() throws SQLException {
        JsonArray topicArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitTopics");
        while (rs.next()) {
            topicArray.add(Datastructure.buildTopic(rs));
        }
        return topicArray;
    }

    public static JsonArray getUsers() throws SQLException {
        JsonArray userArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitUsers");
        while (rs.next()) {
            userArray.add(Datastructure.buildUser(rs));
        }
        return userArray;
    }

    public static JsonObject getUserJson(int id) throws SQLException {
        ResultSet rs = VoteitDB.getUser(id);
        rs.next();
        return Datastructure.buildUser(rs);
    }

    public static JsonObject getPollJson(int id) throws SQLException {
        ResultSet rs = VoteitDB.getPoll(id);
        rs.next();
        return Datastructure.buildPoll(rs);
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

    public static void addTopic(JsonObject object) {
        VoteitDB.addData("VoteitTopics", object);
    }
}
