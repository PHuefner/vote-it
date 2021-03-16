package voteit;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.libs.json.JsonArray;
import voteit.libs.json.JsonObject;

public class Manager {

    public Manager() {

    }

    public static JsonArray getTopics() throws SQLException {
        JsonArray topicsArray = new JsonArray();
        ResultSet rs = VoteitDB.getTable("VoteitTopics");
        while (rs.next()) {
            JsonObject topicObject = new JsonObject();
            topicObject.put("title", rs.getString("title"));
            topicObject.put("content", rs.getString("content"));
            topicObject.put("votes", rs.getInt("votes"));
            topicObject.put("pollId", rs.getInt("pollId"));
            topicObject.put("userId", rs.getInt("userId"));
            topicObject.put("topicId", rs.getInt("topicId"));
            topicsArray.add(topicObject);
        }
        return topicsArray;
    }

}
