import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TomadaPrecoComponent } from './list/tomada-preco.component';
import { TomadaPrecoDetailComponent } from './detail/tomada-preco-detail.component';
import { TomadaPrecoUpdateComponent } from './update/tomada-preco-update.component';
import TomadaPrecoResolve from './route/tomada-preco-routing-resolve.service';

const tomadaPrecoRoute: Routes = [
  {
    path: '',
    component: TomadaPrecoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TomadaPrecoDetailComponent,
    resolve: {
      tomadaPreco: TomadaPrecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TomadaPrecoUpdateComponent,
    resolve: {
      tomadaPreco: TomadaPrecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TomadaPrecoUpdateComponent,
    resolve: {
      tomadaPreco: TomadaPrecoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tomadaPrecoRoute;
