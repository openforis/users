import { NgModule }               from '@angular/core';
import { BrowserModule }          from '@angular/platform-browser';
import { RouterModule }           from '@angular/router';
import { HttpModule }             from '@angular/http';
import { FormsModule }            from '@angular/forms';

import { AppComponent }           from './app.component';
import { AppRoutingModule }       from './app-routing.module';
import { AppConfiguration }       from './app-configuration';

import { NavbarComponent }        from './navbar/components/navbar.component';
import { HomeComponent }          from './home/components/home.component';
import { UserListComponent }      from './user/components/user-list.component';
import { UserDetailComponent }    from './user/components/user-detail.component';
import { UserFormComponent }      from './user/components/user-form.component';

import { UserService }            from './user/services/user.service';

@NgModule({
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpModule,
        FormsModule
    ],
    declarations: [
        AppComponent,
        NavbarComponent,
        HomeComponent,
        UserListComponent,
        UserDetailComponent,
        UserFormComponent
    ],
    providers: [
        AppConfiguration,
        UserService
    ],
    bootstrap: [
        AppComponent
    ]
})
export class AppModule { }
