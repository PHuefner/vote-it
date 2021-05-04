import Header from "components/header";
import Poll from "components/poll";
import PollModel from "models/pollModel";
import { useEffect, useState } from "react";
import { usePollStore } from "store/pollStore";
import style from "styles/pages/index.module.scss";

export default function Index() {
  const pollStore = usePollStore();
  useEffect(() => {
    pollStore.getPolls();
  }, []);

  return (
    <div>
      <Header />
      <div id={style.main}>
        <h1 id={style.pageTitle}>Abstimmungen</h1>
        {pollStore.polls.map((el, index) => {
          return <Poll pollModel={el} index={index}></Poll>;
        })}
      </div>
    </div>
  );
}
