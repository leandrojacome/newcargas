import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { RegiaoComponent } from './list/regiao.component';
import { RegiaoDetailComponent } from './detail/regiao-detail.component';
import { RegiaoUpdateComponent } from './update/regiao-update.component';
import RegiaoResolve from './route/regiao-routing-resolve.service';

const regiaoRoute: Routes = [
  {
    path: '',
    component: RegiaoComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: RegiaoDetailComponent,
    resolve: {
      regiao: RegiaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: RegiaoUpdateComponent,
    resolve: {
      regiao: RegiaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: RegiaoUpdateComponent,
    resolve: {
      regiao: RegiaoResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default regiaoRoute;
