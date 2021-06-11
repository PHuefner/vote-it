import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogContentText,
  DialogTitle,
  Grid,
  IconButton,
  Snackbar,
  SnackbarContent,
  TextField,
} from "@material-ui/core";
import { Close } from "@material-ui/icons";
import React, { useEffect, useState } from "react";
import { useUserStore } from "../../store/userStore";

interface AuthDialogProps {
  register: boolean;
  open: boolean;
  onClose: () => void;
}

export default function AuthDialog(props: AuthDialogProps) {
  const title = props.register ? "Registrieren" : "Einloggen";
  const [name, setName] = useState("");
  const [nameError, setNameError] = useState<String>(null);
  const [password, setPassword] = useState("");
  const [passwordError, setPasswordError] = useState<String>(null);

  useEffect(() => setNameError(null), [name]);
  useEffect(() => setPasswordError(null), [password]);

  const userStore = useUserStore();

  const [snackState, setSnackState] = useState(false);
  const [snackMessage, setSnackMessage] = useState("");

  const submit = async () => {
    let error = false;
    if (!name) {
      error = true;
      setNameError("Name kann nicht leer sein");
    }
    if (!password) {
      error = true;
      setPasswordError("Passwort kann nicht leer sein");
    }
    if (nameError || passwordError || error) {
      return;
    } else {
      try {
        if (props.register) {
          await userStore.register(name, password);
        } else {
          await userStore.login(name, password);
        }
        props.onClose();
      } catch (error) {
        setSnackMessage(error.message);
        setSnackState(true);
      }
    }
  };

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle>{title}</DialogTitle>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          submit();
        }}
      >
        <DialogContent>
          <Grid container spacing={2} direction="column">
            <Grid item>
              <TextField
                label="Name"
                error={nameError ? true : false}
                helperText={nameError}
                autoFocus
                value={name}
                onChange={(e) => setName(e.target.value)}
              />
            </Grid>
            <Grid item>
              <TextField
                label="Passwort"
                type="password"
                error={passwordError ? true : false}
                helperText={passwordError}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => props.onClose()}>Schließen</Button>
          <Button type="submit">{title}</Button>
        </DialogActions>
      </form>

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
    </Dialog>
  );
}
