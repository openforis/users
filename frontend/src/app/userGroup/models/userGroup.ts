import { User } from '../../user/models/user';
import { Group } from '../../group/models/group';

export class UserGroup {

    user: User;
    group: Group;
    statusCode: string;
    roleCode: string;

    constructor() {
    }

}
