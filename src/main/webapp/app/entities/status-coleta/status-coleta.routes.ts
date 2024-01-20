import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { StatusColetaComponent } from './list/status-coleta.component';
import { StatusColetaDetailComponent } from './detail/status-coleta-detail.component';
import { StatusColetaUpdateComponent } from './update/status-coleta-update.component';
import StatusColetaResolve from './route/status-coleta-routing-resolve.service';

const statusColetaRoute: Routes = [
  {
    path: '',
    component: StatusColetaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StatusColetaDetailComponent,
    resolve: {
      statusColeta: StatusColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StatusColetaUpdateComponent,
    resolve: {
      statusColeta: StatusColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StatusColetaUpdateComponent,
    resolve: {
      statusColeta: StatusColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default statusColetaRoute;
