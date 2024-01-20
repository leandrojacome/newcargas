import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { NotificacaoComponent } from './list/notificacao.component';
import { NotificacaoDetailComponent } from './detail/notificacao-detail.component';
import { NotificacaoUpdateComponent } from './update/notificacao-update.component';
import NotificacaoResolve from './route/notificacao-routing-resolve.service';

const notificacaoRoute: Routes = [
  {
    path: '',
    component: NotificacaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotificacaoDetailComponent,
    resolve: {
      notificacao: NotificacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotificacaoUpdateComponent,
    resolve: {
      notificacao: NotificacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotificacaoUpdateComponent,
    resolve: {
      notificacao: NotificacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notificacaoRoute;
