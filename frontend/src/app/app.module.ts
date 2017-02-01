import { NgModule }               from '@angular/core';
import { BrowserModule }          from '@angular/platform-browser';
import { RouterModule }           from '@angular/router';
import { HttpModule }             from '@angular/http';
import { FormsModule }            from '@angular/forms';
import { ReactiveFormsModule }    from '@angular/forms';

import { AppComponent }           from './app.component';
import { AppRoutingModule }       from './app-routing.module';
import { AppConfiguration }       from './app-configuration';

import { NavbarComponent }        from './navbar/components/navbar.component';
import { HomeComponent }          from './home/components/home.component';

import { BackButtonComponent }    from './backButton/components/back-button.component';

import { UserListComponent }      from './user/components/user-list.component';
import { UserDetailComponent }    from './user/components/user-detail.component';
import { UserFormComponent }      from './user/components/user-form.component';
import { UserService }            from './user/services/user.service';

import { GroupListComponent }    from './group/components/group-list.component';
import { GroupDetailComponent }  from './group/components/group-detail.component';
import { GroupFormComponent }    from './group/components/group-form.component';
import { GroupService }          from './group/services/group.service';

@NgModule({
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpModule,
        FormsModule,
        ReactiveFormsModule
    ],
    declarations: [
        AppComponent,
        NavbarComponent,
        HomeComponent,
        BackButtonComponent,
        UserListComponent,
        UserDetailComponent,
        UserFormComponent,
        GroupListComponent,
        GroupDetailComponent,
        GroupFormComponent
    ],
    providers: [
        AppConfiguration,
        UserService,
        GroupService
    ],
    bootstrap: [
        AppComponent
    ]
})
export class AppModule { }
