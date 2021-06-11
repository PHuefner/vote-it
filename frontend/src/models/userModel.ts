export default class UserModel{
    id: number;
    name: string;
    admin: boolean

    constructor(id: number, name: string, admin: boolean) {
        this.id = id;
        this.name = name;
        this.admin = admin;
    }
}