import PollModel from "models/pollModel";
import React, { useState } from "react";
import { usePollStore } from "store/pollStore";
import style from "styles/components/topicPopup.module.scss";
import Popup from "./popup";

interface TopicPopupProps {
  close: () => void;
  poll: PollModel;
  index: number;
}

export default function TopicPopup(props: TopicPopupProps) {
  const submitTopic = usePollStore((state) => state.submitTopic);
  const getPolls = usePollStore((state) => state.getPolls);
  const action = () => {
    submitTopic(props.poll, title, content);
    getPolls();
    props.close();
  };

  const [title, setTitel] = useState("");
  const [content, setContent] = useState("");
  return (
    <Popup close={props.close}>
      <form
        id={style.login}
        onSubmit={(e) => {
          e.preventDefault();
          action();
        }}
      >
        <h1>Fragestellung hinzufügen</h1>
        <input
          placeholder="Topic"
          value={title}
          onChange={(e) => setTitel(e.target.value)}
        ></input>
        <textarea
          placeholder="Weitere Informationen zur Fragestellung..."
          value={content}
          onChange={(e) => setContent(e.target.value)}
        ></textarea>

        <div className={style.vspacer}></div>
        <button className={style.button}>Hinzufügen</button>
      </form>
    </Popup>
  );
}
