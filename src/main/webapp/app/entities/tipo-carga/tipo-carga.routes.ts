import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { TipoCargaComponent } from './list/tipo-carga.component';
import { TipoCargaDetailComponent } from './detail/tipo-carga-detail.component';
import { TipoCargaUpdateComponent } from './update/tipo-carga-update.component';
import TipoCargaResolve from './route/tipo-carga-routing-resolve.service';

const tipoCargaRoute: Routes = [
  {
    path: '',
    component: TipoCargaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: TipoCargaDetailComponent,
    resolve: {
      tipoCarga: TipoCargaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: TipoCargaUpdateComponent,
    resolve: {
      tipoCarga: TipoCargaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: TipoCargaUpdateComponent,
    resolve: {
      tipoCarga: TipoCargaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default tipoCargaRoute;
