package voteit;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;
import voteit.modules.LoginNotFoundException;
import voteit.resources.DataStructureFactory;

/**
 * Manager Class
 *
 * Provides methods to interact with the database to create, modify, update and
 * delete datasets
 */
public class Manager {

    public static JsonArray getTopics() throws SQLException {
        JsonArray topicArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitTopics");
        while (rs.next()) {
            topicArray.add(DataStructureFactory.buildTopic(rs));
        }
        return topicArray;
    }

    public static JsonArray getUsers() throws SQLException {
        JsonArray userArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitUsers");
        while (rs.next()) {
            userArray.add(DataStructureFactory.buildUser(rs));
        }
        return userArray;
    }

    public static JsonObject getUserJson(int id) throws SQLException {
        ResultSet rs = VoteitDB.getUser(id);
        rs.next();
        return DataStructureFactory.buildUser(rs);
    }

    public static JsonObject getPollJson(int id) throws SQLException {
        ResultSet rs = VoteitDB.getPoll(id);
        rs.next();
        return DataStructureFactory.buildPoll(rs);
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

    public static void updateTopic(JsonObject object) {
        VoteitDB.updateData("VoteitTopics", object);
    }

    public static int loginUser(String name, String password) throws LoginNotFoundException, SQLException {
        boolean done = false;
        int token = 0;
        ResultSet rs = VoteitDB.getUser(name, password);
        if (rs.next()) {
            while (!done) {
                token = (int) Math.floor(Math.random() * 1000000000 + 1);
                try {
                    VoteitDB.execCommand(
                            "INSERT INTO UserTokens(token, userId) VALUES (" + token + "," + rs.getInt("userId") + ")");
                    done = true;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
        return token;
    }
}
