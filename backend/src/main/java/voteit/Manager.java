package voteit;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.modules.LoginNotFoundException;

/**
 * Manager Class
 *
 * Provides methods to interact with the database to create, modify, update and
 * delete datasets
 */
public class Manager {

    public static int loginUser(String name, String password) throws LoginNotFoundException, SQLException {
        boolean done = false;
        int token = 0;
        ResultSet rs = VoteitDB.getUser(name, password);
        if (rs.next()) {
            while (!done) {
                token = (int) Math.floor(Math.random() * 1000000000 + 1);
                try {
                    VoteitDB.execCommand(
                            "INSERT INTO UserTokens(token, userId) VALUES (" + token + "," + rs.getInt("userId") + ")");
                    done = true;
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            throw new LoginNotFoundException("Wrong username or password");
        }
        return token;
    }
}
