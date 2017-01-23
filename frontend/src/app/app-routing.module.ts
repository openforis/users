import { NgModule }             from '@angular/core';
import { RouterModule }         from '@angular/router';
import { Routes }               from '@angular/router';

import { NavbarComponent }      from './navbar/components/navbar.component';
import { HomeComponent }        from './home/components/home.component';
import { UserListComponent }    from './user/components/user-list.component';
import { UserDetailComponent }  from './user/components/user-detail.component';
import { UserFormComponent }    from './user/components/user-form.component';

const routes: Routes = [
    { path: '',                  component: HomeComponent },
    { path: 'users',             component: UserListComponent },
    { path: 'users/add',         component: UserFormComponent },
    { path: 'users/:id',         component: UserDetailComponent },
    { path: 'users/:id/edit',    component: UserFormComponent }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule { }
