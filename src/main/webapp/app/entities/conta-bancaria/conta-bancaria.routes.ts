import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContaBancariaComponent } from './list/conta-bancaria.component';
import { ContaBancariaDetailComponent } from './detail/conta-bancaria-detail.component';
import { ContaBancariaUpdateComponent } from './update/conta-bancaria-update.component';
import ContaBancariaResolve from './route/conta-bancaria-routing-resolve.service';

const contaBancariaRoute: Routes = [
  {
    path: '',
    component: ContaBancariaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContaBancariaDetailComponent,
    resolve: {
      contaBancaria: ContaBancariaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContaBancariaUpdateComponent,
    resolve: {
      contaBancaria: ContaBancariaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContaBancariaUpdateComponent,
    resolve: {
      contaBancaria: ContaBancariaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contaBancariaRoute;
