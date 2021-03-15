import React from "react";
import style from "styles/components/Header.module.css";
import Link from "next/link";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "store/store";
import { logIn, logOut } from "store/features/user";

export default function Header() {
  const dispatch = useDispatch();
  const user = useSelector((state: RootState) => state.users);
  return (
    <div className={style.header}>
      <Link href="/">
        <div className={style.title}>vote.it</div>
      </Link>
      <div className={style.divider}></div>

      {/* Topic Management */}
      {user.loggedIn
        ? [
            <Link href="/admin/topics" key="adminTopicManagement">
              <div className={style.Button}>
                <span className={style.buttonText}>Manage Topics</span>
              </div>
            </Link>,
            <Link href="/user/submit" key="userTopicSubmit">
              <div className={style.Button}>
                <span className={style.buttonText}>Submit a topic</span>
              </div>
            </Link>,
          ]
        : null}

      {/* Login / Logout Button */}
      {user.loggedIn ? (
        [
          <div className={style.Button}>
            <span
              className={style.buttonText}
              onClick={() => dispatch(logOut())}
            >
              Logout!
            </span>
          </div>,
          <span className={style.loggedInText}>Logged in as: {user.name}</span>,
        ]
      ) : (
        <Link href="/login">
          <div className={style.Button}>
            <span className={style.buttonText}>Login</span>
          </div>
        </Link>
      )}
    </div>
  );
}
