package voteit.models;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.VoteitDB;
import voteit.modules.Constants;
import voteit.modules.exceptions.InvalidFormatException;
import voteit.modules.exceptions.LoginNotFoundException;

public class User {
  private String name;
  private String password;
  private int userId;
  private int token;
  private boolean admin;

  /**
   * Register a user
   * 
   * @param name
   * @param password
   * @throws InvalidFormatException
   * @throws SQLException
   * @throws LoginNotFoundException
   */
  public static void register(String name, String password, boolean admin)
      throws InvalidFormatException, SQLException, LoginNotFoundException {

    // Register user while checking for duplicate name
    try {
      VoteitDB.addUser(name, password, admin);
    } catch (SQLException e) {
      if (e.getSQLState().equals(Constants.SQLUNIQUEVIOLATION)) {
        throw new InvalidFormatException("user already exists");
      } else {
        throw e;
      }
    }

  }

  /**
   * Login with name and password and generate token
   * 
   * @param name
   * @param password
   * @return User object with valid token
   * @throws SQLException
   * @throws LoginNotFoundException
   */
  public User(String name, String password) throws SQLException, LoginNotFoundException {
    // Get user data
    ResultSet userRow = VoteitDB.getUser(name, password);
    if (userRow.next()) {
      // Get values and build user object
      this.name = userRow.getString("name");
      this.password = userRow.getString("password");
      this.userId = userRow.getInt("userId");
      this.admin = userRow.getBoolean("admin");

      // Generate new token
      int token = VoteitDB.createToken(userRow.getInt("userId"));
      this.token = token;
    } else {
      throw new LoginNotFoundException("name or password wrong");
    }
  }

  /**
   * Get user object from token
   * 
   * @param id
   * @return User object with valid token
   * @throws SQLException
   * @throws LoginNotFoundException
   */
  public User(int tokenId) throws SQLException, LoginNotFoundException {
    // Get token
    ResultSet token = VoteitDB.getToken(tokenId);
    if (token.next()) {
      // Get user from token
      ResultSet user = VoteitDB.getUser(token.getInt("userId"));
      user.next();

      // Fill values
      this.name = user.getString("name");
      this.password = user.getString("password");
      this.userId = user.getInt("userId");
      this.token = token.getInt("token");
      this.admin = user.getBoolean("admin");
    } else {
      throw new LoginNotFoundException("token invalid");
    }
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public int getUserId() {
    return userId;
  }

  public int getToken() {
    return token;
  }

  public boolean getAdmin() {
    return admin;
  }

  public void setName(String name) throws SQLException {
    VoteitDB.updateUser(name, this.password, this.admin, this.userId);
    this.name = name;
  }

  public void setPassword(String password) throws SQLException {
    VoteitDB.updateUser(this.name, password, this.admin, this.userId);
    this.password = password;
  }

  public void setAdmin(boolean admin) throws SQLException {
    VoteitDB.updateUser(this.name, this.password, admin, this.userId);
    this.admin = admin;
  }
}
