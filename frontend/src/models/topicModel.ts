export default class TopicModel {
  id: number;
  title: string;
  content: string;
  votes: number;
  voted: boolean;
  userId: number;

  constructor(
    id: number,
    title: string,
    content: string,
    votes: number,
    voted: boolean,
    userId: number
  ) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.votes = votes;
    this.voted = voted;
    this.userId = userId;
  }
}
