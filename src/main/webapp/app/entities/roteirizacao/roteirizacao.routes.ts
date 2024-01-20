import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RoteirizacaoComponent } from './list/roteirizacao.component';
import { RoteirizacaoDetailComponent } from './detail/roteirizacao-detail.component';
import { RoteirizacaoUpdateComponent } from './update/roteirizacao-update.component';
import RoteirizacaoResolve from './route/roteirizacao-routing-resolve.service';

const roteirizacaoRoute: Routes = [
  {
    path: '',
    component: RoteirizacaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RoteirizacaoDetailComponent,
    resolve: {
      roteirizacao: RoteirizacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RoteirizacaoUpdateComponent,
    resolve: {
      roteirizacao: RoteirizacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RoteirizacaoUpdateComponent,
    resolve: {
      roteirizacao: RoteirizacaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default roteirizacaoRoute;
