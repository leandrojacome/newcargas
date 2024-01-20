import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TabelaFreteComponent } from './list/tabela-frete.component';
import { TabelaFreteDetailComponent } from './detail/tabela-frete-detail.component';
import { TabelaFreteUpdateComponent } from './update/tabela-frete-update.component';
import TabelaFreteResolve from './route/tabela-frete-routing-resolve.service';

const tabelaFreteRoute: Routes = [
  {
    path: '',
    component: TabelaFreteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TabelaFreteDetailComponent,
    resolve: {
      tabelaFrete: TabelaFreteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TabelaFreteUpdateComponent,
    resolve: {
      tabelaFrete: TabelaFreteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TabelaFreteUpdateComponent,
    resolve: {
      tabelaFrete: TabelaFreteResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tabelaFreteRoute;
