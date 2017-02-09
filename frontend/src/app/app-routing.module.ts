import { NgModule }             from '@angular/core';
import { RouterModule }         from '@angular/router';
import { Routes }               from '@angular/router';

import { NavbarComponent }      from './navbar/components/navbar.component';
import { HomeComponent }        from './home/components/home.component';

import { LoginComponent }       from './auth/components/login.component';
import { LoggedInGuard }        from './auth/services/logged-in.guard';

import { UserListComponent }    from './user/components/user-list.component';
import { UserDetailComponent }  from './user/components/user-detail.component';
import { UserFormComponent }    from './user/components/user-form.component';
import { ChangePasswordComponent } from './user/components/change-password.component';

import { GroupListComponent }    from './group/components/group-list.component';
import { GroupDetailComponent }  from './group/components/group-detail.component';
import { GroupFormComponent }    from './group/components/group-form.component';

const routes: Routes = [
    { path: '',                  component: HomeComponent },
    { path: 'login',             component: LoginComponent },
    { path: 'users',             component: UserListComponent, canActivate: [LoggedInGuard] },
    { path: 'users/add',         component: UserFormComponent, canActivate: [LoggedInGuard] },
    { path: 'users/:id',         component: UserDetailComponent, canActivate: [LoggedInGuard] },
    { path: 'users/:id/edit',    component: UserFormComponent, canActivate: [LoggedInGuard] },
    { path: 'users/:id/change-password', component: ChangePasswordComponent, canActivate: [LoggedInGuard] },
    { path: 'groups',            component: GroupListComponent, canActivate: [LoggedInGuard] },
    { path: 'groups/add',        component: GroupFormComponent, canActivate: [LoggedInGuard] },
    { path: 'groups/:id',        component: GroupDetailComponent, canActivate: [LoggedInGuard] },
    { path: 'groups/:id/edit',   component: GroupFormComponent, canActivate: [LoggedInGuard] }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule { }
