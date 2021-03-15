import { configureStore } from "@reduxjs/toolkit";
import { topicReducer } from "./features/topics";
import { userReducer } from "./features/user";

export const store = configureStore({
  reducer: {
    topics: topicReducer,
    users: userReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
