export default class TopicModel {
  id: number;
  title: string;
  content: string;
  votes: number;
  voted: boolean;

  constructor(
    id: number,
    title: string,
    content: string,
    votes: number,
    voted: boolean
  ) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.votes = votes;
    this.voted = voted;
  }
}
