package voteit.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import voteit.VoteitDB;
import voteit.modules.exceptions.ResourceNotFoundException;

public class TopicVoted extends Topic {
  int voterId;
  boolean voted;

  protected TopicVoted(int topicId, String title, String content, int userId, int pollId, int voterId, boolean voted) {
    super(topicId, title, content, userId, pollId);
    this.voterId = voterId;
    this.voted = voted;
  }

  public static TopicVoted fromIds(int currTopicId, int voterId) throws SQLException, ResourceNotFoundException {
    ResultSet topicSet = VoteitDB.getTopicVoted(currTopicId, voterId);
    if (topicSet.next()) {
      int topicId = topicSet.getInt("topicId");
      String title = topicSet.getString("title");
      String content = topicSet.getString("content");
      int userId = topicSet.getInt("userId");
      int pollId = topicSet.getInt("pollId");
      boolean voted = topicSet.getBoolean("voted");
      return new TopicVoted(topicId, title, content, userId, pollId, voterId, voted);
    } else {
      throw new ResourceNotFoundException("topic");
    }
  }

  public static ArrayList<Topic> getTopicsVoted(int pollId, int voterId) throws SQLException {
    ArrayList<Topic> topics = new ArrayList<>();
    ResultSet topicSet = VoteitDB.getTopicsVoted(pollId, voterId);
    while (topicSet.next()) {
      int topicId = topicSet.getInt("topicId");
      String title = topicSet.getString("title");
      String content = topicSet.getString("content");
      int userId = topicSet.getInt("userId");
      boolean voted = topicSet.getBoolean("voted");

      topics.add(new TopicVoted(topicId, title, content, userId, pollId, voterId, voted));
    }
    return topics;
  }

  public boolean getVoted() {
    return this.voted;
  }

  public void setVoted(boolean voted) throws SQLException {
    if (voted) {
      VoteitDB.setVote(this.topicId, this.voterId);
    } else {
      VoteitDB.deleteVote(this.topicId, this.voterId);
    }
    this.voted = voted;
  }

}
