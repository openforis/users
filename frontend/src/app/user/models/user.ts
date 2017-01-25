export class User {

    id: number;
    username: string;
    rawPassword: string;
    enabled: boolean;

    constructor() {
        this.enabled = false;
    }

}
