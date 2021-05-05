import React, { useState } from "react";
import { useUserStore } from "store/userStore";
import style from "styles/components/loginPopup.module.scss";
import Popup from "./popup";

interface LoginPopupProps {
  close: () => void;
  register: boolean;
}

export default function LoginPopup(props: LoginPopupProps) {
  const { login, register } = useUserStore((store) => ({
    login: store.login,
    register: store.register,
  }));
  const [user, setUser] = useState("");
  const [password, setPassword] = useState("");

  const action = () => {
    if (props.register) {
      register(user, password);
    } else {
      login(user, password);
    }
    props.close();
  };

  const buttonText = props.register ? "Register" : "Login";
  return (
    <Popup close={props.close}>
      <form
        id={style.login}
        onSubmit={(e) => {
          e.preventDefault();
          action();
        }}
      >
        <h1>{buttonText}</h1>
        <input
          placeholder="Username"
          value={user}
          onChange={(e) => setUser(e.target.value)}
        ></input>
        <input
          placeholder="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        ></input>
        <div className={style.vspacer}></div>
        <button className={style.button}>{buttonText}</button>
      </form>
    </Popup>
  );
}
