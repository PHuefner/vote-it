import Header from "components/header";
import type { AppProps } from "next/app";
import "styles/globals.css";
import style from "styles/App.module.css";
import { Provider } from "react-redux";
import { store } from "store/store";

export default function App({ Component, pageProps }: AppProps) {
  return (
    <Provider store={store}>
      <div>
        <Header></Header>
        <div className={style.layout}>
          <Component {...pageProps} />
        </div>
      </div>
    </Provider>
  );
}
