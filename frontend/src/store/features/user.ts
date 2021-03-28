import { createSlice, PayloadAction } from "@reduxjs/toolkit";

interface logInPayload {
  user: string;
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
      fetch("http://localhost:3001/api/user/login",{method:"POST",body:JSON.stringify({user:action.payload.user, password: action.payload.password})});
    },
    logOut: (state) => {
      state.loggedIn = false;
      state.name = "";
    },
  },
});

export const { logIn, logOut } = user.actions;

export const userReducer = user.reducer;
