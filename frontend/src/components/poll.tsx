import PollModel from "models/pollModel";
import React, { useEffect, useState } from "react";
import style from "styles/components/poll.module.scss";
import TopicPopup from "components/topicPopup";

interface PollProps {
  pollModel: PollModel;
}

export default function Poll(props: PollProps) {
  const [topicPopup, setTopicPopup] = useState(false);

  let poll = props.pollModel;
  let id = poll.id;
  let begun = poll.begin.valueOf() > new Date().valueOf() ? false : true;
  let beginDate = shortDate(poll.begin);
  let endDate = shortDate(poll.end);
  let eventDate = shortDate(poll.date);

  return (
    <div id={style.poll}>
      <h2>Abstimmung für den {eventDate}</h2>
      <div id={style.header}>
        <span>Ort: {poll.place}</span>
        <div className={style.seperator}></div>
        <span>
          {begun
            ? "Abstimmung endet am " + endDate
            : "Abstimmung beginnt am " + beginDate}
        </span>
      </div>
      <hr />
      <div>
        {poll.topics
          ? props.pollModel.topics.map((el) => (
              <div className={style.topic}>
                <span>{el.title}</span>
                <div className={style.seperator} />
                <span>40%</span>
                <button className={style.upvote}>↑</button>
              </div>
            ))
          : null}
      </div>
      <button className={style.button} onClick={() => setTopicPopup(true)}>
        Fragestellung hinzufügen
      </button>

      {topicPopup ? (
        <TopicPopup close={() => setTopicPopup(false)} poll={poll}></TopicPopup>
      ) : null}
    </div>
  );
}

function shortDate(date: Date): string {
  return (
    date.getDate() +
    "." +
    date.getMonth() +
    " " +
    date.getHours() +
    ":" +
    date.getMinutes().toString().padStart(2, "0")
  );
}
