import {
  Box,
  Button,
  Container,
  Divider,
  Grid,
  Paper,
  styled,
  Typography,
  useMediaQuery,
  useTheme,
} from "@material-ui/core";
import { Add } from "@material-ui/icons";
import React, { useEffect, useState } from "react";
import PollModel from "../models/pollModel";
import { usePollStore } from "../store/pollStore";
import { useUserStore } from "../store/userStore";
import SubmitTopicDialog from "./dialogs/submitTopicDialog";
import Topic from "./topic";

interface PollProps {
  pollData: PollModel;
}

export default function Poll(props: PollProps) {
  const [submitOpen, setSubmitOpen] = useState(false);

  const phone = useMediaQuery("(min-width:900px)");

  const theme = useTheme();
  const Wrapper = styled(Paper)({
    padding: theme.spacing(1),
  });
  const TopicWrapper = styled(Box)({
    margin: phone
      ? `${theme.spacing(2)}px ${theme.spacing(5)}px`
      : `${theme.spacing(2)}px ${theme.spacing(0)}px`,
  });
  const ButtonArea = styled(Box)({
    display: "flex",
    margin: theme.spacing(2),
  });
  const InfoArea = styled(Box)({
    display: "flex",
  });
  const Spacer = styled(Box)({
    flexGrow: 1,
  });

  const userStore = useUserStore();
  const pollStore = usePollStore();
  const pollData = props.pollData;

  const topics = pollData.topics;
  const eventDate = shortDate(pollData.date);
  const pollEnd = shortDate(pollData.end);
  const ended = pollData.end < new Date();

  return (
    <Wrapper>
      <Typography variant="h4">Abstimmung</Typography>
      <InfoArea>
        <Typography>
          Findet statt in: {pollData.place} <br />
          am: {eventDate}
        </Typography>
        <Spacer />
        <Typography>
          {ended ? (
            <React.Fragment>
              <Typography>
                Abstimmung ist beendet <br />
                Endete am {pollEnd}
              </Typography>
            </React.Fragment>
          ) : (
            <React.Fragment>Abstimmung endet am: {pollEnd}</React.Fragment>
          )}
        </Typography>
      </InfoArea>
      <Divider></Divider>
      <TopicWrapper>
        <Grid container spacing={2} direction="column">
          {topics
            ? props.pollData.topics.map((el) => (
                <Grid item>
                  <Topic topicModel={el} ended={ended} />
                </Grid>
              ))
            : null}
        </Grid>
      </TopicWrapper>
      <ButtonArea>
        <Spacer />
        {userStore.user && !ended ? (
          <Button
            onClick={() => setSubmitOpen(true)}
            startIcon={<Add />}
            variant="contained"
            color="primary"
          >
            Thema hinzufügen
          </Button>
        ) : null}
      </ButtonArea>

      <SubmitTopicDialog
        open={submitOpen}
        onClose={() => setSubmitOpen(false)}
        submit={async (title: string, content: string) => {
          await pollStore.submitTopic(props.pollData, title, content);
        }}
      />
    </Wrapper>
  );
}

function shortDate(date: Date): string {
  return (
    date.getDate() +
    "." +
    (date.getMonth() + 1).toString().padStart(2, "0") +
    " " +
    date.getHours() +
    ":" +
    date.getMinutes().toString().padStart(2, "0")
  );
}
