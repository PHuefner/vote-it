import { useState } from "react";
import style from "styles/components/popup.module.scss";

interface PopupProps {
  close: () => void;
  children: JSX.Element;
}

export default function Popup(props: PopupProps) {
  return (
    <div id={style.popup} onClick={props.close}>
      <div id={style.popupInner} onClick={(e) => e.stopPropagation()}>
        <div id={style.close} onClick={props.close}>
          X
        </div>
        {props.children}
      </div>
    </div>
  );
}
