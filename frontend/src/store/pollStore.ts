import PollModel from "../models/pollModel";
import TopicModel from "../models/topicModel";
import create, { State } from "zustand";

interface PollStore extends State {
  polls: Array<PollModel>;
  submitPoll: (poll: PollModel) => Promise<void>;
  getPolls: () => Promise<void>;
  submitTopic: (
    poll: PollModel,
    title: string,
    content: string
  ) => Promise<void>;
  setTopicVote: (topicId: number, voted: boolean) => Promise<void>;
  deleteTopic: (topic: TopicModel) => Promise<void>;
}

export const usePollStore = create<PollStore>((set, get) => ({
  polls: [],
  submitPoll: async (poll: PollModel) => {
    let req = await fetch("http://kucera-server.de/api/poll/create", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        place: poll.place,
        pollEnd: poll.end.valueOf(),
        date: poll.date.valueOf(),
      }),
    });
    if (req.ok) {
      get().getPolls();
    } else {
      throw new Error(await req.text());
    }
  },
  getPolls: async () => {
    //Fetch all polls
    let req = await fetch("http://kucera-server.de/api/poll/get");
    if (!req.ok) {
      throw new Error(await req.text())
    }
    let json = await req.json()
    let polls = [];
    json.map((el) => {
      polls.push(
        new PollModel(el.place, el.pollEnd, el.date, el.pollId)
      );
    });
    //Set polls to display before loading topics
    set({ polls: polls });
    //Fetch and set topics seperate for each topic
    polls.map(async (poll, index) => {
      let topicRes = 
        await fetch("http://kucera-server.de/api/topic/get", {
          method: "POST",
          credentials: "include",
          body: JSON.stringify({ pollId: poll.id }),
        })
      if (!topicRes.ok) {
        throw new Error(await topicRes.text())
      }
      let topics = await topicRes.json()
      let newTopics: TopicModel[] = [];
      for (const topic of topics) {
        newTopics.push(
          new TopicModel(
            topic.topicId,
            topic.title,
            topic.content,
            topic.votes,
            topic.voted,
            topic.userId,
          )
        );
      }
      let newPolls = get().polls;
      newPolls[index].topics = newTopics;
      set({ polls: newPolls });
    });
  },
  deleteTopic: async (topic: TopicModel) => {
    let req = await fetch("http://kucera-server.de/api/topic/delete", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        topicId: topic.id
      })
    })
    if (!req.ok) {
      throw new Error(await req.text());
    } else {
      get().getPolls();
    }
  },
  submitTopic: async (poll: PollModel, title: string, content: string) => {
    let req = await fetch("http://kucera-server.de/api/topic/submit", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        pollId: poll.id,
        title: title,
        content: content,
      }),
    });
    if (!req.ok) {
      throw new Error(await req.text())
    }
    get().getPolls();
  },
  setTopicVote: async (topicId: number, voted: boolean) => {
    let res = await fetch("http://kucera-server.de/api/topic/vote", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        topicId: topicId,
        voted: voted,
      }),
    });
    if (!res.ok) {
      throw new Error(await res.text())
    }
    get().getPolls();
  },
}));
