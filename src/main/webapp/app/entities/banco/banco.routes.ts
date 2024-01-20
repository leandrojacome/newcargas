import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { BancoComponent } from './list/banco.component';
import { BancoDetailComponent } from './detail/banco-detail.component';
import { BancoUpdateComponent } from './update/banco-update.component';
import BancoResolve from './route/banco-routing-resolve.service';

const bancoRoute: Routes = [
  {
    path: '',
    component: BancoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BancoDetailComponent,
    resolve: {
      banco: BancoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BancoUpdateComponent,
    resolve: {
      banco: BancoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BancoUpdateComponent,
    resolve: {
      banco: BancoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default bancoRoute;
