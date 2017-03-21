import { User } from '../../user/models/user';
import { Group } from '../../group/models/group';

export class UserGroup {

    userId: number;
    groupId: number;
    statusCode: string;
    roleCode: string;

    user: User;
    group: Group;

    constructor() { }

}
