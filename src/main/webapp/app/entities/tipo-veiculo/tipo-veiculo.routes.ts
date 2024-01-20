import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TipoVeiculoComponent } from './list/tipo-veiculo.component';
import { TipoVeiculoDetailComponent } from './detail/tipo-veiculo-detail.component';
import { TipoVeiculoUpdateComponent } from './update/tipo-veiculo-update.component';
import TipoVeiculoResolve from './route/tipo-veiculo-routing-resolve.service';

const tipoVeiculoRoute: Routes = [
  {
    path: '',
    component: TipoVeiculoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoVeiculoDetailComponent,
    resolve: {
      tipoVeiculo: TipoVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoVeiculoUpdateComponent,
    resolve: {
      tipoVeiculo: TipoVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoVeiculoUpdateComponent,
    resolve: {
      tipoVeiculo: TipoVeiculoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoVeiculoRoute;
