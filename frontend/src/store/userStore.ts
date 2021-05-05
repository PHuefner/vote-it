import { userInfo } from "node:os";
import create, { State } from "zustand";

interface UserStore extends State {
  name: string;
  login: (user: string, password: string) => void;
  register: (user: string, password: string) => void;
  checkLogin: () => void;
  logout: () => void;
}

export const useUserStore = create<UserStore>((set, get) => ({
  name: "",
  login: async (user, password) => {
    let res = await fetch("http://localhost:3001/api/user/login", {
      method: "POST",
      body: JSON.stringify({ name: user, password: password }),
      credentials: "include",
    });
    if (res.ok) {
      get().checkLogin();
    }
  },
  register: async (user, password) => {
    let res = await fetch("http://localhost:3001/api/user/register", {
      method: "POST",
      body: JSON.stringify({ name: user, password: password }),
      credentials: "include",
    });
    if (res.ok) {
      get().checkLogin();
    }
  },
  logout: async () => {
    let res = await fetch("http://localhost:3001/api/user/logout", {
      credentials: "include",
    });
    if (res.ok) {
      get().checkLogin();
    }
  },
  checkLogin: async () => {
    try {
      let res = await fetch("http://localhost:3001/api/user/data", {
        credentials: "include",
      });
      if (res.ok) {
        let user = await res.json();
        set({ name: user.name });
      } else {
        set({ name: "" });
      }
    } catch (error) {}
  },
}));
