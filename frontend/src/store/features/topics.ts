import { createAsyncThunk, createSlice, PayloadAction } from "@reduxjs/toolkit";
import { createTopic, TopicData } from "components/topicList";
import { store } from "store/store";

export const fetchTopic = createAsyncThunk("topics/fetchTopic", async () => {
  console.log("boi");
  const response = await (
    await fetch("http://77.179.142.208:3000/api/topic")
  ).json();
  console.log(response);
  let topics: TopicData[] = [];
  response.forEach((element) => {
    topics.push(
      createTopic(element.abstimmungsId, element.inhalt, element.votes)
    );
  });
  return topics;
});

interface addTopicPayload {
  topic: string;
}

export const topicSlice = createSlice({
  name: "topics",
  initialState: {
    topics: [],
    latestID: 1,
  },
  reducers: {
    addTopic: (state, { payload }: PayloadAction<addTopicPayload>) => {
      let id = state.topics[state.topics.length - 1];
      state.topics.push(
        createTopic(state.latestID, payload.topic, state.latestID)
      );
      state.latestID++;
    },
    delTopic: (state, { payload }: PayloadAction<number>) => {
      state.topics.splice(payload, 1);
    },
  },
  extraReducers: (builder) => {
    builder.addCase(fetchTopic.fulfilled, (state, action) => {
      console.log(action);
      state.topics = action.payload;
    });
  },
});

export const { addTopic, delTopic } = topicSlice.actions;

export const topicReducer = topicSlice.reducer;
