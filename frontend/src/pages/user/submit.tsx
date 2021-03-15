import { Router, useRouter } from "next/dist/client/router";
import { ChangeEvent, FormEvent, useState } from "react";
import { useDispatch } from "react-redux";
import { addTopic } from "store/features/topics";
import style from "styles/pages/user/Submit.module.css";

export default function submit() {
  const [topicText, setTopicText] = useState("");
  const router = useRouter();

  const dispatch = useDispatch();
  const onSubmit = (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    dispatch(addTopic({ topic: topicText }));
    router.push("/");
  };

  const setTopic = (event: ChangeEvent<HTMLInputElement>) => {
    setTopicText(event.target.value);
    event.target.value = topicText;
  };

  return (
    <div className={style.content}>
      <span className={style.title}>Submit a new Topic!</span>
      <form className={style.form} onSubmit={onSubmit}>
        <input
          type="text"
          placeholder="Topic"
          className={style.textInput}
          onChange={(e) => setTopic(e)}
          value={topicText}
        />
        <button className={style.button} type="submit">
          Submit
        </button>
      </form>
    </div>
  );
}

function onSubmit(event) {
  event.preventDefault();
  console.log("fdsa");
  // custom form handling here
}
