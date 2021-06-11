import {
  AppBar,
  Avatar,
  Box,
  Button,
  Card,
  styled,
  Toolbar,
  Typography,
  useTheme,
} from "@material-ui/core";
import React, { useState } from "react";
import { useUserStore } from "../store/userStore";
import AuthDialog from "./dialogs/authDialog";
import CreatePollDialog from "./dialogs/createPollDialog";

export default function Header() {
  const [loginOpen, setLoginOpen] = useState(false);
  const [registerOpen, setRegisterOpen] = useState(false);
  const [pollOpen, setPollOpen] = useState(false);

  const theme = useTheme();
  const Spacer = styled(Box)({
    flexGrow: 1,
  });
  const ButtonBar = styled(Box)({
    display: "flex",
    "& > *": { margin: theme.spacing(1) },
  });
  const AvatarContainer = styled(Box)({
    display: "flex",
    flexDirection: "column",
  });

  const userStore = useUserStore();

  const unauthHeader = (
    <React.Fragment>
      <Button color="inherit" onClick={() => setRegisterOpen(true)}>
        Registrieren
      </Button>
      <Button color="inherit" onClick={() => setLoginOpen(true)}>
        Einloggen
      </Button>
    </React.Fragment>
  );

  const userHeader = (
    <React.Fragment>
      <Button color="inherit" onClick={() => userStore.logout()}>
        Ausloggen
      </Button>
      <AvatarContainer>
        <Typography>{userStore.user ? userStore.user.name : null}</Typography>
        <Avatar style={{ margin: "auto" }} src="/avatar.jpg">
          {userStore.user ? userStore.user.name.charAt(0) : null}
        </Avatar>
      </AvatarContainer>
    </React.Fragment>
  );

  const adminHeader = (
    <React.Fragment>
      <Button color="inherit" onClick={() => setPollOpen(true)}>
        Neue Abstimmung
      </Button>
      {userHeader}
    </React.Fragment>
  );

  let bar;
  if (userStore.user) {
    if (userStore.user.admin) {
      bar = adminHeader;
    } else {
      bar = userHeader;
    }
  } else {
    bar = unauthHeader;
  }

  return (
    <AppBar position="static">
      <Toolbar>
        <Typography variant="h3">vote.it</Typography>
        <Spacer></Spacer>
        <ButtonBar>{bar}</ButtonBar>
      </Toolbar>

      <CreatePollDialog
        open={pollOpen}
        onClose={() => setPollOpen(false)}
      ></CreatePollDialog>
      <AuthDialog
        open={loginOpen}
        onClose={() => setLoginOpen(false)}
        register={false}
      />
      <AuthDialog
        open={registerOpen}
        onClose={() => setRegisterOpen(false)}
        register={true}
      />
    </AppBar>
  );
}
