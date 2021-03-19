package voteit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.libs.json.JsonObject;

/**
 * VoteidDB Class
 *
 * Abstraction for SQL queries. Allows interaction with the database without
 * having to write sql queries
 */
public class VoteitDB {

    static Connection database;
    static String host = "localhost:5432";
    static String db = "voteit";
    static String url = "jdbc:postgresql://" + host + "/" + db;
    static String user = "voteit";
    static String password = "pw";

    public static void initTables() {
        try {
            database.createStatement().execute("DROP TABLE IF EXISTS VoteitUsers,VoteitPolls,VoteitTopics;");

            database.createStatement().execute("CREATE TABLE VoteitUsers"
                    + "(userId SERIAL NOT NULL PRIMARY KEY, lastname TEXT, name TEXT, password TEXT);");

            database.createStatement().execute("CREATE TABLE VoteitPolls"
                    + "(pollId SERIAL NOT NULL PRIMARY KEY, place TEXT, pollBegin TIMESTAMP, date TIMESTAMP, pollEnd TIMESTAMP);");

            database.createStatement().execute("CREATE TABLE VoteitTopics"
                    + "(topicId SERIAL NOT NULL PRIMARY KEY, title TEXT, votes INT, content TEXT, userId INT, pollId INT,"
                    + "FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON UPDATE SET NULL,"
                    + "FOREIGN KEY (pollId) REFERENCES VoteitPolls(pollId) ON UPDATE SET NULL);");

        } catch (SQLException e) {
            System.out.println("Couldn't create tables.");
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Couldn't connect db.");
        }
    }

    public static void execCommand(String command) {
        try {
            database.createStatement().execute(command);
        } catch (SQLException e) {
            System.out.println("Couldn't execute the command.");
            System.out.println(e.getMessage());
        }
    }

    public static ResultSet getTable(String name) {
        try {
            return database.createStatement().executeQuery("SELECT * FROM " + name);
        } catch (SQLException e) {
            System.out.println("Couldn't read table from database.");
            System.out.println(e.getMessage());
            System.exit(20);
            return null;
        }
    }

    public static ResultSet getUser(int id) {
        try {
            PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitUsers WHERE userId=?");
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't find user in database.");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static ResultSet getPoll(int id) {
        try {
            PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitPolls WHERE pollId=?");
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println("Couldn't find poll in database.");
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void delete(String table, int id) {
        try {
            PreparedStatement ps;
            if (table.contains("Topics")) {
                ps = database.prepareStatement("DELETE FROM " + table + " WHERE topicId=?");
            } else if (table.contains("Polls")) {
                ps = database.prepareStatement("DELETE FROM " + table + " WHERE pollId=?");
            } else if (table.contains("Users")) {
                ps = database.prepareStatement("DELETE FROM " + table + " WHERE userId=?");
            } else {
                System.out.println("Couldn't get table.");
                return;
            }

            ps.setInt(1, id);
            ps.executeUpdate();
            return;
        } catch (Exception e) {
            System.out.println("Couldn't delete data.");
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void addData(String table, JsonObject object) {
        try {
            PreparedStatement ps = null;
            if (table.contains("Topics")) {
                ps = database.prepareStatement(
                        "INSERT INTO VoteitTopics(title,votes,content,userId,pollId) VALUES (?,?,?,?,?)");
                ps.setString(1, object.getString("title"));
                ps.setInt(2, object.getInteger("votes"));
                ps.setString(3, object.getString("content"));
                ps.setInt(4, object.getInteger("userId"));
                ps.setInt(5, object.getInteger("pollId"));
            } else if (table.contains("Polls")) {

            } else if (table.contains("Users")) {

            } else {
                System.out.println("Couldn't get table.");
                return;
            }

            ps.executeUpdate();
            return;
        } catch (Exception e) {
            System.out.println("Couldn't insert data.");
            System.out.println(e.getMessage());
            return;
        }
    }

    public static void createConnection() {
        try {
            database = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Error while connecting to db.");
            System.out.println(e.getMessage());
            System.exit(20);
        }
    }

    public static void closeConnection() {
        try {
            database.close();
        } catch (SQLException e) {
            System.out.println("Error while closing connection.");
            System.out.println(e.getMessage());
            System.exit(20);
        } catch (NullPointerException e) {
            System.out.println("No open Database connection to be closed.");
        }
    }
}
