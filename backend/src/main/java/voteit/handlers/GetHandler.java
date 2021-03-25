package voteit.handlers;

import java.sql.SQLException;

import voteit.Manager;
import voteit.libs.serverhttp.*;

/**
 * MyHttpHandler
 */
public class GetHandler implements Handler {

    @Override
    public Response handle(Context context) {

        if (context.route.contains("topics/get")) {
            try {
                return new Response(Manager.getTopics());
            } catch (SQLException e) {
                System.out.println("Couldn't create a TopicArray.");
                System.out.println(e.getMessage());
                return null;
            }
        } else if (context.route.contains("user/data")) {
            String id = context.cookies.get("userId");
            try {
                return new Response(Manager.getUserJson(Integer.parseInt(id)));
            } catch (SQLException e) {
                System.out.println("Couldn't create UserObject.");
                System.out.println(e.getMessage());
                return null;
            }
        } else if (context.route.contains("poll/get")) {
            String id = context.cookies.get("pollId");
            try {
                return new Response(Manager.getPollJson(Integer.parseInt(id)));
            } catch (SQLException e) {
                System.out.println("Couldn't create PollObject");
                System.out.println(e.getMessage());
                return null;
            }
        } else {
            System.out.println("Invalid url for this handler.");
            return null;
        }
    }

}
