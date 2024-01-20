import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { NotaFiscalColetaComponent } from './list/nota-fiscal-coleta.component';
import { NotaFiscalColetaDetailComponent } from './detail/nota-fiscal-coleta-detail.component';
import { NotaFiscalColetaUpdateComponent } from './update/nota-fiscal-coleta-update.component';
import NotaFiscalColetaResolve from './route/nota-fiscal-coleta-routing-resolve.service';

const notaFiscalColetaRoute: Routes = [
  {
    path: '',
    component: NotaFiscalColetaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: NotaFiscalColetaDetailComponent,
    resolve: {
      notaFiscalColeta: NotaFiscalColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: NotaFiscalColetaUpdateComponent,
    resolve: {
      notaFiscalColeta: NotaFiscalColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: NotaFiscalColetaUpdateComponent,
    resolve: {
      notaFiscalColeta: NotaFiscalColetaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default notaFiscalColetaRoute;
