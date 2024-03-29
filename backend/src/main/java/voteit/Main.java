package voteit;

import java.io.IOException;
import java.sql.SQLException;

import voteit.handlers.PollHandler;
import voteit.handlers.TopicsHandler;
import voteit.handlers.UserHandler;
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
    public static void main(String[] args) throws InterruptedException {
        ServerHttp server = null;
        // TODO set negative Response status

        Handler notFoundHandler = (Context context) -> {
            return Constants.genericNotFound();
        };

        boolean error = false;
        do {
            try {
                VoteitDB.createConnection();
                error = false;
            } catch (SQLException e1) {
                System.out.println("Error connectin to database. retrying in 10 seconds");
                error = true;
                Thread.sleep(10000);
            }
        } while (error);

        VoteitDB.initTables();

        try {
            server = new ServerHttp(80);
        } catch (IOException e) {
            System.out.println("Couldn't create HttpServer.");
            System.out.println(e.getMessage());
            System.exit(69);
        }

        try {
            PollHandler.addHandlers(server);
            UserHandler.addHandlers(server);
            TopicsHandler.addHandlers(server);
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
