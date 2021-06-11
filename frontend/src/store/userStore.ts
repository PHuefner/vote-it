import create, { State } from "zustand";
import UserModel from "../models/userModel";

interface UserStore extends State {
  user: UserModel;
  login: (user: string, password: string) => Promise<void>;
  register: (user: string, password: string) => Promise<void>;
  checkLogin: () => Promise<void>;
  logout: () => Promise<void>;
}

export const useUserStore = create<UserStore>((set, get) => ({
  user: null,
  login: async (user, password) => {
    let res = await fetch("http://kucera-server.de/api/user/login", {
      method: "POST",
      body: JSON.stringify({ name: user, password: password }),
      credentials: "include",
    });
    if (res.ok) {
      get().checkLogin();
    } else {
      throw new Error(await res.text())
    }
  },
  register: async (user, password) => {
    let res = await fetch("http://kucera-server.de/api/user/register", {
      method: "POST",
      body: JSON.stringify({ name: user, password: password }),
      credentials: "include",
    });
    if (res.ok) {
      get().checkLogin();
    } else {
      let text = await res.text()
      if (text.trim() == "user already exists") {
        text = "Name schon in Verwendung"
        console.log("boi")
      }
      throw new Error(text)
    }
  },
  logout: async () => {
    let res = await fetch("http://kucera-server.de/api/user/logout", {
      credentials: "include",
    });
    if (res.ok) {
      location.reload()
      return;
    } else {
      throw new Error(await res.text())
    }
  },
  checkLogin: async () => {
    try {
      let res = await fetch("http://kucera-server.de/api/user/data", {
        credentials: "include",
      });
      if (res.ok) {
        let user = await res.json();
        set({ user: new UserModel(user.userId,user.name,user.admin) });
      } else {
        set({ user: null });
      }
    } catch (error) {}
  },
}));
