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
            try {
                return new Response(Manager.getUserJson(1));
            } catch (SQLException e) {
                System.out.println("Couldn't create UserObject.");
                System.out.println(e.getMessage());
                return null;
            }
        } else if (context.route.contains("poll/get")) {
            try {
                return new Response(Manager.getPollJson(1));
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
