import PollModel from "models/pollModel";
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
}

export const usePollStore = create<PollStore>((set, get) => ({
  polls: [],
  submitPoll: async (poll: PollModel) => {
    await fetch("http://localhost:3001/api/poll/create", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        place: poll.place,
        pollBegin: poll.begin.valueOf(),
        pollEnd: poll.end.valueOf(),
        date: poll.date.valueOf(),
      }),
    });
    get().getPolls();
  },
  getPolls: async () => {
    //Fetch all polls
    let res = await (await fetch("http://localhost:3001/api/poll/get")).json();
    let polls = [];
    res.map((el) => {
      polls.push(
        new PollModel(el.place, el.pollBegin, el.pollEnd, el.date, el.pollId)
      );
    });
    //Set polls to display before loading topics
    set({ polls: polls });
    //Fetch and set topics seperate for each topic
    polls.map(async (poll, index) => {
      let topics = await (
        await fetch("http://localhost:3001/api/topic/get", {
          method: "POST",
          body: JSON.stringify({ pollId: poll.id }),
        })
      ).json();
      let newPolls = get().polls;
      newPolls[index].topics = topics;
      set({ polls: newPolls });
    });
  },
  submitTopic: async (poll: PollModel, title: string, content: string) => {
    fetch("http://localhost:3001/api/topic/submit", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        pollId: poll.id,
        title: title,
        content: content,
      }),
    });
  },
}));
