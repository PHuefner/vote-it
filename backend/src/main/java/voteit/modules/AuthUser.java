package voteit.modules;

import java.sql.ResultSet;
import java.sql.SQLException;

import voteit.VoteitDB;

public class AuthUser {
  private int id;
  private String name;

  public AuthUser(int token) throws SQLException, LoginNotFoundException {
    ResultSet user = VoteitDB.getUserFromToken(token);
    user.next();
    this.id = user.getInt("userId");
    this.name = user.getString("name");
  }

  public AuthUser(String token) throws SQLException, LoginNotFoundException {
    try {
      int tokenConverted = Integer.parseInt(token);
      ResultSet user = VoteitDB.getUserFromToken(tokenConverted);
      user.next();
      this.id = user.getInt("userId");
      this.name = user.getString("name");
    } catch (NumberFormatException e) {
      throw new LoginNotFoundException("token syntax invalid");
    }
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }
}
