import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { HistoricoStatusColetaComponent } from './list/historico-status-coleta.component';
import { HistoricoStatusColetaDetailComponent } from './detail/historico-status-coleta-detail.component';
import { HistoricoStatusColetaUpdateComponent } from './update/historico-status-coleta-update.component';
import HistoricoStatusColetaResolve from './route/historico-status-coleta-routing-resolve.service';

const historicoStatusColetaRoute: Routes = [
  {
    path: '',
    component: HistoricoStatusColetaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HistoricoStatusColetaDetailComponent,
    resolve: {
      historicoStatusColeta: HistoricoStatusColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HistoricoStatusColetaUpdateComponent,
    resolve: {
      historicoStatusColeta: HistoricoStatusColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HistoricoStatusColetaUpdateComponent,
    resolve: {
      historicoStatusColeta: HistoricoStatusColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default historicoStatusColetaRoute;
