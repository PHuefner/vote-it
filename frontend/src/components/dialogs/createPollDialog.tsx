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
  Typography,
} from "@material-ui/core";
import { DateTimePicker, MuiPickersUtilsProvider } from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import deLocale from "date-fns/locale/de";
import React, { useEffect, useState } from "react";
import { usePollStore } from "../../store/pollStore";
import PollModel from "../../models/pollModel";
import { Close } from "@material-ui/icons";

interface CreatePollDialogProps {
  onClose: () => void;
  open: boolean;
}

export default function CreatePollDialog(props: CreatePollDialogProps) {
  const [endDate, setEndDate] = useState<Date>(null);
  const [endDateError, setEndDateError] = useState<String>(null);

  const [eventDate, setEventDate] = useState<Date>(null);
  const [eventDateError, setEventDateError] = useState<String>(null);

  const [place, setPlace] = useState<string>("");
  const [placeError, setPlaceError] = useState<String>(null);

  const [snackState, setSnackState] = useState(false);
  const [snackMessage, setSnackMessage] = useState("");

  //Live Validate
  useEffect(() => setEventDateError(null), [eventDate]);
  useEffect(
    () =>
      setEndDateError(
        endDate > eventDate ? "Poll End can't be after event date" : null
      ),
    [eventDate, endDate]
  );
  useEffect(() => setPlaceError(null), [place]);

  const pollStore = usePollStore();

  //submit
  const submit = async () => {
    let error = false;
    if (!eventDate) {
      error = true;
      setEventDateError("Veranstaltungs Datum kann nicht leer sein");
    }
    if (!endDate) {
      error = true;
      setEndDateError("Abstimmungsende kann nicht leer sein");
    }
    if (!place) {
      error = true;
      setPlaceError("Veranstaltungs Ort kann nicht leer sein");
    }
    if (eventDateError || endDateError || placeError || error) {
      return;
    } else {
      try {
        await pollStore.submitPoll(new PollModel(place, endDate, eventDate));
        props.onClose();
      } catch (error) {
        setSnackMessage(error.message);
        setSnackState(true);
      }
    }
  };

  return (
    <Dialog open={props.open} onClose={props.onClose}>
      <DialogTitle> Erstelle eine neue Abstimmung!</DialogTitle>
      <form
        onSubmit={(e) => {
          e.preventDefault();
          submit();
        }}
      >
        <DialogContent>
          <MuiPickersUtilsProvider utils={DateFnsUtils} locale={deLocale}>
            <Grid container direction="column" spacing={2}>
              <Grid item>
                <DateTimePicker
                  clearable
                  disablePast={true}
                  label="Veranstaltungsdatum"
                  ampm={false}
                  onChange={(date) => setEventDate(date)}
                  value={eventDate}
                  error={eventDateError ? true : false}
                  helperText={eventDateError}
                />
              </Grid>
              <Grid item>
                <DateTimePicker
                  clearable
                  disablePast={true}
                  error={endDateError ? true : false}
                  helperText={endDateError}
                  maxDate={eventDate}
                  label="Abstimmungsende"
                  ampm={false}
                  onChange={(date) => setEndDate(date)}
                  value={endDate}
                />
              </Grid>
              <Grid item>
                <TextField
                  label="Veranstaltungsort"
                  value={place}
                  onChange={(e) => setPlace(e.target.value)}
                  error={placeError ? true : false}
                  helperText={placeError}
                />
              </Grid>
            </Grid>
          </MuiPickersUtilsProvider>
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
