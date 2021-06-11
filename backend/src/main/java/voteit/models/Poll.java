package voteit.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import voteit.VoteitDB;
import voteit.modules.exceptions.ResourceNotFoundException;

public class Poll {
  private int pollId;
  private String place;
  private long pollEnd;
  private long date;

  /**
   * Create new poll
   * 
   * @param place
   * @param pollEnd
   * @param date
   * @throws SQLException
   */
  public static void add(String place, long pollEnd, long date) throws SQLException {
    VoteitDB.addPoll(place, pollEnd, date);
  }

  /**
   * Build raw Poll
   * 
   * @param pollId
   * @param place
   * @param pollEnd
   * @param date
   */
  private Poll(int pollId, String place, long pollEnd, long date) {
    this.pollId = pollId;
    this.place = place;
    this.pollEnd = pollEnd;
    this.date = date;
  }

  /**
   * Get poll from id
   * 
   * @param pollId
   * @throws SQLException
   * @throws ResourceNotFoundException
   */
  public Poll(int pollId) throws SQLException, ResourceNotFoundException {
    ResultSet pollSet = VoteitDB.getPoll(pollId);
    if (pollSet.next()) {
      this.pollId = pollSet.getInt("pollId");
      this.place = pollSet.getString("place");
      this.pollEnd = pollSet.getTimestamp("pollEnd").getTime();
      this.date = pollSet.getTimestamp("date").getTime();
    } else {
      throw new ResourceNotFoundException("poll");
    }
  }

  /**
   * Build poll array
   * 
   * @throws SQLException
   * @return Array of polls
   */
  public static ArrayList<Poll> getPolls() throws SQLException {
    // Get all polls
    ResultSet pollSet = VoteitDB.getPolls();
    ArrayList<Poll> polls = new ArrayList<>();

    // Transform poll ResultSet to Objects
    while (pollSet.next()) {
      polls.add(new Poll(pollSet.getInt("pollId"), pollSet.getString("place"),
          pollSet.getTimestamp("pollEnd").getTime(), pollSet.getTimestamp("date").getTime()));
    }
    return polls;
  }

  public int getPollId() {
    return pollId;
  }

  public String getPlace() {
    return place;
  }

  public long getPollEnd() {
    return pollEnd;
  }

  public long getDate() {
    return date;
  }

  public void update(String place, long pollEnd, long date) throws SQLException {
    VoteitDB.updatePoll(place, pollEnd, date, pollId);
    this.place = place;
    this.pollEnd = pollEnd;
    this.date = date;
  }

}
