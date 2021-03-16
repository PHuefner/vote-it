package voteit.resources;

public class Topic {

    String titel;
    int votes;
    String content;
    int pollId;
    int userId;
    int topicId;

    public Topic(String titel, String content) {
        this.titel = titel;
        this.content = content;
    }

    public Topic(String titel, String content, int votes, int pollId, int userId, int topicId) {
        this.titel = titel;
        this.content = content;
        this.votes = votes;
        this.pollId = pollId;
        this.userId = userId;
        this.topicId = topicId;
    }

    public void setBenutzerId(int userId) {
        this.userId = userId;
    }

}
