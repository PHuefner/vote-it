export default class TopicModel {
  id: number;
  title: string;
  content: string;
  voted: boolean;

  constructor(id: number, title: string, content: string, voted: boolean) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.voted = voted;
  }
}
