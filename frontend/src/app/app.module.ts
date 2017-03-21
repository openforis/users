import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpModule } from '@angular/http';

import { AppComponent } from './app.component';

import { RouterModule }           from '@angular/router';
import { ReactiveFormsModule }    from '@angular/forms';

import { AppRoutingModule }       from './app-routing.module';
import { AppConfiguration }       from './app-configuration';

import { LoginComponent }         from './auth/components/login.component';
import { AuthService }            from './auth/services/auth.service';
import { LoggedInGuard }          from './auth/services/logged-in.guard';

import { NavbarComponent }        from './navbar/components/navbar.component';
import { HomeComponent }          from './home/components/home.component';

import { MessageBarComponent }    from './message-bar/components/message-bar.component';
import { MessageBarService }      from './message-bar/services/message-bar.service';

import { BackButtonComponent }    from './back-button/components/back-button.component';

import { UserListComponent }      from './user/components/user-list.component';
import { UserDetailComponent }    from './user/components/user-detail.component';
import { UserFormComponent }      from './user/components/user-form.component';
import { ChangePasswordComponent } from './user/components/change-password.component';
import { UserService }            from './user/services/user.service';

import { GroupListComponent }    from './group/components/group-list.component';
import { GroupDetailComponent }  from './group/components/group-detail.component';
import { GroupFormComponent }    from './group/components/group-form.component';
import { GroupService }          from './group/services/group.service';

import { GroupAssignmentComponent } from './user-group/components/group-assignment.component';
import { UserGroupService }        from './user-group/services/user-group.service';

import { AlertModule }           from 'ng2-bootstrap/alert';
import { ModalModule }           from 'ng2-bootstrap/modal';

@NgModule({
    imports: [
        BrowserModule,
        AppRoutingModule,
        HttpModule,
        FormsModule,
        ReactiveFormsModule,
        AlertModule.forRoot(),
        ModalModule.forRoot()
    ],
    declarations: [
        AppComponent,
        LoginComponent,
        NavbarComponent,
        MessageBarComponent,
        HomeComponent,
        BackButtonComponent,
        UserListComponent,
        UserDetailComponent,
        UserFormComponent,
        ChangePasswordComponent,
        GroupAssignmentComponent,
        GroupListComponent,
        GroupDetailComponent,
        GroupFormComponent
    ],
    providers: [
        AppConfiguration,
        AuthService,
        LoggedInGuard,
        UserService,
        GroupService,
        UserGroupService,
        MessageBarService
    ],
    bootstrap: [
        AppComponent
    ]
})
export class AppModule { }
