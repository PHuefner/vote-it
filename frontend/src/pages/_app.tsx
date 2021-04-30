import { useEffect } from "react";
import { useStore } from "store/store";
import "../styles/globals.scss";

function MyApp({ Component, pageProps }) {
  const login = useStore((state) => state.checkLogin);
  useEffect(() => login(), []);

  return <Component {...pageProps} />;
}

export default MyApp;
