import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EstadoComponent } from './list/estado.component';
import { EstadoDetailComponent } from './detail/estado-detail.component';
import { EstadoUpdateComponent } from './update/estado-update.component';
import EstadoResolve from './route/estado-routing-resolve.service';

const estadoRoute: Routes = [
  {
    path: '',
    component: EstadoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EstadoDetailComponent,
    resolve: {
      estado: EstadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EstadoUpdateComponent,
    resolve: {
      estado: EstadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EstadoUpdateComponent,
    resolve: {
      estado: EstadoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default estadoRoute;
