import de from "date-fns/locale/de";
import PollModel from "models/pollModel";
import React, { useState } from "react";
import ReactDatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { usePollStore } from "store/pollStore";
import style from "styles/components/createPollPopup.module.scss";
import Popup from "./popup";

interface LoginPopupProps {
  close: () => void;
}

export default function CreatePollPopup(props: LoginPopupProps) {
  const { submitPoll, getPolls } = usePollStore((store) => ({
    submitPoll: store.submitPoll,
    getPolls: store.getPolls,
  }));
  const [place, setPlace] = useState("");
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState<Date>();
  const [eventDate, setEventDate] = useState<Date>();

  const action = async () => {
    await submitPoll(new PollModel(place, startDate, endDate, eventDate));
    getPolls();
    props.close();
  };

  return (
    <Popup close={props.close}>
      <form
        id={style.post}
        onSubmit={(e) => {
          e.preventDefault();
          action();
        }}
      >
        <h1>Create a poll</h1>

        <input
          placeholder="Ort"
          value={place}
          onChange={(e) => setPlace(e.target.value)}
        ></input>

        <div className={style.datePicker}>
          <span>Abstimmung ist vom</span>
          <ReactDatePicker
            selectsStart
            selected={startDate}
            startDate={startDate}
            endDate={endDate}
            showTimeSelect
            timeFormat="p"
            timeIntervals={15}
            dateFormat="Pp"
            locale={de}
            timeCaption="Uhrzeit"
            onChange={(date) => setStartDate(date as Date)}
          ></ReactDatePicker>
          <span>bis zum</span>
          <ReactDatePicker
            selectsEnd
            selected={endDate}
            startDate={startDate}
            endDate={endDate}
            showTimeSelect
            timeFormat="p"
            timeIntervals={15}
            dateFormat="Pp"
            locale={de}
            timeCaption="Uhrzeit"
            onChange={(date) => setEndDate(date as Date)}
          ></ReactDatePicker>
        </div>

        <div className={style.datePicker}>
          <span>Findet statt am:</span>
          <ReactDatePicker
            selected={eventDate}
            showTimeSelect
            timeFormat="p"
            timeIntervals={15}
            dateFormat="Pp"
            locale={de}
            timeCaption="Uhrzeit"
            onChange={(date) => setEventDate(date as Date)}
          ></ReactDatePicker>
        </div>

        <div className={style.vspacer}></div>
        <button className={style.button}>Post</button>
      </form>
    </Popup>
  );
}
