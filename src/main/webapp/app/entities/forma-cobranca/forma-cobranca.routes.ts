import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { FormaCobrancaComponent } from './list/forma-cobranca.component';
import { FormaCobrancaDetailComponent } from './detail/forma-cobranca-detail.component';
import { FormaCobrancaUpdateComponent } from './update/forma-cobranca-update.component';
import FormaCobrancaResolve from './route/forma-cobranca-routing-resolve.service';

const formaCobrancaRoute: Routes = [
  {
    path: '',
    component: FormaCobrancaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FormaCobrancaDetailComponent,
    resolve: {
      formaCobranca: FormaCobrancaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FormaCobrancaUpdateComponent,
    resolve: {
      formaCobranca: FormaCobrancaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FormaCobrancaUpdateComponent,
    resolve: {
      formaCobranca: FormaCobrancaResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default formaCobrancaRoute;
