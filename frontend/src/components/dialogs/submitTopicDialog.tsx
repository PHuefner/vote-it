import {
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
  Grid,
  IconButton,
  Snackbar,
  SnackbarContent,
  TextField,
} from "@material-ui/core";
import { Close } from "@material-ui/icons";
import React, { useEffect, useState } from "react";

interface SubmitTopicDialogProps {
  open: boolean;
  onClose: () => void;
  submit: (title: string, content: string) => void;
}

export default function SubmitTopicDialog(props: SubmitTopicDialogProps) {
  const [title, setTitle] = useState("");
  const [titleError, setTitleError] = useState<String>(null);
  const [content, setContent] = useState("");
  const [contentError, setContentError] = useState<String>(null);

  const [snackState, setSnackState] = useState(false);
  const [snackMessage, setSnackMessage] = useState("");

  useEffect(() => setTitleError(null), [title]);
  useEffect(() => setContentError(null), [content]);

  const submit = async () => {
    let error = false;
    if (!title) {
      error = true;
      setTitleError("Titel kann nicht leer sein");
    }
    if (!content) {
      error = true;
      setContentError("Inhalt kann nicht leer sein");
    }
    if (titleError || contentError || error) {
      return;
    } else {
      try {
        await props.submit(title, content);
        props.onClose();
      } catch (error) {
        setSnackMessage(error.message);
        setSnackState(true);
      }
    }
  };

  return (
    <Dialog open={props.open} onClose={props.onClose} maxWidth="md" fullWidth>
      <DialogTitle>Thema hinzufügen</DialogTitle>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          submit();
        }}
      >
        <DialogContent>
          <Grid container direction="column" spacing={2}>
            <Grid item>
              <TextField
                label="Titel"
                fullWidth
                value={title}
                onChange={(e) => {
                  setTitle(e.target.value);
                }}
                error={titleError ? true : false}
                helperText={titleError}
              />
            </Grid>
            <Grid item>
              <TextField
                label="Inhalt"
                fullWidth
                multiline
                rows={4}
                value={content}
                onChange={(e) => {
                  setContent(e.target.value);
                }}
                error={contentError ? true : false}
                helperText={contentError}
                variant="outlined"
              ></TextField>
            </Grid>
          </Grid>
        </DialogContent>
        <DialogActions>
          <Button onClick={props.onClose}>Schließen</Button>
          <Button type="submit">Erstellen</Button>
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
