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

        switch (context.route.charAt(5)) {
        case 't':
            try {
                return new Response(Manager.getTopics());
            } catch (SQLException e) {
                System.out.println("Couldn't create a response.");
                System.out.println(e.getMessage());
                return null;
            }
        case 'u':
            return null;
        case 'v':
            return null;
        default:
            System.out.println("Response couldn't be created.");
            return null;
        }
    }

}
