package voteit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import voteit.libs.json.JsonObject;
import voteit.libs.json.KeyNotFoundException;
import voteit.libs.json.WrongTypeException;

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
            database.createStatement().execute("DROP TABLE IF EXISTS VoteitUsers,VoteitPolls,VoteitTopics,UserTokens;");

            database.createStatement().execute("CREATE TABLE VoteitUsers"
                    + "(userId SERIAL NOT NULL PRIMARY KEY, lastname TEXT, name TEXT, password TEXT);");

            database.createStatement().execute("CREATE TABLE VoteitPolls"
                    + "(pollId SERIAL NOT NULL PRIMARY KEY, place TEXT, pollBegin TIMESTAMP, date TIMESTAMP, pollEnd TIMESTAMP);");

            database.createStatement().execute("CREATE TABLE VoteitTopics"
                    + "(topicId SERIAL NOT NULL PRIMARY KEY, title TEXT, votes INT, content TEXT, userId INT, pollId INT,"
                    + "FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON UPDATE SET NULL,"
                    + "FOREIGN KEY (pollId) REFERENCES VoteitPolls(pollId) ON UPDATE SET NULL);");

            database.createStatement()
                    .execute("CREATE TABLE Nutzer_Thema(userId INT, topicId INT,"
                            + "FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON UPDATE SET NULL,"
                            + "FOREIGN KEY (topicId) REFERENCES VoteitTopics(topicId) ON UPDATE SET NULL);");

            database.createStatement().execute("CREATE TABLE UserTokens(token INT PRIMARY KEY, userId INT,"
                    + "FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON DELETE CASCADE)");
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

    public static ResultSet getUser(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitUsers WHERE userId=?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static ResultSet getUser(String name, String password) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitUsers WHERE name=? AND password=?");
        ps.setString(1, name);
        ps.setString(2, password);
        return ps.executeQuery();
    }

    public static ResultSet getUsers() throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitUsers");
        return ps.executeQuery();
    }

    public static ResultSet getPoll(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitPolls WHERE pollId=?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static ResultSet getPolls() throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitPolls");
        return ps.executeQuery();
    }

    public static ResultSet getTopic(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitTopics WHERE topicId=?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static ResultSet getTopics() throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitTopics");
        return ps.executeQuery();
    }

    public static void deleteUser(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("DELETE FROM VoteitUsers WHERE userId=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public static void deleteTopic(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("DELETE FROM VoteitTopics WHERE topicId=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public static void deletePoll(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("DELETE FROM VoteitPolls WHERE pollId=?");
        ps.setInt(1, id);
        ps.executeQuery();
    }

    public static void addTopic(JsonObject topic) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database
                .prepareStatement("INSERT INTO VoteiTopics(title, votes, content, userId, pollId) VALUES (?,?,?,?,?)");
        ps.setString(1, topic.getString("title"));
        ps.setInt(2, topic.getInteger("votes"));
        ps.setString(3, topic.getString("content"));
        ps.setInt(4, topic.getInteger("userId"));
        ps.setInt(5, topic.getInteger("pollId"));
        ps.executeUpdate();
    }

    public static void addUser(JsonObject user) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database.prepareStatement("INSERT INTO VoteitUsers(lastname, name, password) VALUES (?,?,?)");
        ps.setString(1, user.getString("lastname"));
        ps.setString(2, user.getString("name"));
        ps.setString(3, user.getString("password"));
        ps.executeUpdate();
    }

    public static void addPoll(JsonObject poll) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database.prepareStatement("INSERT INTO VoteitPolls(place, pollBegin, date, pollEnd) VALUES (?,?,?,?)");
        ps.setString(1, poll.getString("place"));
        ps.setTimestamp(2, new Timestamp(poll.getInteger("pollBegin")));
        ps.setTimestamp(3, new Timestamp(poll.getInteger("date")));
        ps.setTimestamp(4, new Timestamp(poll.getInteger("pollEnd")));
        ps.executeUpdate();
    }

    public static void updateTopic(JsonObject topic) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database.prepareStatement(
                "UPDATE TABLE VoteitTopics SET title = ?, votes = ?, content = ?, userId = ?, pollId = ? WHERE topicId = ?");
        ps.setString(1, topic.getString("title"));
        ps.setInt(2, topic.getInteger("votes"));
        ps.setString(3, topic.getString("content"));
        ps.setInt(4, topic.getInteger("userId"));
        ps.setInt(5, topic.getInteger("pollId"));
        ps.setInt(6, topic.getInteger("topicId"));
        ps.executeUpdate();
    }

    public static void updateUser(JsonObject user) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database
                .prepareStatement("UPDATE TABLE VoteitUsers SET lastname = ?, name = ?, password = ? WHERE userId = ?");
        ps.setString(1, user.getString("lastname"));
        ps.setString(2, user.getString("name"));
        ps.setString(3, user.getString("password"));
        ps.setInt(4, user.getInteger("userId"));
        ps.executeUpdate();
    }

    public static void updatePoll(JsonObject poll) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database.prepareStatement(
                "UPDATE TABLE VoteitPolls SET place = ?, pollBegin = ?, date = ?, pollEnd = ? WHERE pollId = ?");
        ps.setString(1, poll.getString("place"));
        ps.setTimestamp(2, new Timestamp(poll.getInteger("pollBegin")));
        ps.setTimestamp(3, new Timestamp(poll.getInteger("date")));
        ps.setTimestamp(4, new Timestamp(poll.getInteger("pollEnd")));
        ps.setInt(5, poll.getInteger("pollId"));
        ps.executeUpdate();
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
