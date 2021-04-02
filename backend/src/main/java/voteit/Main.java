package voteit;

import java.io.IOException;
import java.sql.SQLException;

import voteit.handlers.GetHandler;
import voteit.handlers.PollPostHandler;
import voteit.handlers.TopicPostHandler;
import voteit.handlers.UserPostHandler;
import voteit.libs.serverhttp.Context;
import voteit.libs.serverhttp.Handler;
import voteit.libs.serverhttp.ServerHttp;
import voteit.modules.Constants;

/**
 * Main Class
 *
 * Initalizes the Database Connection, sets up the routes and handlers and then
 * starts the server
 */
public class Main {

    // docker run -p 5432:5432 -e POSTGRES_PASSWORD=pw -e POSTGRES_USER=voteit
    // postgres
    public static void main(String[] args) {
        String routeBase = "/api/";
        ServerHttp server = null;
        // TODO set negative Response status

        GetHandler getHandler = new GetHandler();
        TopicPostHandler topicPostHandler = new TopicPostHandler();
        UserPostHandler userPostHandler = new UserPostHandler();
        PollPostHandler pollPostHandler = new PollPostHandler();
        Handler notFoundHandler = (Context context) -> {
            return Constants.genericNotFound();
        };

        VoteitDB.createConnection();
        VoteitDB.initTables();

        try {
            server = new ServerHttp(3001);
        } catch (IOException e) {
            System.out.println("Couldn't create HttpServer.");
            System.out.println(e.getMessage());
            System.exit(69);
        }

        try {
            server.addRoute(routeBase + "topics/get", getHandler);
            server.addRoute(routeBase + "topics/edit", topicPostHandler);
            server.addRoute(routeBase + "topics/delete", topicPostHandler);
            server.addRoute(routeBase + "topics/vote", topicPostHandler);
            server.addRoute(routeBase + "topics/submit", topicPostHandler);
            server.addRoute(routeBase + "user/data", getHandler);
            server.addRoute(routeBase + "user/login", userPostHandler);
            server.addRoute(routeBase + "user/register", userPostHandler);
            server.addRoute(routeBase + "user/delete", userPostHandler);
            server.addRoute(routeBase + "user/edit", userPostHandler);
            server.addRoute(routeBase + "user/login", userPostHandler);
            server.addRoute(routeBase + "poll/get", getHandler);
            server.addRoute(routeBase + "poll/create", pollPostHandler);
            server.addRoute(routeBase + "poll/delete", pollPostHandler);
            server.addRoute(routeBase + "poll/edit", pollPostHandler);
            server.setNotFoundHandler(notFoundHandler);
        } catch (Exception e) {
            System.out.println("Couldn't create contexts.");
            System.out.println(e.getMessage());
        }

        try {
            server.start();
        } catch (Exception e) {
            System.out.println("Couldn't start server.");
            System.out.println(e.getMessage());
        }

    }
}
