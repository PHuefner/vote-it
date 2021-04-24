package voteit.handlers;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.VoteitDB;
import voteit.libs.json.JsonArray;
import voteit.libs.serverhttp.*;
import voteit.modules.Constants;
import voteit.resources.DataStructureFactory;

/**
 * MyHttpHandler
 */
public class GetHandler implements Handler {

    @Override
    public Response handle(Context context) {

        if (context.route.contains("topics/get")) {
            JsonArray topics = new JsonArray();
            try {
                ResultSet rs = VoteitDB.getTopics();
                while (rs.next()) {
                    topics.add(DataStructureFactory.buildTopic(rs));
                }
                return new Response(topics);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return Constants.genericServerError();
            }
        } else if (context.route.contains("user/data")) {
            String id = context.cookies.get("userId");
            try {
                ResultSet rs = VoteitDB.getUser(Integer.parseInt(id));
                if (rs.next()) {
                    return new Response(DataStructureFactory.buildUser(rs));
                }
                return Constants.genericNotFound();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return Constants.genericServerError();
            }

        } else if (context.route.contains("poll/get")) {
            String id = context.cookies.get("pollId");
            try {
                ResultSet rs = VoteitDB.getPoll(Integer.parseInt(id));
                if (rs.next()) {
                    return new Response(DataStructureFactory.buildPoll(rs));
                }
                return Constants.genericNotFound();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return Constants.genericServerError();
            }
        } else {
            System.out.println("Invalid url for this handler.");
            return Constants.genericNotFound();
        }
    }

}
