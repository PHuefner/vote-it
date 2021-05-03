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

    // Connection to DB

    public static void initTables() {
        try {
            database.createStatement().execute("""
                    DROP TABLE IF EXISTS
                    VoteitUsers,
                    VoteitPolls,
                    VoteitTopics,
                    UserTokens,
                    VoteitVote
                    CASCADE;
                    """);

            database.createStatement().execute("""
                    CREATE TABLE VoteitUsers
                    (
                        userId SERIAL NOT NULL PRIMARY KEY,
                        name TEXT UNIQUE, password TEXT NOT NULL,
                        admin BOOLEAN NOT NULL
                    );
                    """);

            database.createStatement().execute("""
                    CREATE TABLE VoteitPolls
                    (
                        pollId SERIAL NOT NULL PRIMARY KEY,
                        place TEXT,
                        pollBegin TIMESTAMP,
                        date TIMESTAMP,
                        pollEnd TIMESTAMP
                    );
                    """);

            database.createStatement().execute("""
                    CREATE TABLE VoteitTopics
                    (
                        topicId SERIAL NOT NULL PRIMARY KEY,
                        title TEXT,
                        content TEXT,
                        userId INT,
                        pollId INT,
                        FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON UPDATE SET NULL,
                        FOREIGN KEY (pollId) REFERENCES VoteitPolls(pollId) ON UPDATE SET NULL
                    );
                    """);

            database.createStatement().execute("""
                    CREATE TABLE VoteitVote
                    (
                        userId INT, topicId INT,
                        FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON UPDATE SET NULL,
                        FOREIGN KEY (topicId) REFERENCES VoteitTopics(topicId) ON UPDATE SET NULL
                    );
                    """);

            database.createStatement().execute("""
                    CREATE TABLE UserTokens
                    (
                        token INT PRIMARY KEY,
                        userId INT,
                        FOREIGN KEY (userId) REFERENCES VoteitUsers(userId) ON DELETE CASCADE
                    );
                    """);

        } catch (SQLException e) {
            System.out.println("Couldn't create tables.");
            System.out.println(e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Couldn't connect db.");
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

    // Users

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

    public static void addUser(String name, String password, boolean admin) throws SQLException {
        PreparedStatement ps;
        ps = database.prepareStatement("INSERT INTO VoteitUsers(name, password,admin) VALUES (?,?,?)");
        ps.setString(1, name);
        ps.setString(2, password);
        ps.setBoolean(3, admin);
        ps.executeUpdate();
    }

    public static void deleteUser(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("DELETE FROM VoteitUsers WHERE userId=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public static void updateUser(String name, String password, boolean admin, int userId) throws SQLException {
        PreparedStatement ps;
        ps = database.prepareStatement("UPDATE VoteitUsers SET name = ?, password = ?, admin = ? WHERE userId = ?");
        ps.setString(1, name);
        ps.setString(2, password);
        ps.setBoolean(3, admin);
        ps.setInt(4, userId);
        ps.executeUpdate();
    }

    // Tokens

    public static ResultSet getToken(int tokenId) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM UserTokens WHERE token=?");
        ps.setInt(1, tokenId);
        return ps.executeQuery();
    }

    public static int createToken(int userId) throws SQLException {
        int token = (int) Math.floor(Math.random() * 1000000000 + 1);
        PreparedStatement ps = database.prepareStatement("INSERT INTO UserTokens(token, userId) VALUES (?,?)");
        ps.setInt(1, token);
        ps.setInt(2, userId);
        ps.executeUpdate();
        return token;
    }

    // TODO remove token

    // Polls

    public static ResultSet getPoll(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitPolls WHERE pollId=?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static ResultSet getPolls() throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitPolls");
        return ps.executeQuery();
    }

    public static void addPoll(String place, long pollBegin, long pollEnd, long date) throws SQLException {
        PreparedStatement ps;
        ps = database.prepareStatement("INSERT INTO VoteitPolls(place, pollBegin, date, pollEnd) VALUES (?,?,?,?)");
        ps.setString(1, place);
        ps.setTimestamp(2, new Timestamp(pollBegin));
        ps.setTimestamp(3, new Timestamp(date));
        ps.setTimestamp(4, new Timestamp(pollEnd));
        ps.executeUpdate();
    }

    public static void deletePoll(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("DELETE FROM VoteitPolls WHERE pollId=?");
        ps.setInt(1, id);
        ps.executeUpdate();
    }

    public static void updatePoll(String place, long pollBegin, long pollEnd, long date, int pollId)
            throws SQLException {
        PreparedStatement ps;
        ps = database.prepareStatement(
                "UPDATE VoteitPolls SET place = ?, pollBegin = ?, date = ?, pollEnd = ? WHERE pollId = ?");
        ps.setString(1, place);
        ps.setTimestamp(2, new Timestamp(pollBegin));
        ps.setTimestamp(3, new Timestamp(date));
        ps.setTimestamp(4, new Timestamp(pollEnd));
        ps.setInt(5, pollId);
        ps.executeUpdate();
    }

    // Topics

    public static ResultSet getTopic(int id) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitTopics WHERE topicId=?");
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public static ResultSet getTopics(int pollId) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT * FROM VoteitTopics WHERE pollId=?");
        ps.setInt(1, pollId);
        return ps.executeQuery();
    }

    public static void addTopic(String title, String content, int userId, int pollId) throws SQLException {
        PreparedStatement ps;
        ps = database.prepareStatement("INSERT INTO VoteiTopics(title, content, userId, pollId) VALUES (?,?,?,?)");
        ps.setString(1, title);
        ps.setString(3, content);
        ps.setInt(4, userId);
        ps.setInt(5, pollId);
        ps.executeUpdate();
    }

    public static int deleteTopic(int topicId) throws SQLException {
        PreparedStatement ps = database.prepareStatement("DELETE FROM VoteitTopics WHERE topicId=?");
        ps.setInt(1, topicId);
        return ps.executeUpdate();
    }

    public static void updateTopic(JsonObject topic) throws SQLException, KeyNotFoundException, WrongTypeException {
        PreparedStatement ps;
        ps = database.prepareStatement(
                "UPDATE VoteitTopics SET title = ?, content = ?, userId = ?, pollId = ? WHERE topicId = ?");
        ps.setString(1, topic.getString("title"));
        ps.setString(3, topic.getString("content"));
        ps.setInt(4, topic.getInteger("userId"));
        ps.setInt(5, topic.getInteger("pollId"));
        ps.setInt(6, topic.getInteger("topicId"));
        ps.executeUpdate();
    }

    // Votes

    public static ResultSet getVotesByTopic(int topicId) throws SQLException {
        PreparedStatement ps = database.prepareStatement("SELECT COUNT(*) AS Anzahl FROM VoteitVote WHERE topicId=?");
        ps.setInt(1, topicId);
        return ps.executeQuery();
    }

    public static ResultSet getVoted(int userId, int pollId) throws SQLException {
        PreparedStatement ps = database
                .prepareStatement("SELECT COUNT(*) AS Anzahl FROM VoteitVote WHERE userId=? AND pollId=?");
        ps.setInt(1, userId);
        ps.setInt(2, pollId);
        return ps.executeQuery();
    }

    public static void voteForTopic(int topic, int user) throws SQLException {
        PreparedStatement ps;
        ps = database.prepareStatement("INSERT INTO VoteitVote(topicId, userId) VALUES (?,?)");
        ps.setInt(1, topic);
        ps.setInt(2, user);
        ps.executeUpdate();
    }

}
