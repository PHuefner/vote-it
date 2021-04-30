import Header from "components/header";
import { useEffect, useState } from "react";
import style from "styles/pages/index.module.scss";

export default function Index() {
  const [polls, setPolls] = useState([]);
  useEffect(() => {
    fetch("http://localhost:3001/api/poll/get").then(async (res) => {
      setPolls(JSON.parse(await res.text()));
    });
  }, []);

  return (
    <div>
      <Header />
      <div id={style.main}>
        <h1 id={style.pageTitle}>Polls</h1>
        {polls.map((el) => {
          return (
            <div>
              <span>Ort: {el.place}</span>
              <br />
              <span>Meeting: {new Date(el.date).toString()}</span>
            </div>
          );
        })}
      </div>
    </div>
  );
}
