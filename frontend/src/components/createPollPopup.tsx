import React, { useState } from "react";
import ReactDatePicker, {
  registerLocale,
  setDefaultLocale,
} from "react-datepicker";
import style from "styles/components/createPollPopup.module.scss";
import Popup from "./popup";

import "react-datepicker/dist/react-datepicker.css";
import de from "date-fns/locale/de";

interface LoginPopupProps {
  close: () => void;
}

export default function CreatePollPopup(props) {
  const [place, setPlace] = useState("");
  const [startDate, setStartDate] = useState(new Date());
  const [endDate, setEndDate] = useState<Date>();
  const [eventDate, setEventDate] = useState<Date>();

  const submit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    await fetch("http://localhost:3001/api/poll/create", {
      method: "POST",
      credentials: "include",
      body: JSON.stringify({
        place: place,
        pollBegin: startDate.valueOf(),
        date: eventDate.valueOf(),
        pollEnd: endDate.valueOf(),
      }),
    });
  };

  return (
    <Popup close={props.close}>
      <form id={style.post} action="#" onSubmit={submit}>
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
            showTimeSelect
            timeFormat="p"
            timeIntervals={15}
            dateFormat="Pp"
            locale={de}
            timeCaption="Uhrzeit"
            endDate={endDate}
            onChange={(date) => setStartDate(date as Date)}
          ></ReactDatePicker>
          <span>bis zum</span>
          <ReactDatePicker
            startDate={startDate}
            endDate={endDate}
            minDate={startDate}
            showTimeSelect
            timeFormat="p"
            locale={de}
            timeIntervals={15}
            dateFormat="Pp"
            timeCaption="Uhrzeit"
            selected={endDate}
            onChange={(date) => setEndDate(date as Date)}
          ></ReactDatePicker>
        </div>

        <div className={style.datePicker}>
          <span>Findet statt am:</span>
          <ReactDatePicker
            selectsStart
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
