export class Group {

    id: number;
    name: string;
    label: string;
    description?: string;
    enabled: boolean;
    system_defined: boolean;
    visibility_code: string;

    constructor() {
        this.enabled = false;
    }

}
