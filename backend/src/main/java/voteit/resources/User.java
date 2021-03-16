package voteit.resources;

public class User {

    String name;
    String lastname;
    String password;
    int userId;

    public Topic themaBearbeiten(int topicId, String titel, String content) {
        return null;
    }

    public Topic themaEinreichen(String titel, String content) {
        Topic t = new Topic(titel, content);
        t.setBenutzerId(userId);
        return t;
    }

}
