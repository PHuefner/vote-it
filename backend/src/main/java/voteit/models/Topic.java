package voteit.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import voteit.VoteitDB;
import voteit.modules.exceptions.ResourceNotFoundException;

public class Topic {

  int topicId;
  String title;
  String content;
  int votes;
  int userId;
  int pollId;

  /**
   * Add topic to a poll
   * 
   * @param title
   * @param content
   * @param user
   * @param poll
   * @throws SQLException
   */
  public static void submit(String title, String content, User user, Poll poll) throws SQLException {
    VoteitDB.addTopic(title, content, user.getUserId(), poll.getPollId());
  }

  /**
   * Build raw topic object
   * 
   * @param topicId
   * @param title
   * @param content
   * @param userId
   * @param pollId
   */
  protected Topic(int topicId, String title, String content, int votes, int userId, int pollId) {
    this.topicId = topicId;
    this.title = title;
    this.content = content;
    this.votes = votes;
    this.userId = userId;
    this.pollId = pollId;
  }

  /**
   * Get all topics of a poll
   * 
   * @param pollId
   * @return
   * @throws SQLException
   */
  public static ArrayList<Topic> getTopics(int pollId) throws SQLException {
    ArrayList<Topic> topics = new ArrayList<>();
    ResultSet topicSet = VoteitDB.getTopics(pollId);
    while (topicSet.next()) {
      int topicId = topicSet.getInt("topicId");
      String title = topicSet.getString("title");
      String content = topicSet.getString("content");
      int votes = topicSet.getInt("votes");
      int userId = topicSet.getInt("userId");

      topics.add(new Topic(topicId, title, content, votes, userId, pollId));
    }
    return topics;
  }

  public static Topic getTopic(int topicId) throws SQLException, ResourceNotFoundException {
    ResultSet topicSet = VoteitDB.getTopic(topicId);
    if (!topicSet.next()) {
      throw new ResourceNotFoundException("topic");
    }
    int foundTopicId = topicSet.getInt("topicId");
    String title = topicSet.getString("title");
    String content = topicSet.getString("content");
    int votes = topicSet.getInt("votes");
    int userId = topicSet.getInt("userId");
    int pollId = topicSet.getInt("pollId");
    return new Topic(foundTopicId, title, content, votes, userId, pollId);
  }

  public void delete() throws SQLException {
    VoteitDB.deleteTopic(topicId);
  }

  public int getTopicId() {
    return topicId;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public int getVotes() {
    return votes;
  }

  public int getUserId() {
    return userId;
  }

  public int getPollId() {
    return pollId;
  }

}
