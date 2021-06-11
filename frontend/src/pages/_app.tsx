import React, { useEffect } from "react";
import Head from "next/head";
import { createMuiTheme, CssBaseline, ThemeProvider } from "@material-ui/core";
import { useUserStore } from "../store/userStore";

function MyApp({ Component, pageProps }) {
  const theme = createMuiTheme({ palette: { type: "dark" } });
  return (
    <div>
      <Head>
        <link
          rel="stylesheet"
          href="https://fonts.googleapis.com/css?family=Roboto:300,400,500,700&display=swap"
        />
      </Head>
      <ThemeProvider theme={theme}>
        <CssBaseline></CssBaseline>
        <Component {...pageProps} />
      </ThemeProvider>
    </div>
  );
}

export default MyApp;
