import { useEffect } from "react";
import { useUserStore } from "store/userStore";
import "../styles/globals.scss";

function MyApp({ Component, pageProps }) {
  const checkLogin = useUserStore((state) => state.checkLogin);
  useEffect(() => checkLogin(), []);

  return <Component {...pageProps} />;
}

export default MyApp;
