import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

@Component({
    selector: 'back-button',
    templateUrl: './back-button.component.html'
})
export class BackButtonComponent {

    constructor(private location: Location) { }

    goBack(): void {
        this.location.back();
    }

}
