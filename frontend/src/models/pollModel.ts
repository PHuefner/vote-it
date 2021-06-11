import TopicModel from "./topicModel";

export default class PollModel {
  id: number;
  place: string;
  end: Date;
  date: Date;
  topics: TopicModel[];

  constructor(place: string, end: Date, date: Date, id?: number) {
    this.place = place;
    this.end = new Date(end);
    this.date = new Date(date);
    if (id) {
      this.id = id;
    }
  }
}
