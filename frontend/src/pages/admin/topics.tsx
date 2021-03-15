import TopicList, { TopicData } from "components/topicList";
import { useDispatch, useSelector } from "react-redux";
import { RootState } from "store/store";

export default function topics() {
  const topics = useSelector((state: RootState) => state.topics.topics);

  return (
    <div>
      <TopicList topics={topics} manage={true}></TopicList>
    </div>
  );
}
