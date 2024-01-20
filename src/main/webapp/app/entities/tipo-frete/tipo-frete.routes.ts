import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TipoFreteComponent } from './list/tipo-frete.component';
import { TipoFreteDetailComponent } from './detail/tipo-frete-detail.component';
import { TipoFreteUpdateComponent } from './update/tipo-frete-update.component';
import TipoFreteResolve from './route/tipo-frete-routing-resolve.service';

const tipoFreteRoute: Routes = [
  {
    path: '',
    component: TipoFreteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoFreteDetailComponent,
    resolve: {
      tipoFrete: TipoFreteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoFreteUpdateComponent,
    resolve: {
      tipoFrete: TipoFreteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoFreteUpdateComponent,
    resolve: {
      tipoFrete: TipoFreteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoFreteRoute;
