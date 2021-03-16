package voteit.resources;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Poll {

    String place;
    Date pollBegin;
    Date date;
    int pollId;

    ArrayList<Topic> topics;

    public Poll(Date date, Date pollBegin, String place) {
        this.date = date;
        this.pollBegin = pollBegin;
        this.place = place;

        topics = new ArrayList<>();
    }

    public void addThema(Topic topic) {
        topics.add(topic);
    }

    public void addThemen(Collection<Topic> topics) {
        topics.addAll(topics);
    }

    public int ermittleSieger() {
        if (topics.size() == 0) {
            return -1;
        }
        Topic winner = null;
        int mostVotes = 0;
        for (Topic topic : topics) {
            if (topic.votes > mostVotes) {
                winner = topic;
                mostVotes = topic.votes;
            }
        }
        return winner.userId;
    }

}
