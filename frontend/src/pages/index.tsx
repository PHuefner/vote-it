import {
  Box,
  Button,
  Container,
  Grid,
  IconButton,
  Snackbar,
  SnackbarContent,
  styled,
  Typography,
  useMediaQuery,
} from "@material-ui/core";
import { Close } from "@material-ui/icons";
import React, { useEffect, useState } from "react";
import Header from "../components/header";
import Poll from "../components/poll";
import PollModel from "../models/pollModel";
import TopicModel from "../models/topicModel";
import { usePollStore } from "../store/pollStore";
import { useUserStore } from "../store/userStore";

export default function Home() {
  const phone = useMediaQuery("(min-width:900px)");
  console.log(phone);
  const MainContainer = styled(Container)({
    maxWidth: phone ? "70%" : "100%",
    margin: "auto",
  });

  const userStore = useUserStore();
  useEffect(() => {
    userStore.checkLogin();
    return;
  }, []);

  const pollStore = usePollStore();
  useEffect(() => {
    (async () => {
      try {
        await pollStore.getPolls();
      } catch (error) {
        setSnackMessage(error.message);
        setSnackState(true);
      }
    })();
    return;
  }, []);
  const pollData = pollStore.polls;

  const [snackState, setSnackState] = useState(false);
  const [snackMessage, setSnackMessage] = useState("");

  return (
    <Box>
      <Header></Header>
      <MainContainer>
        <Typography variant="h3">Abstimmungen</Typography>
        <Grid container spacing={2} direction="column">
          {pollData.map((el) => (
            <Grid item>
              <Poll pollData={el}></Poll>
            </Grid>
          ))}
        </Grid>
      </MainContainer>
      <Snackbar
        open={snackState}
        autoHideDuration={5000}
        onClose={() => setSnackState(false)}
      >
        <SnackbarContent
          message={snackMessage}
          action={
            <IconButton onClick={() => setSnackState(false)} color="inherit">
              <Close />
            </IconButton>
          }
        ></SnackbarContent>
      </Snackbar>
    </Box>
  );
}
