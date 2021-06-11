import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  CardHeader,
  Container,
  IconButton,
  ListItem,
  ListItemSecondaryAction,
  ListItemText,
  Paper,
  styled,
  Typography,
  useTheme,
} from "@material-ui/core";
import { ArrowUpward, Delete, Spa } from "@material-ui/icons";
import React from "react";
import TopicModel from "../models/topicModel";
import { usePollStore } from "../store/pollStore";
import { useUserStore } from "../store/userStore";

interface TopicProps {
  topicModel: TopicModel;
  ended: boolean;
}

export default function Topic(props: TopicProps) {
  const theme = useTheme();
  const Wrapper = styled(Card)({
    padding: theme.spacing(1),
  });
  const Spacer = styled(Box)({
    flexGrow: 1,
  });
  const DeleteButton = styled(Button)({
    "&:hover": {
      color: "#b22222",
    },
  });

  const model = props.topicModel;
  const pollStore = usePollStore();
  const userStore = useUserStore();

  const UpvoteButton = styled(Button)({
    "&:hover": {
      color: model.voted ? "inherit" : "#32cd32",
      transform: model.voted ? "rotate(180deg)" : "",
    },
  });

  return (
    <Wrapper variant="outlined">
      <CardContent>
        <Typography variant="h5">{model.title}</Typography>
        <Typography>{model.content}</Typography>
      </CardContent>
      <CardActions>
        <Typography variant="body2">Stimmen: {model.votes}</Typography>
        <Spacer />
        {userStore.user && !props.ended ? (
          <React.Fragment>
            {model.userId == userStore.user.id || userStore.user.admin ? (
              <DeleteButton
                onClick={() => pollStore.deleteTopic(model)}
                variant="outlined"
              >
                <Delete />
              </DeleteButton>
            ) : null}
            <UpvoteButton
              variant="outlined"
              style={model.voted ? { backgroundColor: "green" } : null}
              onClick={() => pollStore.setTopicVote(model.id, !model.voted)}
            >
              <ArrowUpward />
            </UpvoteButton>
          </React.Fragment>
        ) : null}
      </CardActions>
    </Wrapper>
  );
}
