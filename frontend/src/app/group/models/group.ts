export class Group {

    id: number;
    name: string;
    label: string;
    description?: string;
    enabled: boolean;
    systemDefined: boolean;
    visibilityCode: string;

    constructor() {
        this.enabled = false;
    }

}
