import TopicList, { TopicData } from "components/topicList";
import { useDispatch, useSelector } from "react-redux";
import { addTopic, fetchTopic } from "store/features/topics";
import { RootState } from "store/store";
import style from "styles/pages/Index.module.css";

export default function Index() {
  const topics = useSelector((state: RootState) => state.topics.topics);
  const dispatch = useDispatch();

  return (
    <div>
      <button onClick={() => dispatch(fetchTopic())}>
        Get Topics from TestAPI
      </button>
      <TopicList topics={topics} manage={false}></TopicList>
    </div>
  );
}
