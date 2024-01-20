import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { ContratacaoComponent } from './list/contratacao.component';
import { ContratacaoDetailComponent } from './detail/contratacao-detail.component';
import { ContratacaoUpdateComponent } from './update/contratacao-update.component';
import ContratacaoResolve from './route/contratacao-routing-resolve.service';

const contratacaoRoute: Routes = [
  {
    path: '',
    component: ContratacaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ContratacaoDetailComponent,
    resolve: {
      contratacao: ContratacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ContratacaoUpdateComponent,
    resolve: {
      contratacao: ContratacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ContratacaoUpdateComponent,
    resolve: {
      contratacao: ContratacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default contratacaoRoute;
