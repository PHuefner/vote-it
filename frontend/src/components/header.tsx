import React, { useState } from "react";
import { useUserStore } from "store/userStore";
import style from "styles/components/header.module.scss";
import CreatePollPopup from "./createPollPopup";
import LoginPopup from "./loginPopup";

export default function Header() {
  const [showLogin, setShowLogin] = useState(false);
  const [register, setRegister] = useState(false);
  const [showPost, setShowPost] = useState(false);
  const { username, logout } = useUserStore((store) => ({
    username: store.name,
    logout: store.logout,
  }));

  const userButtons = [
    <button onClick={() => setShowPost(true)} className={style.button}>
      Post
    </button>,
    <button onClick={() => logout()} className={style.button}>
      Logout
    </button>,
  ];

  const guestButtons = [
    <button
      onClick={() => {
        setRegister(true);
        setShowLogin(true);
      }}
      className={style.button}
    >
      Register
    </button>,
    <button
      onClick={() => {
        setRegister(false);
        setShowLogin(true);
      }}
      className={style.button}
    >
      Login
    </button>,
  ];

  return (
    <div id={style.header}>
      <h1 id={style.title}>{username}</h1>

      <div className={style.seperator}></div>

      {username ? userButtons : guestButtons}

      {/* Popups */}
      {showLogin ? (
        <LoginPopup close={() => setShowLogin(false)} register={register} />
      ) : null}

      {showPost ? <CreatePollPopup close={() => setShowPost(false)} /> : null}
    </div>
  );
}
