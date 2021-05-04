export default class TopicModel {
  id: number;
  title: string;
  content: string;
  voted: boolean;

  constructor(id: number, title: string) {
    this.id = id;
    this.title = title;
  }
}
