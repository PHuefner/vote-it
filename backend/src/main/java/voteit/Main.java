package voteit;

import java.io.IOException;

import voteit.handlers.*;
import voteit.libs.serverhttp.*;

/**
 * Main
 */
public class Main {

    public static void main(String[] args) {
        String routeBase = "/api/";
        ServerHttp server = null;
        try {
            server = new ServerHttp(3000);
        } catch (IOException e) {
            System.out.println("Couldn't create HttpServer.");
            System.out.println(e.getMessage());
            System.exit(69);
        }

        try {
            server.addRoute(routeBase + "topics/get", new GetHandler());
            server.addRoute(routeBase + "topics/edit", new PostHandler());
            server.addRoute(routeBase + "topics/delete", new PostHandler());
            server.addRoute(routeBase + "topics/vote", new PostHandler());
            server.addRoute(routeBase + "topics/submit", new PostHandler());
            server.addRoute(routeBase + "user/login", new PostHandler());
            server.addRoute(routeBase + "user/data", new GetHandler());
            server.addRoute(routeBase + "user/register", new PostHandler());
            server.addRoute(routeBase + "user/delete", new PostHandler());
            server.addRoute(routeBase + "user/edit", new PostHandler());
            server.addRoute(routeBase + "voting/get", new GetHandler());
            server.addRoute(routeBase + "voting/create", new PostHandler());
            server.addRoute(routeBase + "voting/delete", new PostHandler());
            server.addRoute(routeBase + "voting/edit", new PostHandler());
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

        VoteitDB.createConnection();
        VoteitDB.createTables();
        VoteitDB.execCommand("INSERT INTO VoteitTopics(title) VALUES ('first'), ('second');");
    }
}
