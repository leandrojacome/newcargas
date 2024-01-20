import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { EmbarcadorComponent } from './list/embarcador.component';
import { EmbarcadorDetailComponent } from './detail/embarcador-detail.component';
import { EmbarcadorUpdateComponent } from './update/embarcador-update.component';
import EmbarcadorResolve from './route/embarcador-routing-resolve.service';

const embarcadorRoute: Routes = [
  {
    path: '',
    component: EmbarcadorComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EmbarcadorDetailComponent,
    resolve: {
      embarcador: EmbarcadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EmbarcadorUpdateComponent,
    resolve: {
      embarcador: EmbarcadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EmbarcadorUpdateComponent,
    resolve: {
      embarcador: EmbarcadorResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default embarcadorRoute;
