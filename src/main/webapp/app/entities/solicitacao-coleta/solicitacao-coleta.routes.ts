import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { SolicitacaoColetaComponent } from './list/solicitacao-coleta.component';
import { SolicitacaoColetaDetailComponent } from './detail/solicitacao-coleta-detail.component';
import { SolicitacaoColetaUpdateComponent } from './update/solicitacao-coleta-update.component';
import SolicitacaoColetaResolve from './route/solicitacao-coleta-routing-resolve.service';

const solicitacaoColetaRoute: Routes = [
  {
    path: '',
    component: SolicitacaoColetaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SolicitacaoColetaDetailComponent,
    resolve: {
      solicitacaoColeta: SolicitacaoColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SolicitacaoColetaUpdateComponent,
    resolve: {
      solicitacaoColeta: SolicitacaoColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SolicitacaoColetaUpdateComponent,
    resolve: {
      solicitacaoColeta: SolicitacaoColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default solicitacaoColetaRoute;
