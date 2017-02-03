import { Injectable } from '@angular/core';

@Injectable()
export class MessageBarService {

    public alerts: any = [];

    public add(type: string, msg: string): void {
        this.alerts.push({
            type: type,
            msg: msg,
            timeout: 5000
        });
    }

}
