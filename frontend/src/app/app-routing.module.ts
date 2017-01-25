import { NgModule }             from '@angular/core';
import { RouterModule }         from '@angular/router';
import { Routes }               from '@angular/router';

import { NavbarComponent }      from './navbar/components/navbar.component';
import { HomeComponent }        from './home/components/home.component';

import { UserListComponent }    from './user/components/user-list.component';
import { UserDetailComponent }  from './user/components/user-detail.component';
import { UserFormComponent }    from './user/components/user-form.component';

import { GroupListComponent }    from './group/components/group-list.component';
import { GroupDetailComponent }  from './group/components/group-detail.component';
import { GroupFormComponent }    from './group/components/group-form.component';

const routes: Routes = [
    { path: '',                  component: HomeComponent },
    { path: 'users',             component: UserListComponent },
    { path: 'users/add',         component: UserFormComponent },
    { path: 'users/:id',         component: UserDetailComponent },
    { path: 'users/:id/edit',    component: UserFormComponent },
    { path: 'groups',            component: GroupListComponent },
    { path: 'groups/add',        component: GroupFormComponent },
    { path: 'groups/:id',        component: GroupDetailComponent },
    { path: 'groups/:id/edit',   component: GroupFormComponent }
];

@NgModule({
    imports: [ RouterModule.forRoot(routes) ],
    exports: [ RouterModule ]
})
export class AppRoutingModule { }
