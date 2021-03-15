import { useDispatch } from "react-redux";
import { delTopic } from "store/features/topics";
import style from "styles/components/TopicList.module.css";
import Spinner from "./spinner";

export interface TopicData {
  id: number;
  topic: string;
  percent: number;
}

export function createTopic(
  id: number,
  topic: string,
  percent: number
): TopicData {
  return { id: id, topic: topic, percent: percent };
}

interface TopicListProps {
  topics: TopicData[];
  manage: boolean;
}

export default function TopicList(props: TopicListProps) {
  return (
    <div className={style.content}>
      <div className={style.titleBar}>
        <span className={style.title}>Aktuelle Abstimmung</span>
        <span className={style.endDate}>Ende: 1.1.1990</span>
      </div>
      <hr />
      {props.topics.length == 0 ? (
        <Spinner></Spinner>
      ) : (
        <ul className={style.list}>
          {props.topics.map((e, index) => (
            <Topic
              key={e.id}
              topic={e.topic}
              percent={e.percent}
              manage={props.manage}
              index={index}
            ></Topic>
          ))}
        </ul>
      )}
    </div>
  );
}

interface TopicProps {
  topic: string;
  percent: number;
  manage: boolean;
  index: number;
}

function Topic(props: TopicProps) {
  const dispatch = useDispatch();

  let buttonsAdmin: JSX.Element[] = [
    <button
      className={style.topicVoteButton}
      onClick={() => dispatch(delTopic(props.index))}
      style={{ color: "red" }}
      key="adminDelete"
    >
      Delete
    </button>,
    <button
      className={style.topicVoteButton}
      onClick={() => console.log("hi!")}
      style={{ color: "orange" }}
      key="adminEdit"
    >
      Edit
    </button>,
  ];

  let buttonsUser: JSX.Element[] = [
    <button
      className={style.topicVoteButton}
      onClick={() => console.log("hi!")}
      key="userVote"
    >
      Vote
    </button>,
  ];

  return (
    <div className={style.topic}>
      <span className={style.topicText}>{props.topic}</span>
      <span className={style.topicPercent}>{props.percent}%</span>
      {props.manage ? buttonsAdmin : buttonsUser}
    </div>
  );
}
