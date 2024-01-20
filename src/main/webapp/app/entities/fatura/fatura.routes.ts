import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FaturaComponent } from './list/fatura.component';
import { FaturaDetailComponent } from './detail/fatura-detail.component';
import { FaturaUpdateComponent } from './update/fatura-update.component';
import FaturaResolve from './route/fatura-routing-resolve.service';

const faturaRoute: Routes = [
  {
    path: '',
    component: FaturaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FaturaDetailComponent,
    resolve: {
      fatura: FaturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FaturaUpdateComponent,
    resolve: {
      fatura: FaturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FaturaUpdateComponent,
    resolve: {
      fatura: FaturaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default faturaRoute;
