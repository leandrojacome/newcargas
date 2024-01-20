import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TransportadoraComponent } from './list/transportadora.component';
import { TransportadoraDetailComponent } from './detail/transportadora-detail.component';
import { TransportadoraUpdateComponent } from './update/transportadora-update.component';
import TransportadoraResolve from './route/transportadora-routing-resolve.service';

const transportadoraRoute: Routes = [
  {
    path: '',
    component: TransportadoraComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TransportadoraDetailComponent,
    resolve: {
      transportadora: TransportadoraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TransportadoraUpdateComponent,
    resolve: {
      transportadora: TransportadoraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TransportadoraUpdateComponent,
    resolve: {
      transportadora: TransportadoraResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default transportadoraRoute;
