import Header from "components/header";
import { Router, useRouter } from "next/dist/client/router";
import React, { ChangeEvent, FormEvent, useState } from "react";
import { useDispatch } from "react-redux";
import { logIn } from "store/features/user";
import style from "styles/pages/Login.module.css";

export default function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const dispatch = useDispatch();
  const router = useRouter();

  const handleSubmit = function (e: FormEvent) {
    e.preventDefault();
    dispatch(logIn({ user: username, password: password }));
    console.log("boi");
    router.push("/");
  };

  return (
    <div>
      <div className={style.content}>
        <span className={style.title}>Login</span>
        <form className={style.form} onSubmit={handleSubmit}>
          <input
            name="name"
            type="text"
            placeholder="Username"
            className={style.textInput}
            onChange={(e) => setUsername(e.target.value)}
            value={username}
          />
          <input
            name="pass"
            type="password"
            placeholder="Password"
            className={style.textInput}
            onChange={(e) => setPassword(e.target.value)}
            value={password}
          />
          <button className={style.button} type="submit">
            Log In
          </button>
        </form>
      </div>
    </div>
  );
}
