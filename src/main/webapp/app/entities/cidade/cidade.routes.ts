import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { CidadeComponent } from './list/cidade.component';
import { CidadeDetailComponent } from './detail/cidade-detail.component';
import { CidadeUpdateComponent } from './update/cidade-update.component';
import CidadeResolve from './route/cidade-routing-resolve.service';

const cidadeRoute: Routes = [
  {
    path: '',
    component: CidadeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CidadeDetailComponent,
    resolve: {
      cidade: CidadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CidadeUpdateComponent,
    resolve: {
      cidade: CidadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CidadeUpdateComponent,
    resolve: {
      cidade: CidadeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default cidadeRoute;
