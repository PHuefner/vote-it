import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface logInPayload {
  username: string;
  password: string;
}

export const user = createSlice({
  name: "user",
  initialState: {
    loggedIn: false,
    name: "",
  },
  reducers: {
    logIn: (state, action: PayloadAction<logInPayload>) => {
      state.loggedIn = true;
      state.name = action.payload.username;
    },
    logOut: (state) => {
      state.loggedIn = false;
      state.name = "";
    },
  },
});

export const { logIn, logOut } = user.actions;

export const userReducer = user.reducer;
